package com.zhaobh.test.springboottesttool.controller;

import com.zhaobh.test.springboottesttool.entity.Part;
import com.zhaobh.test.springboottesttool.result.CommonResult;
import com.zhaobh.test.springboottesttool.service.PartService;
import com.zhaobh.test.springboottesttool.vo.PartVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1.0")
public class PartController {

    @Autowired
    private PartService partService;

    @PostMapping("/addPart")
    public CommonResult addPart(@RequestBody PartVo partVo) {
        int result = partService.addPart(partVo);
        if (result > 0) {
            return CommonResult.success("添加部成功");
        } else {
            return CommonResult.fail("添加部失败");
        }
    }

    @GetMapping("/getParts")
    public CommonResult getPart() {
        // 获取所有部名称
        List<Part> partList = partService.getParts();
        return CommonResult.success(partList);
    }


}
