package com.what.spring.mapper;

import com.what.spring.pojo.Avatar;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AvatarMapper {

    List<Avatar> getAllDefaultAvatar();

}
