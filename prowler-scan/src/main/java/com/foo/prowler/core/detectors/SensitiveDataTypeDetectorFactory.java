package com.foo.prowler.core.detectors;

import com.foo.prowler.api.SensitiveTypeName;

import javax.ws.rs.NotSupportedException;
import java.util.ArrayList;
import java.util.List;

public class SensitiveDataTypeDetectorFactory {
    public static SensitiveDataTypeDetector getDetector(SensitiveTypeName sensitiveTypeName) {
        return switch (sensitiveTypeName) {
            case CREDIT_CARD_NUMBER -> new CreditCardNumberDetector();
            case BANK_ACCOUNT_NUMBER -> new BankAccountNumberDetector();
            case PHONE_NUMBER -> new PhoneNumberDetector();
            default -> throw new NotSupportedException();
        };
    }

    public static List<SensitiveDataTypeDetector> getDetectors(List<SensitiveTypeName> sensitiveTypeNameList) {
        List<SensitiveDataTypeDetector> detectors = new ArrayList<>();
        for(var sensitiveTypeName : sensitiveTypeNameList) {
            detectors.add(getDetector(sensitiveTypeName));
        }
        return detectors;
    }
}
