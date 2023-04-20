package com.foo.prowler.api;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AlertList {
    @NotNull
    private int count;

    private List<Alert> alerts;
}
