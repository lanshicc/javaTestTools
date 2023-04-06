package com.zhaobh.test.springboottesttool.service;

import com.zhaobh.test.springboottesttool.entity.Agent;
import com.zhaobh.test.springboottesttool.vo.AgentVo;

public interface AgentService {
    public int addAgent(AgentVo agentVo);

    public int removeAgent(Integer agentId);

    public Agent getAgent(Integer agentId);

    public int editAgent(AgentVo agentVo);
}
