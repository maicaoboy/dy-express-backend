package com.neu.dy.authority.biz.service.auth;

import com.neu.dy.authority.entity.auth.Menu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Classname MenuServiceTest
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/7/18 10:37
 * @Created by maicaoboy
 */
@SpringBootTest
class MenuServiceTest {

    @Autowired
    MenuService menuService;

    @Test
    void removeByIds() {
        ArrayList<Long> longs = new ArrayList<>();
        longs.add(1L);
        boolean result = menuService.removeByIds(longs);
        System.out.println(result);

    }

    @Test
    void findVisibleMenu() {
        List<Menu> visibleMenu = menuService.findVisibleMenu(null, 1130608690714380961L);
        System.out.println(visibleMenu);
    }
}