# 用户相关信息的介绍

1. 用户表的设计
    * 用户的ID(自增涨的主键) id int
    * 用户相关联的第三方账号的id 唯一 应该建立索引 频繁查询的键 并应该成为外键用于用户查询相关的第三方用户的信息 int
    * 用户的姓名 not null varchar(32)
    * 用户的 email varchar(64) 应该可以允许用户自动输入 也可从第三方信息中获取 可以为空
    * 用户的头像uri地址 varchar(64) not null 可以用户亲自上传 也可以通过第三方平台用户信息读取
    * 用户的昵称
    * 用户的密码(数据库中应该存储加密过后的密码)
    * 用户是否活跃信息(代替真正的delete操作) ====> 是否需要定期的删除掉非活跃的用户
    * created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP 用户创建日期
    * data_id 存储所有 数据相关表的主键(用户关注的博主 用户所有的提问 用户所有的回答等)
    * setting_id 关联到用户设置项的杂乱数据
2. 第三方用户信息表的设计
    * 第三方id 主键
    * 用于获取第三方平台用户信息的token
3. 登陆问题
    1. [支持github第三方登录](login/thirdAuthLogin.md)
 