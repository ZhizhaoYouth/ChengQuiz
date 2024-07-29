package com.yang.chengquiz.model.dto.statistic;

import lombok.Data;

/**
 * 统计用户答题结果的个数有多少
 */
@Data
public class AppAnswerResultCountDTO {
    // 结果名称
    private String resultName;
    // 对应个数
    private String resultCount;
}

