package com.foo.prowler.core;

import com.foo.prowler.api.SensitiveDataType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ScanContext {
    private String OriginalText;
    private String RedactedText;
    private List<SensitiveDataType> SensitiveDataTypes;

    public ScanContext(String text) {
        this.OriginalText = text;
        this.RedactedText = text;
        this.SensitiveDataTypes = new ArrayList<>();
    }

    public void addSensitiveDataType(SensitiveDataType sensitiveDataType) {
        SensitiveDataTypes.add(sensitiveDataType);
    }
}
