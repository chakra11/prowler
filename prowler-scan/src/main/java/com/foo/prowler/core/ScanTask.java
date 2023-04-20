package com.foo.prowler.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.foo.prowler.api.Alert;
import com.foo.prowler.api.RedactedContent;
import com.foo.prowler.core.detectors.SensitiveDataTypeDetectorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ScanTask implements Runnable {
    private static final String scanHistoryFilePath =
            System.getProperty("user.dir") + "/scan_history/start.txt";
    private static final String alertDirectory =
            System.getProperty("user.dir") + "/scan_history/alerts";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern("uuuu-MM-dd-HH-mm-ss", Locale.US)
            .withResolverStyle(ResolverStyle.STRICT);
    private static final Logger LOGGER = LoggerFactory.getLogger(ScanTask.class);
    private static final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    private final ScanTaskConfiguration config;
    private final ScanPipeline pipeline;

    public ScanTask(ScanTaskConfiguration config) {
        this.config = config;

        var detectors = SensitiveDataTypeDetectorFactory.getDetectors(
                config.getSensitiveDataTypes());

        pipeline = ScanPipeline.builder()
                .sensitiveDataTypeDetectors(detectors)
                .build();

        this.initialize();
    }

    // TODO: Store scan history in DB instead of FS
    private void initialize() {
        try {
            Path path = Paths.get(scanHistoryFilePath);
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        } catch (FileAlreadyExistsException ex) {
            // do nothing
        } catch (Exception ex) {
            LOGGER.error("Could not create scan history file", ex);
        }

        try {
            Path path = Paths.get(alertDirectory);
            Files.createDirectories(path);
        } catch (Exception ex) {
            LOGGER.error("Could not create alert directory", ex);
        }
    }

    @Override
    public void run() {
        LOGGER.info("Scan task run started");
        LocalDateTime lastScanStartTime = getLastScanStartTime();
        long cutoff = lastScanStartTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // start scan process
        LocalDateTime currentScanStartTime = LocalDateTime.now();
        List<Path> filesModifiedAfterLastScan = getFilesModifiedAfterLastScan(cutoff);
        for (var path : filesModifiedAfterLastScan) {
            scanPath(path, currentScanStartTime);
        }
        // end scan process

        recordScanHistory(currentScanStartTime.format(dateTimeFormatter));
        LOGGER.info("Scan task run finished");
    }

    private LocalDateTime getLastScanStartTime() {
        String lastScanStartTimeStr = "";
        LocalDateTime lastScanStartTime = LocalDateTime.of(2023, 1, 1, 0, 0);
        try {
            FileInputStream fis = new FileInputStream(scanHistoryFilePath);
            Scanner scanner = new Scanner(fis);
            while (scanner.hasNextLine()) {
                lastScanStartTimeStr = scanner.nextLine();
            }
            scanner.close();
            fis.close();
            if (!lastScanStartTimeStr.isEmpty()) {
                lastScanStartTime = LocalDateTime.parse(lastScanStartTimeStr, dateTimeFormatter);
            }
        } catch (Exception ex) {
            LOGGER.error("Could not get last scan start time", ex);
        }

        return lastScanStartTime;
    }

    private List<Path> getFilesModifiedAfterLastScan(long cutoff) {
        List<Path> pathList = new ArrayList<>();

        Path scanDir = Paths.get(config.getLocation());
        if (Files.isDirectory(scanDir)) {
            try {
                pathList = Files.list(scanDir)
                        .filter(p -> !Files.isDirectory(p))
                        .filter(p -> p.toFile().lastModified() > cutoff)
                        .toList();
            } catch (Exception ex) {
                LOGGER.error("Error listing files in scan location");
            }
        } else {
            LOGGER.error("Invalid location in scanner config");
        }
        return pathList;
    }

    private void recordScanHistory(String currentScanStartTimeStr) {
        String contentToAppend = currentScanStartTimeStr + "\r\n";
        try {
            Files.write(
                    Paths.get(scanHistoryFilePath),
                    contentToAppend.getBytes(),
                    StandardOpenOption.APPEND);
        } catch (Exception ex) {
            LOGGER.error("Could not record current scan start time", ex);
        }
    }

    private void scanPath(Path path, LocalDateTime currentScanStartTime) {
        try {
            List<String> allLines = Files.readAllLines(path);
            for (String line : allLines) {
                var context = new ScanContext(line);
                pipeline.run(context);
                if (!context.getSensitiveDataTypes().isEmpty()) {
                    LOGGER.info("Sensitive data type detected");
                    var redactedContent = new RedactedContent(
                            context.getRedactedText(),
                            context.getSensitiveDataTypes()
                    );
                    var alert = Alert.builder()
                            .ts(currentScanStartTime)
                            .hostname(config.getHostname())
                            .application(config.getApplication())
                            .location(config.getLocation())
                            .redactedContent(redactedContent)
                            .build();
                    LOGGER.info("Creating alert");
                    // TODO: Send to alert queue
                    String alertJson = objectWriter.writeValueAsString(alert) + "\r\n";
                    Path alertFilePath = Paths.get(
                            alertDirectory + "/alert_" + currentScanStartTime.format(dateTimeFormatter) + ".json");
                    try {
                        Files.createFile(alertFilePath);
                    } catch (FileAlreadyExistsException ex) {
                        // do nothing
                    }
                    try {
                        Files.write(
                                alertFilePath,
                                alertJson.getBytes(),
                                StandardOpenOption.APPEND);
                    } catch (Exception ex) {
                        LOGGER.error("Could not write alerts", ex);
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Could not read file", ex);
        }
    }
}
