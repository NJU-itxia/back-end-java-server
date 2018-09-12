# IT侠预约系统后端服务

> 与back-end-controller中的node层一起使用， 使用node做权限验证和反向代理，java服务器只在服务器本地启动，不考虑权限问题

## 调试

### 前置条件

1. java环境(java8, 不要用java9+版本)
2. 使用ide时需要添加lombok插件
3. maven环境
4. mysql环境，推荐mariadb
5. (二选一)自行搭建mysql环境，编写application.properties
6. (二选一)找 @Tipwheal(qq:674714966) 提供测试环境

### 步骤(cmd)

1. git clone https://github.com/NJU-itxia/back-end-java-server.git
2. mvn install
3. cd target
4. java -jar back-end-0.0.1-SNAPSHOT.jar

### 步骤(idea)

> 可选其中的一种方式

- open url 从github下载打开
- 先git clone，再open项目，点击运行

### 开始调试

1. 可以运行项目中的test，使用mockmvc测试
2. 使用浏览器访问controller中的url 或者chrome用户使用postman测试

## 部署

### 注意事项

1. 因为没有考虑权限验证的问题，只是由node层代理，需要禁止来自其他ip的访问
2. 建议在docker中启动(DockerFile及相关教程后续补充)
3. 更方便的调试及部署方式正在考虑ing
4. **不要上传配置文件x3**

### 部署

> 见调试

## 目录结构及说明

- src (源码)
    - main (项目代码)
        - java/com/itxia/backend (代码)
            - controller (处理http请求)
            - data (数据层封装)
            - service (业务逻辑，主要编写部分)
            - BackEndApplication.java (Spring boot入口类)
        - resource (资源文件路径)
    - test (测试代码)
- .gitignore
- pom.xml (maven使用)

## 详细文档稍候

(这里是空的)