package com.zhaobh.test.springboottesttool.controller;

import com.zhaobh.test.springboottesttool.aop.SysLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @SysLog("查询用户列表")
    @GetMapping("/list")
    public String getUserList() {
        System.out.println("调用获取用户列表方法");
        return "用户列表";
    }

}
