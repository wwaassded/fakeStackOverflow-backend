package com.what.spring.component;

import com.what.spring.Exception.StringEmptyOrNull;
import com.what.spring.pojo.ThirdPlatformUserInfo;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

/**
 * 空的接口类型 只作为标识使用 所有的第三方验证配置相关信息都必须继承该接口才可使用
 * <br/>
 * 第三方的配置通过该类从ymal文件中读入到类中并注入到service相关类中
 * <br/>
 * 以提高第三方验证的扩展性
 */
public interface ThirdAuthConfig {
    Optional<ThirdPlatformUserInfo> getThirdPlatformUserInfo(String ThirdAuthCode) throws StringEmptyOrNull;
}
