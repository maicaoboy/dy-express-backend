package com.neu.dy.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neu.dy.user.mapper.MemberMapper;
import com.neu.dy.user.service.IMemberService;
import com.neu.dy.user.eneity.Member;
import org.springframework.stereotype.Service;

/**
 * 用户服务类实现
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {

}