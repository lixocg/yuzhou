package com.yuzhou.openai;

import com.yuzhou.openai.api.finetune.FineTuneEvent;
import com.yuzhou.openai.api.finetune.FineTuneRequest;
import com.yuzhou.openai.api.finetune.FineTuneResult;
import com.yuzhou.openai.service.OpenAiService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FineTuneTest {
    static OpenAiService service;
    static String fileId;
    static String fineTuneId;


    @BeforeAll
   static void setup() throws Exception {
        String token = System.getenv("OPENAI_TOKEN");
        service = new OpenAiService(token);
        fileId = service.uploadFile("fine-tune", "src/test/resources/fine-tuning-data.jsonl").getId();

        // wait for file to be processed
        TimeUnit.SECONDS.sleep(10);
    }

    @AfterAll
    static void teardown() {
        service.deleteFile(fileId);
    }

    @Test
    @Order(1)
    void createFineTune() {
        FineTuneRequest request = FineTuneRequest.builder()
                .trainingFile(fileId)
                .model("ada")
                .build();

        FineTuneResult fineTune = service.createFineTune(request);
        fineTuneId = fineTune.getId();

        assertEquals("pending", fineTune.getStatus());
    }

    @Test
    @Order(2)
    void listFineTunes() {
        List<FineTuneResult> fineTunes = service.listFineTunes();

        assertTrue(fineTunes.stream().anyMatch(fineTune -> fineTune.getId().equals(fineTuneId)));
    }

    @Test
    @Order(3)
    void listFineTuneEvents() {
        List<FineTuneEvent> events = service.listFineTuneEvents(fineTuneId);

        assertFalse(events.isEmpty());
    }

    @Test
    @Order(3)
    void retrieveFineTune() {
        FineTuneResult fineTune = service.retrieveFineTune(fineTuneId);

        assertEquals("ada", fineTune.getModel());
    }

    @Test
    @Order(4)
    void cancelFineTune() {
        FineTuneResult fineTune = service.cancelFineTune(fineTuneId);

        assertEquals("cancelled", fineTune.getStatus());
    }
}
