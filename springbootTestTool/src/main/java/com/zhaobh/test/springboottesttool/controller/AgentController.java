package com.zhaobh.test.springboottesttool.controller;

import com.zhaobh.test.springboottesttool.entity.Agent;
import com.zhaobh.test.springboottesttool.result.CommonResult;
import com.zhaobh.test.springboottesttool.service.AgentService;
import com.zhaobh.test.springboottesttool.vo.AgentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1.0")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @PostMapping("/addAgent")
    public CommonResult addAgent(@RequestBody AgentVo agentVo) {
        // 创建代理人
        int result = agentService.addAgent(agentVo);
        if (result > 0) {
            return CommonResult.success("添加代理人成功");
        } else {
            return CommonResult.fail("添加代理人失败");
        }
    }

    @DeleteMapping("/removeAgent/{agentId}")
    public CommonResult removeAgent(@PathVariable("agentId") Integer agentId) {
        // 删除代理人
        int result = agentService.removeAgent(agentId);
        if (result > 0) {
            return CommonResult.success("删除代理人成功");
        } else {
            return CommonResult.fail("删除代理人失败");
        }
    }

    @GetMapping("/getAgent/{agentId}")
    public CommonResult getAgent(@PathVariable("agentId") Integer agentId) {
        // 获取代理人信息
        Agent agent = agentService.getAgent(agentId);

        // TODO 代理人逐月信息

        return CommonResult.success(agent);

    }

    @PatchMapping("/editAgent")
    public CommonResult editAgent(@RequestBody AgentVo agentVo) {
        // 修改代理人基本信息
        int result = agentService.editAgent(agentVo);
        if (result > 0) {
            return CommonResult.success("修改代理人基本信息成功");
        } else {
            return CommonResult.fail("修改代理人基本信息失败");
        }
    }





}
