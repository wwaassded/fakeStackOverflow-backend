package com.what.spring.mapper;

import com.what.spring.pojo.user.ThirdPlatFromInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ThirdPlatFormInfoMapper {
    void InsertIntoThirdPlatFormInfoRow(ThirdPlatFromInfo thirdPlatFromInfo);
}
