# OpenApi AesKey Service
开放平台AES密钥管理服务, 提供密钥的生成与查询接口.

* 该工程是一个标准的`Java web`工程, 打好的`war`包可以部署到`tomcat`等`servlet`容器
* 工程使用`maven`构建请提前安装`maven`, 打包命令:`mvn clean package`
* 由于密钥服务需要数据库存储, 所以必须先建表, 建表sql在:`sql/t_openapi_aeskey-mysql.sql`
* 配置修改, 工程使用的配置文件`src/main/resources/app.properties`,详细配置说明如下:
```properties
#应用appId
openapi.appId=94181123
#应用密钥
openapi.appSecret=6fe73d451cd37f8a
#开放平台接口地址必须以"/"结尾
openapi.http.baseUrl=http://10.0.10.49:8081/openapi/
#接口建立连接超时时间
openapi.http.connectTimeoutMillis=3000
#数据源相关配置, 主要用于存储AES_KEY
#数据库驱动, 目前只支持mysql
openapi.jdbc.driverClassName=com.mysql.jdbc.Driver
#数据库地址
openapi.jdbc.url=jdbc:mysql://10.0.10.41:3306/shinemo_openapi
#数据库用户名
openapi.jdbc.username=root
#数据库用户密码
openapi.jdbc.password=shinemo123
#数据源连接池最大连接数
openapi.jdbc.maxTotal=100
#数据源连接池最大空闲连接数
openapi.jdbc.maxIdle=20
#数据源连接池最大等待超时时间
openapi.jdbc.maxWaitMillis=10000

```
