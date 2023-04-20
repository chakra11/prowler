CREATE SCHEMA IF NOT EXISTS `alert`;

CREATE TABLE IF NOT EXISTS `alert`.`sensitive_datatype` (
    id VARCHAR(255) NOT NULL,
    description VARCHAR(255),

    PRIMARY KEY (id)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `alert`.`redacted_content` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    content TEXT NOT NULL ,

    PRIMARY KEY (id)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `alert`.`redacted_sensitive_map` (
    redacted_content_id BIGINT NOT NULL,
    sensitive_datatype_id VARCHAR(255) NOT NULL,

    PRIMARY KEY (redacted_content_id, sensitive_datatype_id),
    FOREIGN KEY (redacted_content_id) REFERENCES redacted_content(id) ON DELETE CASCADE,
    FOREIGN KEY (sensitive_datatype_id) REFERENCES sensitive_datatype(id) ON DELETE CASCADE
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `alert`.`violation` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    ts TIMESTAMP NOT NULL,
    hostname VARCHAR(255) NOT NULL,
    application VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    redacted_content_id BIGINT,

    PRIMARY KEY (id),
    FOREIGN KEY (redacted_content_id) REFERENCES redacted_content(id) ON DELETE CASCADE,
    INDEX (hostname),
    INDEX (application),
    INDEX (ts),
    INDEX (hostname, application),
    INDEX (hostname, ts),
    INDEX (application, ts),
    INDEX (hostname, application, ts)
) ENGINE=INNODB;


