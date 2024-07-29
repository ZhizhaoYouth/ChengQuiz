package com.yang.chengquiz.model.dto.question;

import lombok.Data;

/**
 * 封装题目与答案，用于传递给AI进行打分
 */
@Data
public class QuestionAnswerDTO {
    /**
     * 题目
     */
    private String title;
    /**
     * 答案
     */
    private String userAnswer;
}
