package com.yuzhou.openai;

import com.alibaba.fastjson2.JSON;
import com.yuzhou.openai.api.Message;
import com.yuzhou.openai.api.completion.CompletionChoice;
import com.yuzhou.openai.api.completion.CompletionRequest;
import com.yuzhou.openai.api.completion.CompletionResult;
import com.yuzhou.openai.service.OpenAiService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ChatCompletionTest {

    String token = "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";

    OpenAiService service = new OpenAiService(token);

    @Test
    void createCompletion() {
        List<Message> messages = new ArrayList<>();
        messages.add(Message.builder().role("user").content("写100字的小作文").build());
        CompletionRequest completionRequest = CompletionRequest.builder()
//                .model("gpt-3.5-turbo")
                .model("gpt-3.5-turbo-0301")
                .messages(messages)
                .n(1)
                .maxTokens(256)
                .temperature(0.3)
                .logitBias(new HashMap<>())
                .build();

        CompletionResult completionResult = service.chatCompletion(completionRequest);
        System.out.println(JSON.toJSONString(completionResult));
    }

}
