DROP TABLE IF EXISTS abtesting;

CREATE TABLE abtesting (
  service_name      VARCHAR(100) PRIMARY KEY NOT NULL,
  active            VARCHAR(1) NOT NULL,
  endpoint          VARCHAR(100) NOT NULL,
  weight            INT);

# http://orgservice-new:8083 需要和新orgService url映射匹配，并且端口是8083
#1-10内的随机数，如果大于5，则转发到新orgService中，所以这里的5算是一半的概率，不是随便配置的
INSERT INTO abtesting (service_name, active,  endpoint, weight) VALUES ('organizationservice', 'Y','http://localhost:8083/orgnew',5);