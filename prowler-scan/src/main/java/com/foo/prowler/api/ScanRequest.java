package com.foo.prowler.api;

import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScanRequest {
    @NotNull
    private ScanCommand command;
}
