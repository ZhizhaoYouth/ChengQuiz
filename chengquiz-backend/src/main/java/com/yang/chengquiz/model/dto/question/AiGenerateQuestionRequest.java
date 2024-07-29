package com.yang.chengquiz.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * AI生成题目请求
 */
@Data
public class AiGenerateQuestionRequest implements Serializable {
    /**
     * 应用id
     */
    private Long appId;

    /**
     * 题目数量
     */
    int questionCount=10;

    /**
     * 选项数量
     */
    int optionCount=2;

    private static final long serialVersionUID = 1L;
}
