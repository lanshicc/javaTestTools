package com.zhaobh.test.springboottesttool.entity;

import lombok.Data;

@Data
public class AgentMonth {

    /**工作月份*/
    private Integer month;
    /**职级*/
    private String rank = "TA";
    /**hwp类型 0-非hwp 1-初级hwp,2-中级hwp,3-高级hwp,4-初级hwp合伙人,5-中级hwp合伙人,
     * 6-高级hwp合伙人,7-初级hwp企业家,8-中级hwp企业家,9-高级hwp企业家
     */
    private Integer hwpType = 0;
    /**首佣*/
    private Double fyc = 0.00;
    /**与增员人是否有血缘关系*/
    private Boolean isBlood = true;

    private CountWages countWages;



}
