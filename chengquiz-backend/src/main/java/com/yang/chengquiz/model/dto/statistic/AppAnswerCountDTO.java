package com.yang.chengquiz.model.dto.statistic;

import lombok.Data;

/**
 * 统计APP用户提交的答题数
 */
@Data
public class AppAnswerCountDTO {

    private Long appId;
    /**
     * 用户提交答案数
     */
    private Long answerCount;
}
