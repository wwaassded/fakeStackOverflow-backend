package com.what.spring.service.nginx;

import com.what.spring.component.NginxProperties;
import com.what.spring.mapper.AvatarMapper;
import com.what.spring.pojo.Avatar;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Predicate;

@Service
public class NginxService {

    private final NginxProperties nginxProperties;

    private final String avatarUrlPrefix;

    @Resource(name = "avatarMapper")
    private AvatarMapper avatarMapper;

    @Resource(name = "myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "myCacheThreadPool")
    private ThreadPoolExecutor cacheThreadPoolExecutor;

    NginxService(NginxProperties nginxProperties) {
        this.nginxProperties = nginxProperties;
        avatarUrlPrefix = nginxProperties.getNginxRoot() + nginxProperties.getNginxAvatarRoot();
    }

    public List<String> getAllDefaultAvatarUrl() {
        String object = (String) redisTemplate.opsForValue().get(nginxProperties.getAvatarRedisKey());
        if (object != null && object.charAt(0) == '[' && object.charAt(object.length() - 1) == ']') {
            object = object.substring(1, object.length() - 1);
            String[] split = object.split(",");
            return new ArrayList<>(Arrays.asList(split));
        }
        List<String> allDefaultAvatars = avatarMapper.getAllDefaultAvatar().stream().map(this::getUrlByAvatar).toList();
        cacheThreadPoolExecutor.execute(() -> redisTemplate.opsForValue().set(nginxProperties.getAvatarRedisKey(), allDefaultAvatars.toString(), Duration.ofHours(1)));
        return allDefaultAvatars;
    }

    private String getUrlByAvatar(Avatar avatar) {
        return avatarUrlPrefix + avatar.getName();
    }
}
