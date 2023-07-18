package com.neu.dy.authority.biz.service.core;

import com.neu.dy.authority.entity.core.Org;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname orgService
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/7/18 10:18
 * @Created by maicaoboy
 */
@SpringBootTest
public class OrgServiceTest {
    @Autowired
    OrgService orgService;

    @Test
    void testFindOrgPage() {
        ArrayList<Long> list = new ArrayList<>();
        list.add(1126157877761802337L);
        List<Org> children = orgService.findChildren(list);
        System.out.println(children.toString());
    }
}
