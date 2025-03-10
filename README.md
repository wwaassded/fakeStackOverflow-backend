# FakeStackOverflow

所需技术:

* springboot3 + springmvc
* mysql + redis + mybatis + hikari
* ~~考虑学习并尝试使用RabbitMQ等消息队列~~

开发日志

* [ ]  完成数据库的初始化 以及 pojo 类的生成
* [ ]  程序的开发文件应该放到对应的文件夹中去
* [ ]  需要全局的异常管理
* [ ]  第三方认证应该支持解析用户的关注列表找到其中已经关联的用户
* [ ]  使用线程池更新用户session的生存时间 后续可以考虑换成消息队列实现该功能
* [ ]  通过redis订阅功能实现sessionCache与redis之间的同步关系

[](https://)

[**架构设计**](doc/fundamental.md)
