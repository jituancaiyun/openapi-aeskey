CREATE TABLE t_openapi_aeskey
(
  id           INT DEFAULT '0' NOT NULL AUTO_INCREMENT PRIMARY KEY
  COMMENT '主键密钥ID',
  gmt_create   DATETIME        NOT NULL
  COMMENT '创建时间',
  gmt_modified DATETIME        NOT NULL
  COMMENT '修改时间',
  org_id       VARCHAR(32)     NOT NULL
  COMMENT '组织id',
  aes_key      VARCHAR(128)    NOT NULL
  COMMENT '密钥base64'
);

CREATE INDEX t_openapi_aeskey_org_id_index
  ON t_openapi_aeskey (org_id);

COMMENT ON TABLE t_openapi_aeskey IS '开放平台加密密钥库';

