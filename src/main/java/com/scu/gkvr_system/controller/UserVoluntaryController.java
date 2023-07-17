package com.scu.gkvr_system.controller;

import com.scu.common.vo.Result;
import com.scu.gkvr_system.service.IUserVoluntaryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zzc
 * @since 2023-07-17
 */
@RestController
@RequestMapping("/userVoluntary")
@CrossOrigin
public class UserVoluntaryController {

    @Resource
    private IUserVoluntaryService userVoluntaryService;

    @PostMapping("/addVoluntary")
    public Result<Map<String,Object>> addVoluntary(@RequestBody String userId, @RequestBody String schoolId, @RequestBody String majorId){
        if (userVoluntaryService.addVoluntary(userId, schoolId, majorId)) {
            return Result.success("添加成功");
        }
        return Result.fail(20001, "添加失败");
    }


}