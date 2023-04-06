package com.zhaobh.test.springboottesttool.service;


import com.zhaobh.test.springboottesttool.entity.Group;
import com.zhaobh.test.springboottesttool.vo.GroupVo;

import java.util.List;

public interface GroupService {
    public int addGroup(GroupVo groupVo);

    public List<Group> getGroupsByPartId(Integer partId);
}
