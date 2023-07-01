package com.neu.dy.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neu.dy.base.R;
import com.neu.dy.user.eneity.AddressBook;
import com.neu.dy.user.service.IAddressBookService;
import lombok.extern.log4j.Log4j2;


import net.oschina.j2cache.CacheChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 地址簿
 */
@Log4j2
@RestController
@RequestMapping("addressBook")
public class AddressBookController {
    @Autowired
    private IAddressBookService addressBookService;

    @Autowired
    private CacheChannel cacheChannel;

    private String region = "addressBook";


    /**
     * 新增地址簿
     *
     * @param entity
     * @return
     */
    @PostMapping("")
    public R save(@RequestBody AddressBook entity) {
        if (1 == entity.getIsDefault()) {
            addressBookService.lambdaUpdate().set(AddressBook::getIsDefault, 0).eq(AddressBook::getUserId, entity.getUserId()).update();
        }

        boolean result = addressBookService.save(entity);
        if (result) {
            //载入缓存
            cacheChannel.set(region,entity.getId(),entity);
        }
        return R.success();
    }

    /**
     * 查询地址簿详情
     *
     * @param id
     * @return
     */
    @GetMapping("detail/{id}")
//    @Cache(region = "addressBook",key = "ab",params = "id")
    public AddressBook detail(@PathVariable(name = "id") String id) {
       AddressBook addressBook = addressBookService.getById(id);
       return addressBook;
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @param userId
     * @return
     */
    @GetMapping("page")
    public R page(Integer page, Integer pageSize, String userId, String keyword) {
        Page<AddressBook> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(userId),AddressBook::getUserId,userId);
        queryWrapper.like(StringUtils.isNotBlank(keyword),AddressBook::getName, keyword);
        queryWrapper.like(StringUtils.isNotBlank(keyword),AddressBook::getPhoneNumber, keyword);
        queryWrapper.like(StringUtils.isNotBlank(keyword),AddressBook::getCompanyName, keyword);
        addressBookService.page(iPage,queryWrapper);

        return R.success(iPage);
    }

    /**
     * 修改
     *
     * @param id
     * @param entity
     * @return
     */
    @PutMapping("/{id}")
//    @CacheEvictor(value = {@Cache(region = "addressBook",key = "ab",params = "1.id")})
    public R update(@PathVariable(name = "id") String id, @RequestBody AddressBook entity) {
        entity.setId(id);
        if (1 == entity.getIsDefault()) {
            addressBookService.lambdaUpdate().set(AddressBook::getIsDefault, 0).eq(AddressBook::getUserId, entity.getUserId()).update();
        }
        boolean result = addressBookService.updateById(entity);
        return R.success();
    }
    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
//    @CacheEvictor({@Cache(region = "addressBook",key = "ab",params = "id")})
    public R delete(@PathVariable(name = "id") String id) {
        boolean result = addressBookService.removeById(id);
        return R.success();
    }
}