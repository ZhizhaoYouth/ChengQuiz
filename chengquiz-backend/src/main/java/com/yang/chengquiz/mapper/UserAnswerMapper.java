package com.yang.chengquiz.mapper;

import com.yang.chengquiz.model.dto.statistic.AppAnswerCountDTO;
import com.yang.chengquiz.model.dto.statistic.AppAnswerResultCountDTO;
import com.yang.chengquiz.model.entity.UserAnswer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author Mark
* @description 针对表【user_answer(用户答题记录)】的数据库操作Mapper
* @createDate 2024-06-25 20:05:35
* @Entity com.yang.chengquiz.model.entity.UserAnswer
*/
public interface UserAnswerMapper extends BaseMapper<UserAnswer> {
    List<AppAnswerCountDTO> doAppAnswerCount();
    List<AppAnswerResultCountDTO> doAppAnswerResultCount(Long appId);

}




