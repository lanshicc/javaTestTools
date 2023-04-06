package com.zhaobh.test.springboottesttool.entity;

import lombok.Data;

@Data
public class CountWages {
    /**首佣*/
    private Double FYC = 0.00;
    /**转正奖金*/
    private Double positiveBonus = 0.00;
    /**季度奖*/
    private Double quarterlyBonus = 0.00;
    /**年终奖*/
    private Double annualBonus = 0.00;
    /**增员奖*/
    private Double addPeopleBonus = 0.00;
    /**增才奖*/
    private Double addTalentBonus = 0.00;
    /*计算值*/
    /**新人责任津贴*/
    private Double newAllowance = 0.00;
    /**手续费*/
    private Double wages = 0.00;
}
