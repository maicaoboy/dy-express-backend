package com.neu.dy.order.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

/**
 * 项目启动时执行当前类中的run方法
 */
@Service
@Slf4j
public class CommandLineRunnerImpl implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {

    }
//    @Autowired
//    private ReloadDroolsRulesService reloadDroolsRulesService;
//
//    @Override
//    public void run(String... args) throws Exception {
//        reloadDroolsRulesService.reload();
//    }
}