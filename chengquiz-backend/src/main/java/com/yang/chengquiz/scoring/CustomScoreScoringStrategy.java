package com.yang.chengquiz.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yang.chengquiz.model.dto.question.QuestionContentDTO;
import com.yang.chengquiz.model.entity.App;
import com.yang.chengquiz.model.entity.Question;
import com.yang.chengquiz.model.entity.ScoringResult;
import com.yang.chengquiz.model.entity.UserAnswer;
import com.yang.chengquiz.model.vo.QuestionVO;
import com.yang.chengquiz.service.QuestionService;
import com.yang.chengquiz.service.ScoringResultService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 自定义评分类应用评分策略
 */
@ScoringStrategyConfig(appType = 0,scoringStrategy = 0)
public class CustomScoreScoringStrategy implements ScoringStrategy {

    @Resource
    private QuestionService questionService;

    @Resource
    private ScoringResultService scoringResultService;
    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        //根据id查询题目与其结果信息（分数按降序排列）
        Long id=app.getId();
        Question question = questionService.getOne(
                Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, id)
        );
        List<ScoringResult> scoringResultList = scoringResultService.list(
                Wrappers.lambdaQuery(ScoringResult.class)
                        .eq(ScoringResult::getAppId, id)
                        .orderByDesc(ScoringResult::getResultScoreRange)
        );
        //根据用户选择的答案与题目结果信息比对，计算得分
        QuestionVO questionVO = QuestionVO.objToVo(question);
        List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();
        int totalScore=0;
        for (int i = 0; i < choices.size(); i++) {
            String choice = choices.get(i); // 用户的选择
            QuestionContentDTO questionItem = questionContent.get(i); // 当前的问题内容

            // 找到用户选择的选项
            QuestionContentDTO.Option selectedOption = questionItem.getOptions().stream()
                    .filter(option -> option.getKey().equals(choice))
                    .findFirst()
                    .orElse(null);

            // 更新相应属性的分数
            if (selectedOption != null) {
                totalScore+= Optional.ofNullable(selectedOption.getScore()).orElse(0);
            }
        }
        //遍历得分结果，找到第一个用户得分大于得分结果范围的结果
        ScoringResult bestResult = null;
        for (ScoringResult result : scoringResultList) {
            if (totalScore >= result.getResultScoreRange()) {
                bestResult = result;
                break;
            }
        }
        //返回得分最高的用户答案，填充答案对象属性
        if (bestResult != null) {
            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setAppId(id);
            userAnswer.setAppType(app.getAppType());
            userAnswer.setScoringStrategy(app.getScoringStrategy());
            userAnswer.setChoices(JSONUtil.toJsonStr(choices));
            userAnswer.setResultId(bestResult.getId());
            userAnswer.setResultName(bestResult.getResultName());
            userAnswer.setResultDesc(bestResult.getResultDesc());
            userAnswer.setResultPicture(bestResult.getResultPicture());
            userAnswer.setResultScore(totalScore);
            return userAnswer;
        }
        return null;
    }
}
