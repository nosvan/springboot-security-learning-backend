package com.sbsl.springbootsecuritylearning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String hello(){
        return "received data from main controller";
    }

    @GetMapping("/bye")
    public String goodbye(){
        return "goodbye from main controller";
    }
}