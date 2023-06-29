package com.neu.dy.server.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neu.dy.user.eneity.Member;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 */
@Mapper
public interface MemberMapper extends BaseMapper<Member> {
}