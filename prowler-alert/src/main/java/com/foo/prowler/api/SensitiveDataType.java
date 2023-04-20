package com.foo.prowler.api;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "sensitive_datatype")
public class SensitiveDataType {
    @Id
    @NotNull
    @Enumerated(EnumType.STRING)
    private SensitiveTypeName id;

    private String description;
}
