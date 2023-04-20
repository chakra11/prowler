package com.foo.prowler.core;

import com.foo.prowler.core.detectors.SensitiveDataTypeDetector;
import lombok.Builder;

import java.util.List;

@Builder
public class ScanPipeline {
    private List<SensitiveDataTypeDetector> sensitiveDataTypeDetectors;

    public void run(ScanContext context) {
        if(sensitiveDataTypeDetectors == null || sensitiveDataTypeDetectors.isEmpty()) {
            return;
        }
        for(var sensitiveDataTypeDetector : sensitiveDataTypeDetectors) {
            sensitiveDataTypeDetector.run(context);
        }
    }
}
