package com.zhaobh.test.springboottesttool.service.impl;

import com.zhaobh.test.springboottesttool.entity.Part;
import com.zhaobh.test.springboottesttool.mapper.PartMapper;
import com.zhaobh.test.springboottesttool.service.PartService;
import com.zhaobh.test.springboottesttool.vo.PartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartServiceImpl implements PartService {

    @Autowired
    private PartMapper partMapper;

    @Override
    public int addPart(PartVo partVo) {
        return partMapper.addPart(partVo);
    }

    @Override
    public List<Part> getParts() {
        return partMapper.getParts();
    }
}
