package com.qing.fan.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author QingFan 2022/6/22 09:49
 * @version 1.0.0
 */
@RestController
public class HelloController {

    @RequestMapping("/getInfo")
    public String getInfo() {
        return "This is App1";
    }
}
