CREATE TABLE ONLINE_PAYMENT
(
  ID                     VARBINARY(32) NOT NULL,
  VERSION                NUMERIC(8)    NOT NULL DEFAULT 1,
  AMOUNT                 NUMERIC(8, 2) NOT NULL,
  STATUS                 NUMERIC(2)    NOT NULL,
  CORRELATION_KEY        VARCHAR(32)   NOT NULL,
  CORRELATION_VALUE      VARBINARY(32) NOT NULL,
  PAYEE_CONTEXT          VARCHAR(32),
  PAYEE_OBJ_ID           VARBINARY(32),
  METHOD                 VARCHAR(32)   NOT NULL,
  SUBJECT                VARCHAR(32),
  BODY                   VARCHAR(1024),
  PROVIDER_SPECIFIC_INFO VARCHAR(4000),
  RAW_NOTIFICATION       VARCHAR(4000),
  CREATED_AT             DATETIME      NOT NULL,
  LAST_MODIFIED_AT       DATETIME      NOT NULL,
  EXPIRES_AT             DATETIME      NOT NULL,
  CONSTRAINT PK_ONLINE_PAYMENT PRIMARY KEY (ID)
);

CREATE TABLE ONLINE_PAYMENT_RESULT_NOTIFICATION
(
  ID         VARBINARY(32) NOT NULL,
  OP_ID      VARBINARY(32) NOT NULL,
  AMOUNT     NUMERIC(8, 2) NOT NULL,
  CODE       NUMERIC(2)    NOT NULL,
  TEXT       VARCHAR(4000),
  CREATED_AT DATETIME      NOT NULL,
  CONSTRAINT PK_ONLINE_PAYMENT_RESULT_NOTIFICATION PRIMARY KEY (ID)
);