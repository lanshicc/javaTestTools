package com.zhaobh.test.springboottesttool.entity;

import lombok.Data;

@Data
public class Wages {
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
    /**业务主任晋升奖金*/
    private Double upgradeBonusAS = 0.00;
    /**业务经理晋升奖金*/
    private Double upgradeBonusUM = 0.00;
    /**助理业务主任津贴*/
    private Double bonusTS = 0.00;
    /**销售责任津贴*/
    private Double bonusSD = 0.00;
    /**总监特别津贴*/
    private Double bonusAD = 0.00;
    /**高级业务总监职务津贴*/
    private Double bonusHD = 0.00;
    /**增组奖金*/
    private Double addGroupBonus = 0.00;
    /**增部奖金*/
    private Double addPartBonus = 0.00;
    /**管理津贴*/
    private Double manageBonus = 0.00;
    /**直辖组季度奖*/
    private Double groupQuarterlyBonus = 0.00;
    /**直辖部季度奖*/
    private Double partQuarterlyBonus = 0.00;
}
