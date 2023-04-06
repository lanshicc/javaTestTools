package com.zhaobh.test.springboottesttool.entity;

import lombok.Data;

@Data
public class Group {
    private Integer id;
    private Integer leaderId;
    private String name;
    private Integer partId;
}
