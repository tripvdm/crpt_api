package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class CrptApiTest {
    private static CrptApi.Document document;

    @BeforeAll
    static void beforeAll() {
        document = CrptApi.Document
                .builder()
                .description(CrptApi.Description.builder()
                        .participantInn("123")
                        .build())
                .docId("123")
                .docStatus("good")
                .docType("232")
                .importRequest(true)
                .ownerInn("sdsa")
                .participantInn("weqweqw")
                .producerInn("ewrwe")
                .productionDate("erwtwe")
                .productionType("gdfgdf")
                .products(new ArrayList<>(Collections.singleton(CrptApi.Product.builder()
                        .certificateDocument("wdsw")
                        .certificateDocumentDate("dgfsd")
                        .certificateDocumentNumber("erwe")
                        .ownerInn("ertwerw")
                        .producerInn("dgds")
                        .productionDate("ewrwe")
                        .tnvedCode("gfdgfd")
                        .uitCode("dfsd")
                        .uituCode("dgdfgdf")
                        .build())))
                .regDate("3435")
                .regNumber("3423")
                .build();
    }

    @Test
    void testCheckResponseForDocumentRequest() {
        TimeUnit timeUnit = TimeUnit.valueOf("SECONDS");
        CrptApi crptApi = new CrptApi(timeUnit, 1000, 1);
        crptApi.createDocument(document).run();
    }

    @Test
    void testTimedSemaphoreWhenActualSlotsLessExpectedSlots() {
        TimeUnit timeUnit = TimeUnit.valueOf("SECONDS");
        int actualSlots = 30;
        ExecutorService executorService = Executors.newFixedThreadPool(actualSlots);

        int expectedSlots = 40;
        CrptApi crptApi = new CrptApi(timeUnit, 1, expectedSlots);

        IntStream.range(0, actualSlots)
                .forEach(user -> executorService.execute(crptApi.createDocument(document)));
        executorService.shutdown();

        assertTrue(crptApi.timedSemaphore.tryAcquire());
    }

    @Test
    void testTimedSemaphoreWhenActualSlotsMoreExpectedSlots() {
        TimeUnit timeUnit = TimeUnit.valueOf("SECONDS");
        int actualSlots = 81;
        ExecutorService executorService = Executors.newFixedThreadPool(actualSlots);

        int expectedSlots = 40;
        CrptApi crptApi = new CrptApi(timeUnit, 1, expectedSlots);

        IntStream.range(0, actualSlots)
                .forEach(user -> executorService.execute(crptApi.createDocument(document)));
        executorService.shutdown();

        assertFalse(crptApi.timedSemaphore.tryAcquire());
    }

    @Test
    public void testTimedSemaphoreWhenTimeLimitExceeded() {
        TimeUnit timeUnit = TimeUnit.valueOf("MILLISECONDS");
        int actualSlots = 300;
        ExecutorService executorService = Executors.newFixedThreadPool(actualSlots);

        int expectedSlots = 400;
        CrptApi crptApi = new CrptApi(timeUnit, 50, expectedSlots);

        IntStream.range(0, actualSlots)
                .forEach(user -> executorService.execute(crptApi.createDocument(document)));
        executorService.shutdown();

        int availablePermits = crptApi.timedSemaphore.getAvailablePermits();
        assertNotEquals(availablePermits, 0);
    }
}