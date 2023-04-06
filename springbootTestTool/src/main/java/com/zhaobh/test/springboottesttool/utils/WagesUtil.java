package com.zhaobh.test.springboottesttool.utils;

import com.zhaobh.test.springboottesttool.entity.Agent;
import com.zhaobh.test.springboottesttool.entity.AgentMonth;
import com.zhaobh.test.springboottesttool.entity.Wages;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class WagesUtil {


    /**
     * 根据代理人信息计算代理人本工作月的收益
     * @param agent 代理人基本信息及每月的FYC、职级等信息
     * @param workMonth 工作月
     * @param addAgentList 直接增员列表
     * @return
     */
    public Wages getWages(Agent agent, Integer workMonth, List<Agent> addAgentList){

        // 各项手续费
        Wages wages = new Wages();

        // 根据代理人工作时间获取当前自然月信息--方便计算季度奖、年终奖
        String entryTime = agent.getEntryTime();
        // 获取入职月份
        Integer entryMonth = Integer.valueOf(entryTime.substring(entryTime.indexOf("-")));
        // 获取入职年份
        Integer entryYear = Integer.valueOf(entryTime.substring(0,entryTime.indexOf("-")));
        // 获取当前自然月
        Integer naturalMonth = (entryMonth + workMonth) % 12 == 0 ? 12 : (entryMonth + workMonth) % 12;
        // 获取当前自然年
        Integer naturalYear = (entryMonth + workMonth) % 12 == 0 ? entryYear + ((entryMonth + workMonth) / 12) - 1 : entryYear + ((entryMonth + workMonth) / 12);
        // 获取自然时间
        String naturalTimeStr = naturalYear + "-" + naturalMonth;

        // 获取代理人逐月信息
        List<AgentMonth> agentMonthList = agent.getAgentMonthList();

        // 获取代理人本月信息
        AgentMonth agentMonth = agentMonthList.get(workMonth - 1);
        // 获取代理人本月职级
        String rank = agentMonth.getRank();

        /*---晋升奖金--一次性---*/

        /*转正奖金*/
        if ("SA".equals(rank) && workMonth > 1 && "TA".equals(agentMonthList.get(workMonth - 2).getRank())) {
            // 本月职级为SA，上月职级为TA，发放转正奖金---最近三个月任职见习期间累计FYC*0.25
            Double positiveBonus = getPositiveBonus(agentMonthList, workMonth);
            wages.setPositiveBonus(positiveBonus);
        }
        /*业务主任晋升奖金*/
        if ("AS".equals(rank) && "AS".equals(agentMonthList.get(workMonth - 2).getRank())) {
            wages.setUpgradeBonusAS(5000.00);
        }
        /*业务经理晋升奖金*/
        if ("UM".equals(rank) && "SS".equals(agentMonthList.get(workMonth - 2).getRank())) {
            wages.setUpgradeBonusUM(10000.00);
        }

        /*---个人收益---*/

        /*计算FYC*/
        Double fyc = getFyc(agentMonth.getFyc(), agentMonth.getRank());
        wages.setFYC(fyc);
        /*个人销售奖金(季度奖)*/
        int[] quarterlyMonths = {3, 6, 9, 12};
        // 发放季度奖的职级
        String[] quarterlyRanks = {"SA", "TS", "SE", "SM", "SD"};
        if (Arrays.asList(quarterlyMonths).contains(naturalMonth) && Arrays.asList(quarterlyRanks).contains(rank)) {

            Double quarterlyBonus = getQuarterlyBonus(agentMonthList, workMonth);
            wages.setQuarterlyBonus(quarterlyBonus);
        }
        /*个人年终奖金*/
        if (naturalMonth == 12 && !"TA".equals(rank)) {
            Double annualBonus = getAnnualBonus(agentMonthList, workMonth);
            wages.setAnnualBonus(annualBonus);
        }

        /*---增员类收益---*/
        /*增员奖金*/
        // 发放增员奖的职级
        String[] addPeopleRanks = {"SA", "TS", "SE", "SM", "SD"};
        if (Arrays.asList(addPeopleRanks).contains(rank)) {
            Double addPeopleBonus = getAddPeopleBonus(addAgentList, naturalTimeStr);
            wages.setAddPeopleBonus(addPeopleBonus);
        }
        /*增才奖金*/
        if (!"TA".equals(rank)) {
            Double addTalentBonus = getAddTalentBonus(agent, addAgentList, naturalTimeStr);
            wages.setAddTalentBonus(addTalentBonus);
        }
        /*增组奖金*/
        String[] addGroupRanks = {"AS", "SS", "UM", "AD", "HD"};
        if (Arrays.asList(addGroupRanks).contains(rank)) {
            Double addGroupBonus = getAddGroupBonus(addAgentList, naturalTimeStr);
            wages.setAddGroupBonus(addGroupBonus);
        }
        /*增部奖金*/
        if ("UM".equals(rank) || "AD".equals(rank) || "HD".equals(rank)) {
            Double addPartBonus = getAddPartBonus(agent.getPartId(), naturalTimeStr);
            wages.setAddPartBonus(addPartBonus);
        }

        /*---职务津贴---*/

        /*助理业务主任津贴*/
        if ("TS".equals(rank) && agentMonth.getFyc()>=1200) {
            Double bonusTS = getBonusTS(addAgentList, naturalTimeStr);
            wages.setBonusTS(bonusTS);
        }
        /*销售责任津贴*/
        if ("SD".equals(rank)) {
            Double bonusSD = getBonusSD(agentMonth.getFyc());
            wages.setBonusSD(bonusSD);
        }
        /*总监特别津贴*/
        if ("AD".equals(rank) || "HD".equals(rank)) {
            Double bonusAD = getBonusAD(agent.getPartId(), addAgentList, naturalTimeStr);
            wages.setBonusAD(bonusAD);
        }
        /*高级业务总监职务津贴*/
        if ("HD".equals(rank)) {
            wages.setBonusHD(1200.00);
        }
        /*TODO 年终经营分红需要吗 任职24个月以上才有*/

        /*---团队收益---*/

        /*管理津贴*/
        String[] manageRank = {"AS", "SS", "UM", "AD", "HD"};
        if (Arrays.asList(manageRank).contains(rank)) {
            Double manageBonus = getManageBonus(rank, agent, addAgentList, naturalTimeStr);
            wages.setManageBonus(manageBonus);
        }

        /*直辖组季度奖金*/
        if ("AS".equals(rank) || "SS".equals(rank)) {
            Double groupQuarterlyBonus = getGroupQuarterlyBonus(agent.getGroupId());
            wages.setGroupQuarterlyBonus(groupQuarterlyBonus);
        }

        /*直辖部季度奖金*/
        if (Arrays.asList(quarterlyMonths).contains(naturalMonth) && ("UM".equals(rank) || "AD".equals(rank) || "HD".equals(rank))) {


        }

        return wages;
    }

    /**
     * 获取根据组编号和自然时间直辖组季度奖金
     * @param groupId 组编号
     * @return
     */
    public Double getGroupQuarterlyBonus(Integer groupId, String naturalTimeStr) {
        Double groupQuarterlyBonus = 0.00;
        // 本季直辖组累计FYC
        Double groupTotalFYC = 0.00;
        // 获取组成员列表
        List<Agent> groupAgentList = getGroupList(groupId);
        // 根据组编号获取直辖组本季度FYC
        for (int i = 0; i < groupAgentList.size(); i++) {
            String entryTime = groupAgentList.get(i).getEntryTime();
            Integer workMonth = getWorkMonthByEntryAndNaturalTime(entryTime, naturalTimeStr);



        }
    }

    /**
     * 根据代理人、增员列表和当前时间获取管理津贴
     * @param agent 代理人信息
     * @param addAgentList 增员列表
     * @param naturalTimeStr 自然时间
     * @return
     */
    public Double getManageBonus(String rank, Agent agent, List<Agent> addAgentList, String naturalTimeStr) {
        Double manageBonus = 0.00;
        if ("AS".equals(rank)) {
            // AS 管理津贴 直辖组
            List<Agent> groupAgentList = getGroupList(agent.getGroupId());
            // 获取直辖组FYC和有效人力
            Double groupFYC = 0.00;
            Integer usedAgentCount = 0;
            groupFYC = getGroupFYC(agent.getGroupId(), naturalTimeStr);
            usedAgentCount = getUsedAgentGroupCount(agent.getGroupId(), naturalTimeStr);
            // 根据直辖组FYC和有效人力获取管理津贴数目
            manageBonus = getManageBonusAS(groupFYC, usedAgentCount);
        } else if ("SS".equals(rank)) {
            // SS 管理津贴 直辖组FYC、直辖业务主任直辖组FYC
            List<Agent> groupAgentList = getGroupList(agent.getGroupId());
            // 获取直辖组FYC和有效人力
            Double groupFYC = 0.00;
            Integer usedAgentCount = 0;
            groupFYC = getGroupFYC(agent.getGroupId(), naturalTimeStr);
            usedAgentCount = getUsedAgentGroupCount(agent.getGroupId(), naturalTimeStr);
            // 获取直辖业务主任AS直辖组FYC
            Double addAsFYC = getAddASFYC(addAgentList, naturalTimeStr);
            manageBonus = getManageBonusSS(groupFYC, addAsFYC, usedAgentCount);
        } else if ("UM".equals(rank)) {
            // UM 管理津贴 直辖组FYC、直辖业务主任直辖组FYC、直辖业务主任所辖组织FYC、直辖部有效人力系数
            // 获取直辖组成员
            List<Agent> groupAgentList = getGroupList(agent.getGroupId());
            // 获取直辖部成员
            List<Agent> partAgentList = getPartList(agent.getPartId());
            // 获取直辖组FYC
            Double groupFYC = 0.00;
            groupFYC = getGroupFYC(agent.getGroupId(), naturalTimeStr);
            // 获取直辖部FYC
            Double partFYC = 0.00;
            partFYC = getPartFYC(agent.getPartId(), naturalTimeStr);
            // 获取直辖部有效人力系数
            Integer usedAgentPartCount = 0;
            usedAgentPartCount = getUsedAgentPartCount(agent.getPartId(), naturalTimeStr);
            // 获取直辖业务主任直辖组FYC
            Double addASFYC = getAddASFYC(addAgentList, naturalTimeStr);
            // 获取直辖高级业务主任所辖组织FYC
            Double addSSFYC = getAddSSFYC(addAgentList, naturalTimeStr);
            manageBonus = getManageBonusUM(groupFYC, addASFYC, addSSFYC, partFYC, usedAgentPartCount);
        } else if ("AD".equals(rank) || "HD".equals(rank)) {
            // AD/HD 管理津贴 直辖组FYC、直辖业务主任直辖组FYC、直辖高级业务主任所辖组织FYC、直辖部FYC、直辖业务经理直辖部FYC、直辖部有效人力系数
            // 获取直辖组成员
            List<Agent> groupAgentList = getGroupList(agent.getGroupId());
            // 获取直辖部成员
            List<Agent> partAgentList = getPartList(agent.getPartId());
            // 获取直辖组FYC
            Double groupFYC = 0.00;
            groupFYC = getGroupFYC(agent.getGroupId(), naturalTimeStr);
            // 获取直辖部FYC
            Double partFYC = 0.00;
            partFYC = getPartFYC(agent.getPartId(), naturalTimeStr);
            // 获取直辖部有效人力系数
            Integer usedAgentPartCount = 0;
            usedAgentPartCount = getUsedAgentPartCount(agent.getPartId(), naturalTimeStr);
            // 获取直辖业务主任直辖组FYC
            Double addASFYC = getAddASFYC(addAgentList, naturalTimeStr);
            // 获取直辖高级业务主任所辖组织FYC
            Double addSSFYC = getAddSSFYC(addAgentList, naturalTimeStr);
            // 获取直辖业务经理直辖部FYC
            Double addUMFYC = getAddUMFYC(addAgentList, naturalTimeStr);
            manageBonus = getManageBonusAD(groupFYC, addASFYC, addSSFYC, addUMFYC, partFYC, usedAgentPartCount);
        }
        return manageBonus;
    }

    /**
     * 根据FYC获取AD/HD的管理津贴
     * @param groupFYC 直辖组FYC
     * @param addASFYC 直辖业务主任直辖组FYC
     * @param addSSFYC 直辖高级业务主任所辖组织FYC
     * @param addUMFYC 直辖业务经理直辖部FYC
     * @param partFYC  直辖部FYC
     * @param usedAgentPartCount 直辖部有效人力系数
     * @return
     */
    public Double getManageBonusAD(Double groupFYC, Double addASFYC, Double addSSFYC, Double addUMFYC, Double partFYC, Integer usedAgentPartCount) {
        Double manageBonus = 0.00;
        Double rewardRate = 0.00;
        // 有效人力系数
        Double usedRate = 0.00;
        if (usedAgentPartCount < 12) {
            usedRate = 0.8;
        } else {
            usedRate = 1.0;
        }
        int level = partFYC>=10000&partFYC<50000?1:
                    partFYC>=50000&partFYC<70000?2:
                    partFYC>=70000&partFYC<120000?3:
                    partFYC>=120000&partFYC<240000?4:
                    partFYC>=240000?5:100;
        switch (level) {
            case 1:
                rewardRate = 0.18;
                break;
            case 2:
                rewardRate = 0.25;
                break;
            case 3:
                rewardRate = 0.28;
                break;
            case 4:
                rewardRate = 0.30;
                break;
            case 5:
                rewardRate = 0.31;
                break;
            default:
                break;
        }
        manageBonus = (groupFYC * rewardRate + addASFYC * (rewardRate>0.12?rewardRate-0.12:0) + addSSFYC * (rewardRate > 0.18?rewardRate-0.18:0)) * usedRate + addUMFYC * (rewardRate > 0.23?rewardRate-0.23:0);
        return manageBonus;
    }

    /**
     * 根据FYC获取UM的管理津贴
     * @param groupFYC 直辖组FYC
     * @param addASFYC 直辖业务主任直辖组FYC
     * @param addSSFYC 直辖高级业务主任所辖组织FYC
     * @param partFYC 直辖部FYC
     * @param usedAgentPartCount 直辖部有效人力系数
     * @return
     */
    public Double getManageBonusUM(Double groupFYC, Double addASFYC, Double addSSFYC, Double partFYC, Integer usedAgentPartCount) {
        Double manageBonus = 0.00;
        Double rewardRate = 0.00;
        // 有效人力系数
        Double usedRate = 0.00;
        if (usedAgentPartCount < 12) {
            usedRate = 0.8;
        } else {
            usedRate = 1.0;
        }
        int level = partFYC>=5000&partFYC<24000?1:
                    partFYC>=24000&partFYC<32000?2:
                    partFYC>=32000&partFYC<70000?3:
                    partFYC>=70000&partFYC<120000?4:
                    partFYC>=120000?5:100;
        switch (level) {
            case 1:
                rewardRate = 0.14;
                break;
            case 2:
                rewardRate = 0.23;
                break;
            case 3:
                rewardRate = 0.26;
                break;
            case 4:
                rewardRate = 0.28;
                break;
            case 5:
                rewardRate = 0.29;
                break;
            default:
                break;
        }
        manageBonus = (groupFYC * rewardRate + addASFYC * (rewardRate>0.12?rewardRate-0.12:0) + addSSFYC * (rewardRate > 0.18?rewardRate-0.18:0)) * usedRate;
        return manageBonus;
    }


    /**
     * 根据部编号获取部成员列表
     * @param partId 部编号
     * @return
     */
    public List<Agent> getPartList(Integer partId) {
        // TODO 根据部编号获取部成员列表
    }

    /**
     * 获取直辖部有效人力
     * @param partId 部编号
     * @param naturalTimeStr 自然时间
     * @return
     */
    public Integer getUsedAgentPartCount(Integer partId, String naturalTimeStr) {
        Integer usedAgentPartCount = 0;
        List<Agent> partList = getPartList(partId);
        for (int i = 0; i < partList.size(); i++) {
            String entryTime = partList.get(i).getEntryTime();
            Integer workMonth = getWorkMonthByEntryAndNaturalTime(entryTime, naturalTimeStr);
            Double fyc = partList.get(i).getAgentMonthList().get(workMonth - 1).getFyc();
            if (fyc >= 1200) {
                usedAgentPartCount++;
            }
        }
        return usedAgentPartCount;
    }

    /**
     * 获取直辖高级业务主任所辖组织FYC
     * @param addAgentList 直接增员
     * @param naturalTimeStr 自然时间
     * @return
     */
    public  Double getAddUMFYC(List<Agent> addAgentList, String naturalTimeStr) {
        Double addUMFYCTotal = 0.00;
        for (int i = 0; i < addAgentList.size(); i++) {
            Agent agent = addAgentList.get(i);
            String entryTime = agent.getEntryTime();
            Integer workMonth = getWorkMonthByEntryAndNaturalTime(entryTime, naturalTimeStr);
            String rank = agent.getAgentMonthList().get(workMonth - 1).getRank();
            if ("UM".equals(rank)) {
                // 获取直辖业务经理所辖组织 直辖组+直辖业务主任所辖组
                Double partFYC = getGroupFYC(agent.getGroupId(), naturalTimeStr);
                // 获取增员人列表
                addUMFYCTotal = addUMFYCTotal + partFYC;
            }
        }
        return addUMFYCTotal;
    }

    /**
     * 获取直辖高级业务主任所辖组织FYC
     * @param addAgentList 直接增员
     * @param naturalTimeStr 自然时间
     * @return
     */
    public  Double getAddSSFYC(List<Agent> addAgentList, String naturalTimeStr) {
        Double addSSFYCTotal = 0.00;
        for (int i = 0; i < addAgentList.size(); i++) {
            Agent agent = addAgentList.get(i);
            String entryTime = agent.getEntryTime();
            Integer workMonth = getWorkMonthByEntryAndNaturalTime(entryTime, naturalTimeStr);
            String rank = agent.getAgentMonthList().get(workMonth - 1).getRank();
            if ("SS".equals(rank)) {
                // 获取直辖高级业务主任所辖组织 直辖组+直辖业务主任所辖组
                Double groupFYC = getGroupFYC(agent.getGroupId(), naturalTimeStr);
                // 获取增员人列表
                List<Agent> addAgentList2 = getAddAgentList(agent.getId());
                Double addASFYC = getAddASFYC(addAgentList2, naturalTimeStr);
                addSSFYCTotal = addSSFYCTotal + groupFYC + addASFYC;
            }
        }
        return addSSFYCTotal;
    }

    /**
     * 根据代理人编号获取代理人的增员列表
     * @param id 代理人编号
     * @return
     */
    public List<Agent> getAddAgentList(Integer id) {
        List<Agent> addAgentList = new ArrayList<>();
        // TODO 根据代理人编号获取代理人的增员列表
        return addAgentList;
    }

    /**
     * 根据直辖组FYC、有效人力和直辖业务主任所辖组FYC获取SS职级的管理津贴
     * @param groupFYC 直辖组FYC
     * @param addAsFYC 直辖业务主任所辖组FYC
     * @param usedAgentCount 直辖组有效人力
     * @return
     */
    public Double getManageBonusSS(Double groupFYC, Double addAsFYC, Integer usedAgentCount) {
        Double manageBonus = 0.00;
        // 有效人力系数
        Double usedRate = 0.00;
        if (usedAgentCount < 4) {
            usedRate = 0.8;
        } else {
            usedRate = 1.0;
        }
        int level = groupFYC>=2000&groupFYC<8500?1:
                    groupFYC>=8500&groupFYC<18000?2:
                    groupFYC>=18000&groupFYC<35000?3:
                    groupFYC>=35000&groupFYC<60000?4:
                    groupFYC>=60000?5:100;
        switch (level) {
            case 1:
                manageBonus = groupFYC * usedRate * 0.10;
                break;
            case 2:
                manageBonus = groupFYC * usedRate * 0.18 + addAsFYC * 0.06;
                break;
            case 3:
                manageBonus = groupFYC * usedRate * 0.21 + addAsFYC * 0.09;
                break;
            case 4:
                manageBonus = groupFYC * usedRate * 0.23 + addAsFYC * 0.11;
                break;
            case 5:
                manageBonus = groupFYC * usedRate * 0.24 + addAsFYC * 0.12;
                break;
            default:
                break;
        }
        return manageBonus;
    }

    /**
     * 获取直辖业务主任所辖组的FYC
     * @param addAgentList 增员人列表
     * @param naturalTimeStr 自然时间
     * @return
     */
    public Double getAddASFYC(List<Agent> addAgentList, String naturalTimeStr) {
        Double groupTotalFYC = 0.00;
        for (int i = 0; i < addAgentList.size(); i++) {
            String entryTime = addAgentList.get(i).getEntryTime();
            Integer workMonth = getWorkMonthByEntryAndNaturalTime(entryTime, naturalTimeStr);
            Agent agent = addAgentList.get(i);
            String rank = agent.getAgentMonthList().get(workMonth - 1).getRank();
            if ("AS".equals(rank)) {
                Integer groupId = agent.getGroupId();
                // 获取直辖组FYC
                Double groupFYC = getGroupFYC(groupId, naturalTimeStr);
                groupTotalFYC = groupTotalFYC + groupFYC;
            }
        }
        return groupTotalFYC;
    }

    /**
     * 根据组编号获取当月有效人力系数
     * @param groupId 组编号
     * @param naturalTimeStr 自然时间
     * @return
     */
    public Integer getUsedAgentGroupCount(Integer groupId, String naturalTimeStr) {
        Integer usedAgentGroupCount = 0;
        List<Agent> groupList = getGroupList(groupId);
        for (int i = 0; i < groupList.size(); i++) {
            String entryTime = groupList.get(i).getEntryTime();
            Integer workMonth = getWorkMonthByEntryAndNaturalTime(entryTime, naturalTimeStr);
            Double fyc = groupList.get(i).getAgentMonthList().get(workMonth - 1).getFyc();
            if (fyc >= 1200){
                usedAgentGroupCount++;
            }
        }
        return usedAgentGroupCount;
    }

    /**
     * 获取直辖组FYC
     * @param groupId 组编号
     * @param naturalTimeStr 自然时间
     * @return
     */
    public Double getGroupFYC(Integer groupId, String naturalTimeStr) {
        Double groupFYC = 0.00;
        List<Agent> groupList = getGroupList(groupId);
        for (int i = 0; i <groupList.size() ; i++) {
            String entryTime = groupList.get(i).getEntryTime();
            Integer workMonth = getWorkMonthByEntryAndNaturalTime(entryTime, naturalTimeStr);
            Double fyc = groupList.get(i).getAgentMonthList().get(workMonth - 1).getFyc();
            groupFYC = groupFYC + fyc;
        }
        return groupFYC;
    }

    /**
     * 根据直辖组FYC和有效人力获取管理津贴数目
     * @param groupFYC 直辖组FYC
     * @param usedAgentCount 有效人力
     * @return
     */
    public Double getManageBonusAS(Double groupFYC, Integer usedAgentCount) {
        Double manageBonus = 0.00;
        // 有效人力系数
        Double usedRate = 0.00;
        if (usedAgentCount < 3) {
            usedRate = 0.8;
        } else {
            usedRate = 1.0;
        }
        int level = groupFYC>=1000&groupFYC<3600?1:
                    groupFYC>=3600&groupFYC<8000?2:
                    groupFYC>=8000&groupFYC<18000?3:
                    groupFYC>=18000&groupFYC<35000?4:
                    groupFYC>=35000?5:100;
        switch (level) {
            case 1:
                manageBonus = groupFYC * usedRate * 0.06;
                break;
            case 2:
                manageBonus = groupFYC * usedRate * 0.12;
                break;
            case 3:
                manageBonus = groupFYC * usedRate * 0.15;
                break;
            case 4:
                manageBonus = groupFYC * usedRate * 0.17;
                break;
            case 5:
                manageBonus = groupFYC * usedRate * 0.20;
                break;
            default:
                break;
        }
        return manageBonus;
    }

    /**
     * 根据直辖组编号获取直辖组成员列表
     * @param groupId 组编号
     * @return
     */
    public List<Agent> getGroupList(Integer groupId) {
        // TODO 根据直辖组编号获取直辖组成员列表
        return null;
    }

    /**
     * 根据部编号和自然时间获取增部奖金
     * @param partId 部编号
     * @param naturalTimeStr 自然时间
     * @return
     */
    public Double getAddPartBonus(Integer partId, String naturalTimeStr) {
        Double addPartBonus = 0.00;
        List<Agent> partAgentList = getPartAgentListByPartId(partId);
        for (int i = 0; i < partAgentList.size(); i++) {
            String entryTime = partAgentList.get(i).getEntryTime();
            Integer workMonth = getWorkMonthByEntryAndNaturalTime(entryTime, naturalTimeStr);
            String rank = partAgentList.get(i).getAgentMonthList().get(workMonth - 1).getRank();
            if ("UM".equals(rank)) {
                // 判断是否首次达到UM
                Boolean firstUMFlag = true;
                List<AgentMonth> agentMonthList = partAgentList.get(i).getAgentMonthList();
                for (int j = 0; j < workMonth; j++) {
                    if ("UM".equals(agentMonthList.get(j - 1).getRank())) {
                        firstUMFlag = false;
                    }
                }
                if (firstUMFlag) {
                    addPartBonus = addPartBonus + 10000.00;
                }
            }
        }
        return addPartBonus;
    }

    public List<Agent> getPartAgentListByPartId(Integer partId) {
        // TODO 根据部编号获取部成员列表 ----- 职级达到UM的
        return null;
    }

    /**
     * 根据增员列表和自然时间获取增组奖金
     * @param addAgentList 直接增员列表
     * @param naturalTimeStr 自然时间
     * @return
     */
    public Double getAddGroupBonus(List<Agent> addAgentList, String naturalTimeStr) {
        Double addGroupBonus = 0.00;
        for (int i = 0; i < addAgentList.size(); i++) {
            Agent addAgent = addAgentList.get(i);
            List<AgentMonth> agentMonthList = addAgent.getAgentMonthList();
            String entryTime = addAgent.getEntryTime();
            Integer workMonth = getWorkMonthByEntryAndNaturalTime(entryTime, naturalTimeStr);
            String rank = agentMonthList.get(workMonth - 1).getRank();
            if ("AS".equals(rank)) {
                Boolean firstASFlag = true;
                // 判断是否首次晋升
                for (int j = 0; j < workMonth; j++) {
                    String oldRank = agentMonthList.get(j - 1).getRank();
                    if ("AS".equals(oldRank)) {
                        firstASFlag = false;
                    }
                }
                if (firstASFlag) {
                    addGroupBonus = addGroupBonus + 5000.00;
                }
            }
        }
        return addGroupBonus;
    }

    /**
     * 根据代理人信息，直接增员列表和自然时间获取增才奖金
     * @param addAgentList 直接增员列表
     * @param naturalTimeStr 自然时间
     * @return
     */
    public Double getAddTalentBonus(Agent agent, List<Agent> addAgentList, String naturalTimeStr) {
        Double addTalentBonus = 0.00;
        // 代理人入职时间
        String agentEntryTime = agent.getEntryTime();
        // 代理人逐月信息
        List<AgentMonth> agentMonthList = agent.getAgentMonthList();
        for (int i = 0; i < addAgentList.size(); i++) {
            Agent addAgent = addAgentList.get(i);
            String entryTime = addAgent.getEntryTime();
            Integer workMonth = getWorkMonthByEntryAndNaturalTime(entryTime, naturalTimeStr);
            if (3 <= workMonth && workMonth <= 6) {
                List<AgentMonth> addAgentMonthList = addAgent.getAgentMonthList();
                // 代理人本月工作月小于6个月，判断最近3个月是否均FYC>=3000
                Boolean talentFlag = true;
                for (int j = 0; j < 3; j++) {
                    Double addFYC = addAgentMonthList.get(workMonth - 1 - i).getFyc();
                    if (addFYC < 3000) {
                        talentFlag = false;
                    }
                }
                if (talentFlag) {
                    Integer reachMonths = 0;
                    // 判断是否初次达成入围泰1星标准
                    for (int j = 0; j < workMonth; j++) {
                        Double addFYC = addAgentMonthList.get(j).getFyc();
                        if (addFYC >= 3000) {
                            reachMonths++;
                        }
                    }
                    if (reachMonths == 3) {
                        // 根据增员人入职时间时代理人的职级定档
                        Double addTalentBonusOne = getAddTalentLevel(entryTime, agentEntryTime, agentMonthList);
                        addTalentBonus = addTalentBonus + addTalentBonusOne;
                    }
                }
            }
        }
        return addTalentBonus;
    }

    /**
     * 根据增员人入职时代理人的职级获取增才奖金的数目
     * @param entryTime 直接增员入职时间
     * @param agentEntryTime 增员人入职时间
     * @param agentMonthList 增员人逐月信息
     * @return
     */
    public Double getAddTalentLevel(String entryTime, String agentEntryTime, List<AgentMonth> agentMonthList) {
        Integer workMonth = getWorkMonthByEntryAndNaturalTime(agentEntryTime, entryTime);
        String rank = agentMonthList.get(workMonth - 1).getRank();
        String[] firstLevelRanks = {"SA", "TS", "SE", "SM", "SD"};
        String[] secondLevelRanks = {"AS", "SS", "UM", "AD", "HD"};
        if (Arrays.asList(firstLevelRanks).contains(rank)) {
            return 2500.00;
        } else if (Arrays.asList(secondLevelRanks).contains(rank)) {
            return 4000.00;
        } else {
            return 0.00;
        }
    }

    /**
     * 根据直接增员列表和自然时间获取增员奖
     * @param addAgentList 直接增员列表
     * @param naturalTimeStr 自然时间
     * @return
     */
    public Double getAddPeopleBonus(List<Agent> addAgentList, String naturalTimeStr) {
        Double addPeopleBonus = 0.00;
        for (int i = 0; i < addAgentList.size(); i++) {
            String entryTime = addAgentList.get(i).getEntryTime();
            Integer workMonth = getWorkMonthByEntryAndNaturalTime(entryTime, naturalTimeStr);
            if (workMonth != null && workMonth <= 12) {
                // 直接增员前12个月给增员人发放增员奖
                Double addAgentFYC = addAgentList.get(i).getAgentMonthList().get(workMonth - 1).getFyc();
                if (addAgentFYC < 1000) {
                    addPeopleBonus = addPeopleBonus + addAgentFYC * 0.05;
                } else {
                    addPeopleBonus = addPeopleBonus + addAgentFYC * 0.10;
                }
            }
        }
        return addPeopleBonus;
    }

    /**
     * 根据直辖部编号和增员人列表、自然时间获取总监特别津贴
     * @param partId 直辖部编号
     * @param addAgentList 增员人列表
     * @param naturalTimeStr 自然时间
     * @return
     */
    public Double getBonusAD(Integer partId, List<Agent> addAgentList, String naturalTimeStr) {
        Double bonusAD = 0.00;
        // 获取直辖部FYC
        Double partFYC = getPartFYCByPartId(partId, naturalTimeStr);
        // 获取直辖业务经理所辖部FYC
        Double addPartFYC = getAddPartFYC(addAgentList, naturalTimeStr);
        Double totalPartFYC = partFYC + addPartFYC;
        int level = totalPartFYC >=30000&totalPartFYC < 45000 ? 1:
                    totalPartFYC >= 45000&totalPartFYC < 60000 ? 2:
                    totalPartFYC >= 60000&totalPartFYC < 100000 ? 3:
                    totalPartFYC >= 100000 ? 4 : 100;
        switch (level) {
            case 1:
                bonusAD = totalPartFYC * 0.01;
                break;
            case 2:
                bonusAD = totalPartFYC * 0.02;
                break;
            case 3:
                bonusAD = totalPartFYC * 0.03;
                break;
            case 4:
                bonusAD = totalPartFYC * 0.04;
                break;
            default:
                break;
        }
        return bonusAD;
    }

    /**
     * 根据增员人列表和自然时间获取本月直辖业务经理直辖部的总FYC
     * @param addAgentList 增员人列表
     * @param naturalTimeStr 自然月
     * @return
     */
    public Double getAddPartFYC(List<Agent> addAgentList, String naturalTimeStr) {
        Double addPartFYC = 0.00;
        // 获取直辖业务经理(UM)所辖部编号
        for (int i = 0; i < addAgentList.size(); i++) {
            String entryTimeStr = addAgentList.get(i).getEntryTime();
            Integer workMonth = getWorkMonthByEntryAndNaturalTime(entryTimeStr, naturalTimeStr);
            if (workMonth != null) {
                String rank = addAgentList.get(i).getAgentMonthList().get(workMonth - 1).getRank();
                if ("UM".equals(rank)) {
                    Integer partId = addAgentList.get(i).getPartId();
                    addPartFYC = addPartFYC + getPartFYCByPartId(partId, naturalTimeStr);
                }
            }
        }
        return addPartFYC;
    }

    /**
     * 根据部编号获取部本月FYC
     * @param partId 部编号
     * @return
     */
    public Double getPartFYC(Integer partId, String naturalTimeStr) {
        Double partFYC = 0.00;
        // TODO 根据部编号获取部本月FYC
        return partFYC;
    }

    /**
     * 根据当月fyc获取销售责任津贴
     * @param FYC 当月fyc
     * @return
     */
    public Double getBonusSD(Double FYC) {
        if (FYC >= 5000 && FYC < 8000) {
            return 250.00;
        } else if (FYC >= 8000) {
            return 500.00;
        } else {
            return 0.00;
        }
    }

    /**
     * 根据增员人员信息和自然时间获取助理业务主任津贴
     * @param addAgentList
     * @param naturalTimeStr
     * @return
     */
    public Double getBonusTS(List<Agent> addAgentList, String naturalTimeStr) {
        // 是否存在增员人当月FYC大于1200,存在发放500元助理业务主任津贴
        Boolean flag = false;
        for (int i = 0; i < addAgentList.size(); i++) {
            Agent addAgent = addAgentList.get(i);
            String addEntryTime = addAgent.getEntryTime();
            Integer addWorkMonth = getWorkMonthByEntryAndNaturalTime(addEntryTime, naturalTimeStr);
            if (addWorkMonth != null) {
                Double addFYC = addAgent.getAgentMonthList().get(addWorkMonth - 1).getFyc();
                if (addFYC >= 1200) {
                    flag = true;
                    break;
                }
            }
        }
        return flag?500.00:0.00;
    }

    /**
     * 根据入职时间和自然时间获取当前工作月份
     * @param entryTime  入职时间
     * @param naturalTime 自然时间
     * @return
     */
    public Integer getWorkMonthByEntryAndNaturalTime(String entryTime, String naturalTime) {
        Integer addWorkMonth = 0;
        // 获取入职月份
        Integer entryMonth = Integer.valueOf(entryTime.substring(entryTime.indexOf("-")));
        // 获取入职年份
        Integer entryYear = Integer.valueOf(entryTime.substring(0,entryTime.indexOf("-")));
        // 获取自然月份
        Integer naturalMonth = Integer.valueOf(naturalTime.substring(naturalTime.indexOf("-")));
        // 获取自然年份
        Integer naturalYear = Integer.valueOf(naturalTime.substring(0,naturalTime.indexOf("-")));
        if (naturalYear < entryYear || (naturalYear == entryYear && naturalMonth < entryMonth)) {
            return null;
        } else {
            addWorkMonth = (naturalYear - entryYear)*12 + (naturalMonth - entryMonth) + 1;
        }
        return addWorkMonth;
    }

    /**
     * 根据代理人逐月信息计算代理人年终奖
     * @param agentMonthList 代理人逐月信息
     * @param workMonth 工作月份
     * @return
     */
    public Double getAnnualBonus(List<AgentMonth> agentMonthList, Integer workMonth) {
        Double annualBonus = 0.00;
        // 本年累计FYC
        Double totalFYC = 0.00;
        int month = 0;
        for (int i = 0; i < (workMonth>12?12:workMonth); i++) {
            // TA职级的FYC不计算在内
            if (!"TA".equals(agentMonthList.get(workMonth - 1 - i).getRank())) {
                totalFYC = totalFYC + agentMonthList.get(workMonth - 1 - i).getFyc();
                month++;
            }
        }

        // 获取月均FYC
        Double averageFYC = totalFYC / month;
        // 根据月均FYC定档
        int level = 0;

        switch (agentMonthList.get(workMonth-1).getRank()) {
            case "SA": case "TS": case "AS": case "SS": case "UM": case "AD": case "HD":
                level = averageFYC>=800&averageFYC<1500?1:
                        averageFYC>=1500&averageFYC<3000?2:
                        averageFYC>=3000&averageFYC<5000?3:
                        averageFYC>=5000?4 : 100;
                switch (level) {
                    case 1:
                        annualBonus = totalFYC * 0.025;
                        break;
                    case 2:
                        annualBonus = totalFYC * 0.045;
                        break;
                    case 3:
                        annualBonus = totalFYC * 0.07;
                        break;
                    case 4:
                        annualBonus = totalFYC * 0.09;
                        break;
                    default:
                        break;
                }
                break;
            case "SE":
                level = averageFYC>=1000&averageFYC<2000?1:
                        averageFYC>=2000&averageFYC<4000?2:
                        averageFYC>=4000&averageFYC<6000?3:
                        averageFYC>=6000?4 : 100;
                switch (level) {
                    case 1:
                        annualBonus = totalFYC * 0.035;
                        break;
                    case 2:
                        annualBonus = totalFYC * 0.055;
                        break;
                    case 3:
                        annualBonus = totalFYC * 0.08;
                        break;
                    case 4:
                        annualBonus = totalFYC * 0.1;
                        break;
                    default:
                        break;
                }
                break;
            case "SM":
                level = averageFYC>=2000&averageFYC<3000?1:
                        averageFYC>=3000&averageFYC<5000?2:
                        averageFYC>=5000&averageFYC<8000?3:
                        averageFYC>=8000?4 : 100;
                switch (level) {
                    case 1:
                        annualBonus = totalFYC * 0.045;
                        break;
                    case 2:
                        annualBonus = totalFYC * 0.07;
                        break;
                    case 3:
                        annualBonus = totalFYC * 0.09;
                        break;
                    case 4:
                        annualBonus = totalFYC * 0.11;
                        break;
                    default:
                        break;
                }
                break;
            case "SD":
                level = averageFYC>=3000&averageFYC<4000?1:
                        averageFYC>=4000&averageFYC<5000?2:
                        averageFYC>=5000&averageFYC<10000?3:
                        averageFYC>=10000?4 : 100;
                switch (level) {
                    case 1:
                        annualBonus = totalFYC * 0.07;
                        break;
                    case 2:
                        annualBonus = totalFYC * 0.1;
                        break;
                    case 3:
                        annualBonus = totalFYC * 0.12;
                        break;
                    case 4:
                        annualBonus = totalFYC * 0.14;
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return annualBonus;
    }

    /**
     * 根据代理人逐月信息计算代理人季度奖
     * @param agentMonthList 代理人逐月信息
     * @param workMonth 工作月份
     * @return
     */
    public Double getQuarterlyBonus(List<AgentMonth> agentMonthList, Integer workMonth) {
        Double quarterlyBonus = 0.00;
        // 本季累计FYC
        Double totalFYC = 0.00;
        for (int i = 0; i < (workMonth>3?3:workMonth); i++) {
            String rank = agentMonthList.get(workMonth - 1 - i).getRank();
            // 仅计算任职 SA、TS、SE、SM、SD期间的FYC
            String[] quarterlyRanks = {"SA", "TS", "SE", "SM", "SD"};
            if (Arrays.asList(quarterlyRanks).contains(rank)) {
                totalFYC = totalFYC + agentMonthList.get(workMonth - 1 - i).getFyc();
            }
        }

        // 根据季度累计FYC定档
        int level = totalFYC>=1800&totalFYC<4500?1:
                    totalFYC>=4500&totalFYC<9000?2:
                    totalFYC>=9000&totalFYC<15000?3:
                    totalFYC>=15000?4: 100;

        // 每个职级的不同档季度奖
        switch (agentMonthList.get(workMonth-1).getRank()) {
            case "SA":case "TS":
                switch (level) {
                    case 1:
                        quarterlyBonus = totalFYC * 0.06;
                        break;
                    case 2:
                        quarterlyBonus = totalFYC * 0.09;
                        break;
                    case 3:
                        quarterlyBonus = totalFYC * 0.12;
                        break;
                    case 4:
                        quarterlyBonus = totalFYC * 0.14;
                        break;
                    default:
                        break;
                }
                break;
            case "SE":
                switch (level) {
                    case 1:
                        quarterlyBonus = totalFYC * 0.08;
                        break;
                    case 2:
                        quarterlyBonus = totalFYC * 0.11;
                        break;
                    case 3:
                        quarterlyBonus = totalFYC * 0.13;
                        break;
                    case 4:
                        quarterlyBonus = totalFYC * 0.15;
                        break;
                    default:
                        break;
                }
                break;
            case "SM":
                switch (level) {
                    case 1:
                        quarterlyBonus = totalFYC * 0.10;
                        break;
                    case 2:
                        quarterlyBonus = totalFYC * 0.12;
                        break;
                    case 3:
                        quarterlyBonus = totalFYC * 0.14;
                        break;
                    case 4:
                        quarterlyBonus = totalFYC * 0.17;
                        break;
                    default:
                        break;
                }
                break;
            case "SD":
                switch (level) {
                    case 1:
                        quarterlyBonus = totalFYC * 0.12;
                        break;
                    case 2:
                        quarterlyBonus = totalFYC * 0.14;
                        break;
                    case 3:
                        quarterlyBonus = totalFYC * 0.17;
                        break;
                    case 4:
                        quarterlyBonus = totalFYC * 0.20;
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return quarterlyBonus;
    }

    /**
     * 根据代理人逐月信息和工作月份获取转正奖金
     * @param agentMonthList 逐月信息
     * @param workMonth 工作月份
     * @return
     */
    public Double getPositiveBonus(List<AgentMonth> agentMonthList, Integer workMonth) {
        Double totalFYC = 0.00;
        for (int i = 0; i < (workMonth<3?workMonth:3); i++) {
            AgentMonth agentMonth = agentMonthList.get(workMonth - i - 2);
            if ("TA".equals(agentMonth.getRank())) {
                totalFYC = totalFYC + agentMonth.getFyc();
            }
        }
        return totalFYC*0.25;

    }

    /**
     * 根据职级计算应得fyc，TA(见习业务代表)为80%
     * @param fyc 首佣
     * @param rank 职级
     * @return
     */
    public Double getFyc(Double fyc, String rank) {
        if ("TA".equals(rank)) {
            return fyc*0.8;
        } else {
            return fyc;
        }
    }
}
