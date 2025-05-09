# BootDrp 进销存管理系统

## 平台简介

在[BootDo](https://github.com/lcg0124/bootdo)基础上，增加了进销存管理（DRP）模块。

![](./bootdrp.png)

### 效果图

<table>
    <tr>
        <td><img src="./src/main/resources/static/img/blog/workbench.png"/></td>
        <td><img src="./src/main/resources/static/img/blog/se-order.png"/></td>
    </tr>
    <tr>
        <td><img src="./src/main/resources/static/img/blog/se-entry.png"/></td>
        <td><img src="./src/main/resources/static/img/blog/s-recon.png"/></td>
    </tr>
</table>

## 内置功能

1.	用户管理：用户是系统操作者，该功能主要完成系统用户配置。
2.	机构管理：配置系统组织机构（公司、部门、小组），树结构展现，可随意调整上下级。
3.	区域管理：系统城市区域模型，如：国家、省市、地市、区县的维护。
4.	菜单管理：配置系统菜单，操作权限，按钮权限标识等。
5.	角色管理：角色菜单权限分配、设置角色按机构进行数据范围权限划分。
6.	字典管理：对系统中经常使用的一些较为固定的数据进行维护，如：是否、男女、类别、级别等。
7.	操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
8.	连接池监视：监视当期系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。
9.	工作流引擎：实现业务工单流转、在线流程设计器。
10.	进销存管理：进销存管理功能，如：采购、销售、仓库、商品、出纳、财务、运营、报表。


## 技术选型

1、后端

* 核心框架：Spring Boot
* 安全框架：Apache Shiro 
* 模板引擎：Thymeleaf
* 持久层框架：MyBatis、MyBatis Plus
* 数据库连接池：Alibaba Druid 
* 缓存框架：Ehcache 、Redis
* 日志管理：SLF4J 
* 工具类：Apache Commons、Jackson、hutool

2、前端

* JS框架：jQuery、jqGrid
* 客户端验证：JQuery Validation 
* 富文本在线编辑：summernote
* 数据表格：bootstrapTable
* 弹出层：layer
* 树结构控件：jsTree

4、平台

* 服务器中间件：SpringBoot内置
* 数据库支持：目前仅提供MySql数据库的支持，但不限于数据库
* 开发环境：Java、Eclipse Java EE 、Maven 、Git

## 安全考虑

1. 开发语言：系统采用Java 语言开发，具有卓越的通用性、高效性、平台移植性和安全性。
2. 分层设计：（数据库层，数据访问层，业务逻辑层，展示层）层次清楚，低耦合，各层必须通过接口才能接入并进行参数校验（如：在展示层不可直接操作数据库），保证数据操作的安全。
3. 双重验证：用户表单提交双验证：包括服务器端验证及客户端验证，防止用户通过浏览器恶意修改（如不可写文本域、隐藏变量篡改、上传非法文件等），跳过客户端验证操作数据库。
4. 安全编码：用户表单提交所有数据，在服务器端都进行安全编码，防止用户提交非法脚本及SQL注入获取敏感数据等，确保数据安全。
5. 密码加密：登录用户密码进行SHA1散列加密，此加密方法是不可逆的。保证密文泄露后的安全问题。
6. 强制访问：系统对所有管理端链接都进行用户身份权限验证，防止用户直接填写url进行访问。

## 版权声明

本软件使用 [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0) 协议，请严格遵照协议内容

- [x] 注：已上内容为整体规化，部分功能还在实现中
