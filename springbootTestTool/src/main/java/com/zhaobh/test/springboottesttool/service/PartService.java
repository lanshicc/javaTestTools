package com.zhaobh.test.springboottesttool.service;

import com.zhaobh.test.springboottesttool.entity.Part;
import com.zhaobh.test.springboottesttool.vo.PartVo;

import java.util.List;

public interface PartService {
    public List<Part> getParts();

    public int addPart(PartVo partVo);
}
