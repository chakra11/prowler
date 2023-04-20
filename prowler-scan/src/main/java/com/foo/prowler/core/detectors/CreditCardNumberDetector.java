package com.foo.prowler.core.detectors;

import com.foo.prowler.api.SensitiveDataType;
import com.foo.prowler.api.SensitiveTypeName;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CreditCardNumberDetector extends SensitiveDataTypeDetector {
    private static final Pattern pattern = Pattern.compile("\\b\\d{4}[-\\s]?\\d{4}[-\\s]?\\d{4}[-\\s]?\\d{4}\\b");

    @Override
    public SensitiveDataType getSensitiveDataType() {
        return new SensitiveDataType(SensitiveTypeName.CREDIT_CARD_NUMBER);
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public List<String> getKeywords() {
        return Arrays.asList(
                "credit card",
                "visa",
                "master card",
                "cc"
        );
    }

    @Override
    public String getReplacement() {
        return "[CREDIT_CARD_NUMBER]";
    }
}
