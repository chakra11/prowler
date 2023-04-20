package com.foo.prowler.core.detectors;

import com.foo.prowler.api.SensitiveDataType;
import com.foo.prowler.core.ScanContext;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SensitiveDataTypeDetector {
    abstract SensitiveDataType getSensitiveDataType();
    abstract Pattern getPattern();
    abstract List<String> getKeywords();
    abstract String getReplacement();

    public void run(ScanContext context) {
        String text = context.getRedactedText();
        Matcher matcher = this.getPattern().matcher(text);
        if (matcher.find()
                // TODO: optimize keyword match
                && this.getKeywords().stream().anyMatch(keyword -> StringUtils.containsIgnoreCase(text, keyword))) {
            context.setRedactedText(matcher.replaceAll(this.getReplacement()));
            context.addSensitiveDataType(this.getSensitiveDataType());
        }
    }
}
