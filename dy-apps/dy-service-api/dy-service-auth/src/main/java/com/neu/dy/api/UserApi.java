package com.neu.dy.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neu.dy.authority.entity.auth.User;
import com.neu.dy.base.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "dy-auth-server", path = "/user")
public interface UserApi {
    @RequestMapping(value = {"/ds/{id}"}, method = {RequestMethod.GET})
    Map<String, Object> getDataScopeById(@PathVariable("id") Long paramLong);

    @RequestMapping(value = {"/find"}, method = {RequestMethod.GET})
    R<List<Long>> findAllUserId();

    @GetMapping({"/{id}"})
    R<User> get(@PathVariable Long paramLong);

    @GetMapping({"/page"})
    R<Page<User>> page(@RequestParam("current") Long paramLong1, @RequestParam("size") Long paramLong2, @RequestParam(name = "orgId", required = false) Long paramLong3, @RequestParam(name = "stationId", required = false) Long paramLong4, @RequestParam(name = "name", required = false) String paramString1, @RequestParam(name = "account", required = false) String paramString2, @RequestParam(name = "mobile", required = false) String paramString3);

    @GetMapping({"/list"})
    R<List<User>> list(@RequestParam(name = "ids", required = false) List<Long> paramList, @RequestParam(name = "stationId", required = false) Long paramLong1, @RequestParam(name = "name", required = false) String paramString, @RequestParam(name = "orgId", required = false) Long paramLong2);
}
