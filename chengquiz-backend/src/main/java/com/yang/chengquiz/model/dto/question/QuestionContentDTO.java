package com.yang.chengquiz.model.dto.question;

import com.yang.chengquiz.common.ErrorCode;
import com.yang.chengquiz.exception.ThrowUtils;
import com.yang.chengquiz.model.entity.App;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionContentDTO {
    /**
     * 题目标题
     */
    private String title;
    /**
     * 题目选项列表
     */
    private List<Option> options;

    /**
     * 题目选项
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Option {
        private String result;
        private int score;
        private String value;
        private String key;
        public void ValidOption(Option option,boolean add){
            ThrowUtils.throwIf(option == null, ErrorCode.PARAMS_ERROR);
            // 从对象中取值
            String result = option.getResult();
            int score = option.getScore();
            String value = option.getValue();
            String key = option.getKey();
            // 创建数据时，参数不能为空
            if (add) {
                // 补充校验规则
                ThrowUtils.throwIf(StringUtils.isBlank(result)&&score < 0, ErrorCode.PARAMS_ERROR,"题目选项分数和结果至少有一个不能为空");
                ThrowUtils.throwIf(StringUtils.isBlank(value), ErrorCode.PARAMS_ERROR,"题目选项内容不能为空");
                ThrowUtils.throwIf(StringUtils.isBlank(key), ErrorCode.PARAMS_ERROR,"题目选项不能为空");
            }
            // 修改数据时，有参数则校验
            // 补充校验规则
        }
    }


}
