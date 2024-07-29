package com.yang.chengquiz.scoring;

import cn.hutool.json.JSONArray;
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
import java.util.*;
import java.util.stream.Collectors;

@ScoringStrategyConfig(appType = 1,scoringStrategy = 0)
public class CustomTestScoringStrategy implements ScoringStrategy {
    /**
     * 自定义测评类应用评分策略
     */
    @Resource
    private QuestionService questionService;

    @Resource
    private ScoringResultService scoringResultService;

    @Override
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        //1.根据appid查询题目和题目结果信息
        Long id=app.getId();
        Question question = questionService.getOne(
                Wrappers.lambdaQuery(Question.class).eq(Question::getAppId, id)
        );
        List<ScoringResult> scoringResultList = scoringResultService.list(
                Wrappers.lambdaQuery(ScoringResult.class).eq(ScoringResult::getAppId, id)
        );

        //2.统计用户每一个选项对应的属性信息的个数
        QuestionVO questionVO = QuestionVO.objToVo(question);
        List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();
        /*// 初始化一个字典来存储每个属性的分数
        Map<String, Integer> score = new HashMap<>();

        // 遍历用户的选择和问题内容来计算分数
        for (int i = 0; i < choices.size(); i++) {
            String choice = choices.get(i);  // 用户的回答，"A" 或 "B"
            QuestionContentDTO currentQuestion = questionContent.get(i); // 当前的问题对象

            // 找到用户选择的选项
            QuestionContentDTO.Option selectedOption = currentQuestion.getOptions().stream()
                    .filter(option -> option.getKey().equals(choice))
                    .findFirst()
                    .orElse(null);

            // 更新相应属性的分数
            if (selectedOption != null) {
                String result = selectedOption.getResult();
                score.put(result, score.getOrDefault(result, 0) + 1);
            }
        }

        // 找到匹配的结果
        ScoringResult bestResult = null;
        int maxMatchCount = -1;

        for (ScoringResult result : scoringResultList) {
            int matchCount = 0;
            for (String prop : JSONUtil.toList(result.getResultProp(), String.class)) {
                matchCount += score.getOrDefault(prop, 0);
            }
            if (matchCount > maxMatchCount) {
                maxMatchCount = matchCount;
                bestResult = result;
            }
        }*/

        // 2.统计用户每一个选项对应的属性信息的个数
        Map<String, Integer> score = new HashMap<>();

        for (int i = 0; i < choices.size(); i++) {
            String choice = choices.get(i);
            QuestionContentDTO currentQuestion = questionContent.get(i);

            QuestionContentDTO.Option selectedOption = currentQuestion.getOptions().stream()
                    .filter(option -> option.getKey().equals(choice))
                    .findFirst()
                    .orElse(null);

            if (selectedOption != null) {
                String result = selectedOption.getResult();
                score.put(result, score.getOrDefault(result, 0) + 1);
            }
        }
        System.out.println("User Scores:");
        score.forEach((prop, count) -> {
            System.out.println(prop + ": " + count);
        });

        // 3.确定每组对立属性
        int propLength = JSONUtil.toList(scoringResultList.get(0).getResultProp(),String.class).size();
        List<Set<String>> opposingGroups = new ArrayList<>();
        for (int i = 0; i < propLength; i++) {
            Set<String> group = new HashSet<>();
            for (ScoringResult result : scoringResultList) {
                // 使用Hutool JSONUtil解析JSON字符串为List<String>
                List<String> resultProps = parseResultProp(result.getResultProp());
                group.add(resultProps.get(i));
            }
            opposingGroups.add(group);
        }
        // 打印确定的对立属性组合
        System.out.println("Opposing Groups:");
        for (int i = 0; i < opposingGroups.size(); i++) {
            Set<String> group = opposingGroups.get(i);
            System.out.println("Group " + (i + 1) + ": " + group);
        }

        // 4.计算每组对立属性的得分
        Map<String, Integer> finalScores = new HashMap<>();
        for (Set<String> group : opposingGroups) {
            Map<String, Integer> groupScores = new HashMap<>();
            for (String prop : group) {
                groupScores.put(prop, score.getOrDefault(prop, 0));
            }
            String bestProp = groupScores.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);
            if (bestProp != null) {
                finalScores.put(bestProp, groupScores.get(bestProp));
            }
        }
        // 打印每组对立属性的得分
        System.out.println("Final Scores:");
        finalScores.forEach((prop, count) -> {
            System.out.println(prop + ": " + count);
        });

        // 5.根据最终的属性组合查找匹配的结果
        List<String> finalResultProps = finalScores.keySet().stream().collect(Collectors.toList());
        ScoringResult bestResult = scoringResultList.stream()
                .filter(result -> {
                    List<String> resultProps = parseResultProp(result.getResultProp());
                    return new HashSet<>(resultProps).equals(new HashSet<>(finalResultProps));
                })
                .findFirst()
                .orElse(null);

        // 打印最佳匹配结果
        if (bestResult != null) {
            System.out.println("Best Result:");
            System.out.println("Result Name: " + bestResult.getResultName());
            System.out.println("Result Description: " + bestResult.getResultDesc());
            System.out.println("Result Picture: " + bestResult.getResultPicture());
            System.out.println("Result Props: " + bestResult.getResultProp());
        } else {
            System.out.println("No matching result found.");
        }
        // 4. 返回评分结果
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setAppId(id);
        userAnswer.setAppType(app.getAppType());
        userAnswer.setScoringStrategy(app.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        userAnswer.setResultId(bestResult.getId());
        userAnswer.setResultName(bestResult.getResultName());
        userAnswer.setResultDesc(bestResult.getResultDesc());
        userAnswer.setResultPicture(bestResult.getResultPicture());
        return userAnswer;

    }
    // 使用Hutool JSONUtil解析JSON字符串为List<String>
    private List<String> parseResultProp(String resultPropJson) {
        JSONArray jsonArray = JSONUtil.parseArray(resultPropJson);
        List<String> resultProps = new ArrayList<>();
        for (Object obj : jsonArray) {
            resultProps.add(obj.toString());
        }
        return resultProps;
    }
}
