package com.neu.dy.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neu.dy.base.R;
import com.neu.dy.user.eneity.Member;
import com.neu.dy.user.service.IMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户前端控制器
 */
@Log4j2
@RestController
@Api(value = "MemberController", tags = "客户服务")
@RequestMapping("member")
public class MemberController {
    @Autowired
    private IMemberService memberService;

    /**
     * 新增
     * @param member
     * @return
     */
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("")
    public R save(@RequestBody Member member){
        boolean result = memberService.save(member);
        return R.success("新增成功");
    }

    /**
     * 查询某个用户的详细信息
     * @param id
     * @return
     */

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable(name = "id")String id){
        Member member = memberService.getById(id);
        return R.success(member);
    }


    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R page(Integer page, Integer pageSize){
        Page<Member> pageInfo = new Page<>(page, pageSize);
        memberService.page(pageInfo);
        System.out.println(pageInfo.getTotal());
        return R.success(pageInfo);
    }

    /**
     * 修改
     *
     * @param id
     * @param member
     * @return
     */
    @PutMapping("/{id}")
    public R update(@PathVariable(name = "id") String id, @RequestBody Member member) {
        member.setId(id);
        memberService.updateById(member);
        return R.success();
    }
    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public R del(@PathVariable(name = "id") String id) {
        memberService.removeById(id);
        return R.success("删除成功");
    }
}