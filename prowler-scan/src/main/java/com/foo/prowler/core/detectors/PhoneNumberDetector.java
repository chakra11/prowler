package com.foo.prowler.core.detectors;

import com.foo.prowler.api.SensitiveDataType;
import com.foo.prowler.api.SensitiveTypeName;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class PhoneNumberDetector extends SensitiveDataTypeDetector {
    private static final Pattern pattern = Pattern.compile("\\b([+]?\\d{1,2}[-\\s]?|)\\d{3}[-\\s]?\\d{3}[-\\s]?\\d{4}\\b");

    @Override
    public SensitiveDataType getSensitiveDataType() {
        return new SensitiveDataType(SensitiveTypeName.PHONE_NUMBER);
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public List<String> getKeywords() {
        return Arrays.asList(
                "phone",
                "mobile",
                "call"
        );
    }

    @Override
    public String getReplacement() {
        return "[PHONE_NUMBER]";
    }
}
