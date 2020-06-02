package com.sumeng.producer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @date: 2020/6/2 10:23
 * @author: sumeng
 */
@Controller
public class MyController {

    @RequestMapping("/ip")
    public String ip(HttpServletRequest request) {
        System.out.println(request.getRemoteAddr());
        return request.getRemoteAddr();
    }
}
