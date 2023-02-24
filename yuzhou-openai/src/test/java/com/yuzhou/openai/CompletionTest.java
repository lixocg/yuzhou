package com.yuzhou.openai;

import com.alibaba.fastjson2.JSON;
import com.yuzhou.openai.api.completion.CompletionChoice;
import com.yuzhou.openai.api.completion.CompletionRequest;
import com.yuzhou.openai.service.OpenAiService;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CompletionTest {

    String token = System.getenv("OPENAI_TOKEN");

    OpenAiService service = new OpenAiService(token);

    @Test
    void createCompletion() {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt("spring aop原理？")
                .echo(true)
                .n(1)
                .maxTokens(256)
                .temperature(0.3)
                .user("testing")
                .logitBias(new HashMap<>())
                .build();

        List<CompletionChoice> choices = service.createCompletion(completionRequest).getChoices();
        System.out.println(JSON.toJSONString(choices));
        assertEquals(1, choices.size());
    }

}
