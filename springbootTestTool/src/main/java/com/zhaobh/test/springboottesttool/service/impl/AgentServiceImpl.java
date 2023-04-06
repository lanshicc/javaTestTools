package com.zhaobh.test.springboottesttool.service.impl;

import com.zhaobh.test.springboottesttool.entity.Agent;
import com.zhaobh.test.springboottesttool.mapper.AgentMapper;
import com.zhaobh.test.springboottesttool.service.AgentService;
import com.zhaobh.test.springboottesttool.vo.AgentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgentServiceImpl implements AgentService {


    @Autowired
    private AgentMapper agentMapper;

    @Override
    public int addAgent(AgentVo agentVo) {
        return agentMapper.addAgent(agentVo);
    }

    @Override
    public int removeAgent(Integer agentId) {
        return agentMapper.removeAgent(agentId);
    }

    @Override
    public Agent getAgent(Integer agentId) {
        return agentMapper.getAgent(agentId);
    }

    @Override
    public int editAgent(AgentVo agentVo) {
        return agentMapper.editAgent(agentVo);
    }
}
