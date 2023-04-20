package com.foo.prowler.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "violation")
@NamedQueries({
        @NamedQuery(name = "com.foo.prowler.api.Alert.findAll",
                query = "select e from Alert e"),
        @NamedQuery(name = "com.foo.prowler.api.Alert.findByHostnameApplicationTs",
        query = "select e from Alert e " +
                "where (:hostname is null or e.hostname = :hostname) " +
                "and (:application is null or e.application = :application) " +
                "and (:start is null or e.ts >= :start) " +
                "and (:end is null or e.ts < :end) ")
})
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private long id;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime ts;

    @NotNull
    private String hostname;

    @NotNull
    private String application;

    @NotNull
    private String location;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "redacted_content_id", referencedColumnName = "id")
    private RedactedContent redactedContent;

    public Alert(LocalDateTime ts, String hostname, String application, String location, RedactedContent redactedContent) {
        this.ts = ts;
        this.hostname = hostname;
        this.application = application;
        this.location = location;
        this.redactedContent = redactedContent;
    }
}
