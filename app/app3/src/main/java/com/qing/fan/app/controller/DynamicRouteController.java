package com.qing.fan.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author QingFan 2022/6/24 13:47
 * @version 1.0.0
 */
@RestController
@RequestMapping("/v3")
public class DynamicRouteController {

    @RequestMapping("/info")
    public String info() {
        return "张翠山";
    }
}
