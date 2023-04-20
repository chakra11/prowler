package com.foo.prowler.core.detectors;

import com.foo.prowler.api.SensitiveDataType;
import com.foo.prowler.api.SensitiveTypeName;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class BankAccountNumberDetector extends SensitiveDataTypeDetector {
    private static final Pattern pattern = Pattern.compile("\\b\\d{11,17}\\b");

    @Override
    public SensitiveDataType getSensitiveDataType() {
        return new SensitiveDataType(SensitiveTypeName.BANK_ACCOUNT_NUMBER);
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public List<String> getKeywords() {
        return Arrays.asList(
                "bank",
                "account",
                "neft",
                "imps"
        );
    }

    @Override
    public String getReplacement() {
        return "[BANK_ACCOUNT_NUMBER]";
    }
}
