package com.yang.chengquiz;

import com.yang.chengquiz.controller.QuestionController;
import com.yang.chengquiz.model.dto.question.AiGenerateQuestionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class QuestionControllerTest {
    @Resource
    private QuestionController questionController;


    @Test
    void aiGenerateQuestionSSEVIPTest() throws InterruptedException {
        AiGenerateQuestionRequest request = new AiGenerateQuestionRequest();
        request.setAppId(1L);
        request.setQuestionCount(10);
        request.setOptionCount(2);

        questionController.aiGenerateQuestionSSETest(request, false);
        questionController.aiGenerateQuestionSSETest(request, false);
        questionController.aiGenerateQuestionSSETest(request, true);

        Thread.sleep(1000000L);
    }

}
