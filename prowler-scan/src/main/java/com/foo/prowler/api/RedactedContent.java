package com.foo.prowler.api;

import javax.validation.constraints.NotNull;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RedactedContent {
    @NotNull
    private String content;

    @NotNull
    private List<SensitiveDataType> sensitiveDataTypes;
}
