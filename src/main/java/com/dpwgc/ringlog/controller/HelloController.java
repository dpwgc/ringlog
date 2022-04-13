package com.dpwgc.ringlog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloController {

    @RequestMapping("/")
    public String hello() {
        return "hello";
    }
}
