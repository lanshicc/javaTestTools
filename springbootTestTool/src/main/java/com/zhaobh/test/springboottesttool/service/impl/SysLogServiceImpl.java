package com.zhaobh.test.springboottesttool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhaobh.test.springboottesttool.entity.SysLogEntity;
import com.zhaobh.test.springboottesttool.mapper.SysLogMapper;
import com.zhaobh.test.springboottesttool.service.SysLogService;
import org.springframework.stereotype.Service;

@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLogEntity> implements SysLogService {
}
