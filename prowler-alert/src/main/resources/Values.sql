INSERT INTO `alert`.`sensitive_datatype`(id, description) VALUES("CREDIT_CARD_NUMBER", "Credit Card number");
INSERT INTO `alert`.`sensitive_datatype`(id, description) VALUES("BANK_ACCOUNT_NUMBER", "Bank Account number");
INSERT INTO `alert`.`sensitive_datatype`(id, description) VALUES("PHONE_NUMBER", "Phone number");

INSERT INTO `alert`.`redacted_content`(content) VALUES("my credit card number is xxxx-xxxx-xxxx-xxxx");

INSERT INTO `alert`.`redacted_sensitive_map`(redacted_content_id, sensitive_datatype_id) VALUES(1,"CREDIT_CARD_NUMBER");

INSERT INTO `alert`.`violation`(ts, hostname, application, location, redacted_content_id) VALUES("2023-04-15 14:41:00", "host1", "app1", "/logs/dummy", 1);

