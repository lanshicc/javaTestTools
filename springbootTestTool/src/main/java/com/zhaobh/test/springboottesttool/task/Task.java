package com.zhaobh.test.springboottesttool.task;

import com.alibaba.fastjson.JSON;
import com.zhaobh.test.springboottesttool.entity.Agent;
import com.zhaobh.test.springboottesttool.entity.AgentMonth;
import com.zhaobh.test.springboottesttool.utils.AgentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class Task implements ApplicationRunner {

    @Autowired
    private AgentUtil agentUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("执行了");

        // 添加初代代理人
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        // 入职月份
        String entryTimeStr = "2022-11";
        Date entryTimeDate = dateFormat.parse(entryTimeStr);
        Agent agent = agentUtil.crearteAgent(1, "张三", entryTimeStr, 0,false, 0.20);

        log.info(JSON.toJSONString(agent));

        // 输入代理人逐月的FYC

    }

}
