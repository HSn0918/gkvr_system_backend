package com.scu.gkvr_system_backend.controller;

import com.scu.gkvr_system_backend.service.ScoreRankService;
import com.scu.gkvr_system_backend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author liyang
 * @since 2023-07-14
 */
@RestController
@RequestMapping("/scoreRank")
@CrossOrigin
public class ScoreRankController {

    @Autowired
    private ScoreRankService scoreRankService;

    @GetMapping("/getRank")
    public Result<Map<String, Object>> getRank(@RequestParam int score) {
        Map<String, Object> data = scoreRankService.getRank(score);
        if (data != null) {
            return Result.success("获取成功", data);
        }
        return Result.fail(201, "获取失败");
    }

    @GetMapping("/getReco")
    public Result<Map<String, Object>> getReco(@RequestParam int page, @RequestParam int score,
                                               @RequestParam String risk, @RequestParam String provinceName,
                                               @RequestParam String schoolClass) {
        Map<String, Object> data = scoreRankService.getReco(page, score, risk, provinceName, schoolClass);
        if (data != null) {
            return Result.success("获取成功", data);
        }
        return Result.fail(201, "获取失败");
    }
}
