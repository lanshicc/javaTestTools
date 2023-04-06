package com.zhaobh.test.springboottesttool.service.impl;

import com.zhaobh.test.springboottesttool.entity.Group;
import com.zhaobh.test.springboottesttool.mapper.GroupMapper;
import com.zhaobh.test.springboottesttool.service.GroupService;
import com.zhaobh.test.springboottesttool.vo.GroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Override
    public int addGroup(GroupVo groupVo) {
        return groupMapper.addGroup(groupVo);
    }

    @Override
    public List<Group> getGroupsByPartId(Integer partId) {
        return groupMapper.getGroupsByPartId(partId);
    }
}
