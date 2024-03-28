package com.scu.gkvr_system_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scu.gkvr_system_backend.mapper.ScLiScoreMapper;
import com.scu.gkvr_system_backend.mapper.ScoreRankMapper;
import com.scu.gkvr_system_backend.pojo.ScLiScore;
import com.scu.gkvr_system_backend.pojo.ScoreRank;
import com.scu.gkvr_system_backend.service.ScoreRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Liyang
 * @description 针对表【score_rank】的数据库操作Service实现
 * @createDate 2024-03-28 10:28:30
 */
@Service
public class ScoreRankServiceImpl extends ServiceImpl<ScoreRankMapper, ScoreRank>
        implements ScoreRankService {

    private final Map<String, Object> result = new HashMap<>();

    @Autowired
    ScLiScoreMapper scLiScoreMapper;

    @Override
    public Map<String, Object> getRank(int score) {
        LambdaQueryWrapper<ScoreRank> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScoreRank::getScore, score);
        ScoreRank scoreRank = baseMapper.selectOne(wrapper);
        result.put("scoreRank", scoreRank);
        return result;
    }

    @Override
    public Map<String, Object> getReco(int page, int score, String risk, String provinceName, String schoolClass) {
        LambdaQueryWrapper<ScoreRank> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScoreRank::getScore, score);
        ScoreRank scoreRank = baseMapper.selectOne(wrapper);
        Integer rank = scoreRank.getRank();//排名
        int UpperRank = (int) (rank * 0.6);
        int LowerRank = (int) (rank * 1.5);
        switch (risk) {
            //根据风险等级确定排名范围，阈值为高考填报业界经验值
            case "可冲击":
                UpperRank = (int) (rank * 0.6);
                LowerRank = (int) (rank * 0.83);
                break;
            case "较稳妥":
                UpperRank = (int) (rank * 0.83);
                LowerRank = (int) (rank * 0.9);
                break;
            case "可保底":
                UpperRank = (int) (rank * 0.9);
                LowerRank = (int) (rank * 1.5);
                break;
            default:
                break;
        }
        List<ScLiScore> list = scLiScoreMapper.selectRank(UpperRank, LowerRank);
        List<Integer> averageScores = new ArrayList<>();//平均分作为预测投档线
        List<Integer> upLineRateList = new ArrayList<>();
        for (ScLiScore scLiScore : list) {
            double averageScore = (scLiScore.getScore2020() + scLiScore.getScore2021() + scLiScore.getScore2022()) / 3.0;
            double rankRate = ((scLiScore.getRank2020() + scLiScore.getRank2021() + scLiScore.getRank2022()) / 3.0) / rank;
            int UpLineRate = (int) (rankRate * 50) >= 100 ? 99: (int) (rankRate * 50);
            upLineRateList.add(UpLineRate);
            averageScores.add((int) averageScore);
        }
        result.put("upLineRateList",upLineRateList);
        result.put("averageScores", averageScores);
        result.put("scoreRank", scoreRank);
        result.put("list", list);
        return result;
    }
}




