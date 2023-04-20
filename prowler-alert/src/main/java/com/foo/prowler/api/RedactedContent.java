package com.foo.prowler.api;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "redacted_content")
public class RedactedContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private long id;

    @NotNull
    private String content;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade =
                    {
                            CascadeType.DETACH,
                            CascadeType.REFRESH,
                            CascadeType.PERSIST
                    })
    @JoinTable(
            name = "redacted_sensitive_map",
            joinColumns = {@JoinColumn(name = "redacted_content_id")},
            inverseJoinColumns = {@JoinColumn(name = "sensitive_datatype_id")}
    )
    private List<SensitiveDataType> sensitiveDataTypes;

    public RedactedContent(String content, List<SensitiveDataType> sensitiveDataTypes) {
        this.content = content;
        this.sensitiveDataTypes = sensitiveDataTypes;
    }
}
