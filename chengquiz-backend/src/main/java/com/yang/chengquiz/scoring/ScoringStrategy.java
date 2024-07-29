package com.yang.chengquiz.scoring;

import com.yang.chengquiz.model.entity.App;
import com.yang.chengquiz.model.entity.UserAnswer;

import java.util.List;

/**
 * 评分策略
 */
public interface ScoringStrategy {

    /**
     * 执行评分策略
     * @param choices 用户选择的选项
     * @param app 应用
     * @return 评分结果
     * @throws Exception 异常
     */
    UserAnswer doScore(List<String> choices, App app) throws Exception;
}
