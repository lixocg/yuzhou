package com.yuzhou.openai;

import com.yuzhou.openai.api.edit.EditRequest;
import com.yuzhou.openai.api.edit.EditResult;
import com.yuzhou.openai.service.OpenAiService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EditTest {

    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token);

    @Test
    void edit() {
        EditRequest request = EditRequest.builder()
                .model("text-davinci-edit-001")
                .input("What day of the wek is it?")
                .instruction("Fix the spelling mistakes")
                .build();

        EditResult result = service.createEdit(request);
        assertNotNull(result.getChoices().get(0).getText());
    }

}
