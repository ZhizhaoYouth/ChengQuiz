package com.yang.chengquiz.controller;

import com.yang.chengquiz.common.BaseResponse;
import com.yang.chengquiz.common.ErrorCode;
import com.yang.chengquiz.common.ResultUtils;
import com.yang.chengquiz.exception.ThrowUtils;
import com.yang.chengquiz.mapper.UserAnswerMapper;
import com.yang.chengquiz.model.dto.statistic.AppAnswerCountDTO;
import com.yang.chengquiz.model.dto.statistic.AppAnswerResultCountDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/app/statistic")
@Slf4j
public class AppStatisticController {
    @Resource
    private UserAnswerMapper userAnswerMapper;

    /**
     * 获取用户答题统计(top 10)
     * @return
     */
    @GetMapping("/answer_count")
    public BaseResponse<List<AppAnswerCountDTO>> getAppAnswerCount() {
        return ResultUtils.success(userAnswerMapper.doAppAnswerCount());
    }

    /**
     * 获取某应用的用户答题结果统计
     * @param appId
     * @return
     */
    @GetMapping("/answer_result_count")
    public BaseResponse<List<AppAnswerResultCountDTO>> getAppAnswerResultCount(Long appId) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR,"appId非法");
        return ResultUtils.success(userAnswerMapper.doAppAnswerResultCount(appId));
    }
}
