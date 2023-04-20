package com.foo.prowler.api;

import javax.validation.constraints.NotNull;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SensitiveDataType {
    @NotNull
    private SensitiveTypeName id;

    private String description;

    public SensitiveDataType(SensitiveTypeName id) {
        this.id = id;
    }
}
