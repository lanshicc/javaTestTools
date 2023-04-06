package com.zhaobh.test.springboottesttool.controller;

import com.zhaobh.test.springboottesttool.entity.Group;
import com.zhaobh.test.springboottesttool.result.CommonResult;
import com.zhaobh.test.springboottesttool.service.GroupService;
import com.zhaobh.test.springboottesttool.vo.GroupVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1.0")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/addGroup")
    public CommonResult addGroup(@RequestBody GroupVo groupVo) {
        int result = groupService.addGroup(groupVo);
        if (result > 0) {
            return CommonResult.success("添加组成功");
        } else {
            return CommonResult.fail("添加组失败");
        }
    }

    @GetMapping("/getGroups/{partId}")
    public CommonResult getGroups(@PathVariable("partId") Integer partId) {
        List<Group> groupsList = groupService.getGroupsByPartId(partId);
        return CommonResult.success(groupsList);
    }
}
