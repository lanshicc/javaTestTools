package com.zhaobh.test.springboottesttool.utils;

import com.zhaobh.test.springboottesttool.entity.Agent;
import com.zhaobh.test.springboottesttool.entity.AgentMonth;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class AgentUtil {

    /**
     * 创建代理人方法：添加代理人以及初始化代理人18个月的信息
     * @param name 代理人姓名
     * @param entryTime 入职时间
     * @param isNewS 新人类型
     * @param addWagesRate 加佣比例
     * @return
     */
    public Agent crearteAgent(Integer id,String name, String entryTime, Integer masterId,Boolean isNewS, Double addWagesRate) {
        Agent agent = new Agent();
        agent.setId(id);
        agent.setName(name);
        agent.setEntryTime(entryTime);
        agent.setMasterId(masterId);
        agent.setIsNewS(isNewS);
        agent.setAddWagesRate(addWagesRate);

        List<AgentMonth> agentMonthList = new ArrayList<>();

        // 初始化18个月的数据
        for (int i = 0; i < 18; i++) {
            AgentMonth agentMonth = new AgentMonth();
            agentMonth.setMonth(i + 1);
            agentMonthList.add(agentMonth);
        }
        agent.setAgentMonthList(agentMonthList);

        return agent;
    }
}
