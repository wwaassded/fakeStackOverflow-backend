package com.what.spring.mapper;

import com.what.spring.pojo.PlatfromUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    /**
     * @param thirdPlatformId 与网站账号直接关联的第三方用户id
     * @return 用户信息 <br/>
     * description  通常用户通过第三方账号登录时使用
     */
    PlatfromUser getPlatformUserByThirdplatformId(@Param("id") int thirdPlatformId);

    void insertPlatformUserWithDefaultTime(PlatfromUser platfromUser);

}
