package com.zhaobh.test.springboottesttool.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 代理人
 * @author zhaobinhan
 */
@Data
public class Agent {
    /**编号*/
    private Integer id;
    /**姓名*/
    private String name;
    /**入职时间*/
    private String entryTime;
    /**增员人编号*/
    private Integer masterId;
    /**是否优质新人*/
    private Boolean isNewS;
    /**加佣比例*/
    private Double addWagesRate;

    private String groupName;
    private Integer groupId;
    private String partName;
    private Integer partId;
    private Boolean isGroupLeader;
    private Boolean isPartLeader;


    /**代理人每月信息*/
    private List<AgentMonth> agentMonthList;
}
