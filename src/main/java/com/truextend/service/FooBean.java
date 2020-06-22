package com.truextend.service;

import org.springframework.stereotype.Component;

@Component
public class FooBean {
    public String hello() {
        return "Hello, world!";
    }
}