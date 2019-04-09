create table online_payment
(
  ID                     VARBINARY(32) NOT NULL,
  VERSION                NUMERIC(8)    NOT NULL DEFAULT 1,
  AMOUNT                 NUMERIC(8, 2) NOT NULL,
  CORRELATION_KEY        VARCHAR(32)   NOT NULL,
  CORRELATION_VALUE      VARCHAR(32)   NOT NULL,
  STATUS                 VARCHAR(32)   NOT NULL,
  METHOD                 VARCHAR(32)   NOT NULL,
  SUBJECT                VARCHAR(128)  NULL,
  BODY                   VARCHAR(1028) NULL,
  PROVIDER_SPECIFIC_INFO VARCHAR(4000) NULL,
  CREATED_AT             DATETIME      NOT NULL,
  LAST_MODIFIED_AT       DATETIME      NOT NULL,
  PRIMARY KEY (ID)
);