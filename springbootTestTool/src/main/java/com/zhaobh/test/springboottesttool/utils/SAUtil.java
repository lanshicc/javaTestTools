package com.zhaobh.test.springboottesttool.utils;

import com.zhaobh.test.springboottesttool.entity.Agent;
import com.zhaobh.test.springboottesttool.entity.AgentMonth;
import com.zhaobh.test.springboottesttool.entity.SAWages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class SAUtil {
    /**
     * @param agent 代理人信息
     * @param monthNum 要计算的代理人工作月
     * @param addAgentList 直接增员列表
     * @return
     */
    public SAWages computeSAWages(Agent agent, Integer monthNum, List<Agent> addAgentList) throws ParseException {
        // FYC
        List<AgentMonth> agentMonthList = agent.getAgentMonthList();
        AgentMonth agentMonth = agentMonthList.get(monthNum - 1);
        // 初始化本月手续费计算结果
        SAWages saWages = new SAWages();
        Integer month = agentMonth.getMonth();
        if (month.equals(monthNum)) {
            // 定位到当前工作月

            /*计算FYC*/
            Double fyc = agentMonth.getFyc();
            saWages.setFYC(fyc);

            /*计算转正奖金*/
            // 获取上个月的职级
            if(monthNum > 1) {
                AgentMonth agentMonthLast = agentMonthList.get(monthNum - 1);
                String rank = agentMonthLast.getRank();
                // 如果上个月的职级为TA，则获取转正奖金
                if ("TA".equals(rank)) {
                    // 是否首次转正标志
                    Boolean firstPositiveFlag = true;
                    // 存储非TA月份
                    List<Integer> notTAMonth = new ArrayList<>();
                    // 计算转正奖金
                    for (int i = 0; i < monthNum - 1; i++) {
                        // 判断以往月份是否有职级不为TA的
                        if (!agentMonthList.get(i).getRank().equals("TA")) {
                            firstPositiveFlag = false;
                            notTAMonth.add(i + 1);
                        }
                    }
                    Double lastThreeMonthFyc = 0.00;
                    if (firstPositiveFlag && monthNum <= 4) {
                        // 如果是初次转正且转正期间少于三个月，计算见习期间(之前月份)的FYC和
                        for (int i = 0; i < monthNum - 1; i++) {
                            lastThreeMonthFyc = agentMonthList.get(i + 1).getFyc();
                        }
                    } else {
                        // 如果不是初次转正，计算近3个月的FYC
                        for (int i = 0; i < 3; i++) {
                            lastThreeMonthFyc = agentMonthList.get(monthNum-i).getFyc();
                        }
                    }
                    saWages.setPositiveBonus(lastThreeMonthFyc * 0.25);
                }
            }

            /*计算季度奖*/
            // 判断当月是否为季度月 入职时间+工作月
            String entryTime = agent.getEntryTime();
            // 获取入职月份
            Integer entryMonth = Integer.valueOf(entryTime.substring(entryTime.indexOf("-")));
            // 获取入职年份
            Integer entryYear = Integer.valueOf(entryTime.substring(0,entryTime.indexOf("-")));
            // 获取当前自然月
            Integer naturalMonth = (entryMonth + monthNum) % 12;
            // 获取当前自然年
            Integer naturalYear = entryYear + ((entryMonth + monthNum) / 12);
            // 获取自然时间
            String naturalTimeStr = naturalYear + "-" + naturalMonth;
            if (naturalMonth.equals(3) || naturalMonth.equals(6) || naturalMonth.equals(9) || naturalMonth.equals(12)) {
                // 当前为季度奖发放月 获取当季累计个人FYC，本季职级为SA、TS发放
                Double FYCQuarterly = 0.00;
                for (int i = 0; i < 3; i++) {
                    if (monthNum-i > 0) {
                        AgentMonth agentMonthQuarterly = agentMonthList.get(monthNum-i);
                        String rank = agentMonthQuarterly.getRank();
                        if (rank.equals("SA") || rank.equals("TS")) {
                            FYCQuarterly = FYCQuarterly + agentMonthQuarterly.getFyc();
                        }
                    }
                }
                Double quarterlyBonus = getQuarterlyBonus(FYCQuarterly);
                saWages.setQuarterlyBonus(quarterlyBonus);
            }

            /*计算年终奖*/
            if (naturalMonth.equals(12)) {
                Double FYCAnnual = 0.00;
                // 年度有效月：用来计算每月平均FYC
                Integer useMonthAnnual = 0;
                // 当前为年终奖发放月 获取当年个人月均FYC,职级为TA的月份和FYC不计算在内
                for (int i = 0; i < 12; i++) {
                    if (monthNum - i > 0) {
                        AgentMonth agentMonthAnnual = agentMonthList.get(monthNum - i);
                        String rank = agentMonthAnnual.getRank();
                        if (!rank.equals("TA")) {
                            FYCAnnual = FYCAnnual + agentMonthAnnual.getFyc();
                            useMonthAnnual++;
                        }
                    }
                }
                Double annualBonus = getAnnualBonus(FYCAnnual, useMonthAnnual);
                saWages.setAnnualBonus(annualBonus);
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");

            /**计算增员奖金*/
            // 获取入职时间小于12个月的直接增员列表
            Double addPeopleBonus = 0.00;
            for (int i = 0; i < addAgentList.size(); i++) {
                String addEntryTime = addAgentList.get(i).getEntryTime();
                String limitTime = getLimitTime(12, naturalTimeStr);
                // 入职时间限制--该时间之后入职可给增员人提供增员奖
                Date limitDate = simpleDateFormat.parse(limitTime);
                Date addEntryDate = simpleDateFormat.parse(addEntryTime);
                // 获取当前自然时间--该时间之后入职本月不参与计算
                Date naturalTime = simpleDateFormat.parse(naturalTimeStr);
                if (limitDate.compareTo(addEntryDate) < 0 && addEntryDate.compareTo(naturalTime) <= 0) {
                    // 入职时间小于12个月,获取该直接增员的本月FYC
                    // 获取当前自然月为该直接增员的第几个工作月
                    Integer addWorkMonth = getMonthByEntryTime(addEntryTime, naturalYear + "-" + naturalMonth);
                    Double addFyc = addAgentList.get(i).getAgentMonthList().get(addWorkMonth - 1).getFyc();
                    addPeopleBonus = addPeopleBonus + getAddPeopleBonus(addFyc);
                }
            }
            saWages.setAddPeopleBonus(addPeopleBonus);

            /**计算增才奖金*/
            // 获取入职时间小于6个月的直接增员列表
            Double addTalnetBonus = 0.00;
            for (int i = 0; i < addAgentList.size(); i++) {
                String addEntryTime = addAgentList.get(i).getEntryTime();
                String limitTime = getLimitTime(6, naturalTimeStr);

            }

        }

        log.info(saWages.toString());
        return saWages;

    }

    /**
     * 根据当前自然时间和月份数量获取限制月的时间
     * @param month 月份数量
     * @param naturalTime 自然时间
     * @return
     */
    public String getLimitTime(Integer month, String naturalTime) {
        Integer limitMonth = 0;
        Integer limitYear = 0;
        // 获取自然年份
        Integer naturalYear = Integer.valueOf(naturalTime.substring(0, naturalTime.indexOf("-")));
        // 获取自然月份
        Integer naturalMonth = Integer.valueOf(naturalTime.substring(naturalTime.indexOf("-") + 1));
        if (naturalMonth > month) {
            limitYear = naturalYear;
            limitMonth = naturalMonth - month;
        } else {
            limitMonth = 12 - ((month - naturalMonth) % 12);
            limitYear = naturalYear  - ((month - naturalMonth) / 12 + 1);
        }

        return limitYear + "-" + limitMonth;
    }

    /**
     * 根据直接增员的逐月信息和当前工作月份判断是否本月首次达成泰星
     * @param workMonth 当前工作月份
     * @param addAgentMonth 逐月信息
     * @return
     */
    public Boolean isTaiStar(Integer workMonth, List<AgentMonth> addAgentMonth) {

    }

    /**
     * 获取增员奖
     * @param addFyc 直接增员本月的FYC
     * @return
     */
    public Double getAddPeopleBonus(Double addFyc) {
        Double addPeopleBonus = 0.00;
        if (addFyc < 1000) {
            // 小于1000 0.05
            addPeopleBonus = addFyc * 0.05;
        } else {
            // 大于1000 0.10
            addPeopleBonus = addFyc * 0.10;
        }
        return addPeopleBonus;
    }

    /**
     * 根据自然月和入职月份获取工作月
     * @param entryTime 入职时间
     * @param naturalTime 自然时间
     * @return
     */
    public Integer getMonthByEntryTime(String entryTime, String naturalTime) {
        Integer workMonth = 0;
        // 获取入职年份
        Integer entryYear = Integer.valueOf(entryTime.substring(0, entryTime.indexOf("-")));
        // 获取入职月份
        Integer entryMonth = Integer.valueOf(entryTime.substring(entryTime.indexOf("-") + 1));
        // 获取自然年份
        Integer naturalYear = Integer.valueOf(naturalTime.substring(0, naturalTime.indexOf("-")));
        // 获取自然月份
        Integer naturalMonth = Integer.valueOf(naturalTime.substring(naturalTime.indexOf("-") + 1));
        // 比较入职年份和自然年份
        if (entryYear.equals(naturalYear)) {
            //比较入职月份和自然月份
            if (entryMonth > naturalMonth) {
                return null;
            } else {
                workMonth = naturalMonth - entryMonth + 1;
            }
        } else if (entryYear > naturalYear) {
            // 入职时间晚于自然时间，返回空值
            return null;
        } else if (entryYear < naturalYear) {
            // 入职年份小于自然年份，工作时间+年份差*12
            workMonth = naturalMonth - entryMonth + 1 + (naturalYear - entryYear)*12;
        }
        return workMonth;
    }


    /**
     * 根据季度FYC获取季度奖金额
     * @param FYCQuarterly 季度FYC
     * @return
     */
    public Double getQuarterlyBonus(Double FYCQuarterly) {
        Double quarterlyBonus = 0.00;
        // 根据季度FYC定档
        int level = FYCQuarterly>=0.00&FYCQuarterly<1800.00?0:
                    FYCQuarterly>=1800.00&FYCQuarterly<4500.00?1:
                    FYCQuarterly>=4500.00&FYCQuarterly<9000.00?2:
                    FYCQuarterly>=9000.00&FYCQuarterly<15000.00?3:
                    FYCQuarterly>=15000.00?4: 100 ;
        switch (level) {
            case 0:
                break;
            case 1:
                quarterlyBonus = FYCQuarterly * 0.06;
                break;
            case 2:
                quarterlyBonus = FYCQuarterly * 0.09;
                break;
            case 3:
                quarterlyBonus = FYCQuarterly * 0.12;
                break;
            case 4:
                quarterlyBonus = FYCQuarterly * 0.14;
                break;
            default:
                break;
        }
        return quarterlyBonus;
    }


    /** 根据年度累计FYC和年度有效月份获取年终奖金额
     * @param FYCAnnual  年度累计FYC
     * @param useMonthAnnual 年度有效月份
     * @return
     */
    public Double getAnnualBonus(Double FYCAnnual, Integer useMonthAnnual) {
        Double annualBonus = 0.00;
        // 获取年度月平均FYC，用于奖励系数定档
        Double FYCAnnualAverage = FYCAnnual / useMonthAnnual;
        int level = FYCAnnualAverage >= 800&FYCAnnualAverage < 1500 ? 1:
                FYCAnnualAverage >= 1500&FYCAnnualAverage < 3000 ? 2:
                        FYCAnnualAverage >= 3000&FYCAnnualAverage < 5000 ? 3:
                                FYCAnnualAverage >= 5000 ? 4 : 100;
        switch (level) {
            case 1:
                annualBonus = FYCAnnual * 0.025;
                break;
            case 2:
                annualBonus = FYCAnnual * 0.045;
                break;
            case 3:
                annualBonus = FYCAnnual * 0.07;
                break;
            case 4:
                annualBonus = FYCAnnual * 0.09;
                break;
            default:
                break;
        }
        return annualBonus;
    }
}
