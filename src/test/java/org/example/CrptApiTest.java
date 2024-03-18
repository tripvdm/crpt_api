package org.example;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

class CrptApiTest {

    private static CrptApi.Document document;
    private static String signature;
    @BeforeAll
    static void beforeAll() {
        String json = "{\n" +
                "  \"description\": {\n" +
                "    \"participantInn\": \"string\"\n" +
                "  },\n" +
                "  \"doc_id\": \"string\",\n" +
                "  \"doc_status\": \"string\",\n" +
                "  \"doc_type\": \"LP_INTRODUCE_GOODS\",\n" +
                "  \"importRequest\": true,\n" +
                "  \"owner_inn\": \"string\",\n" +
                "  \"participant_inn\": \"string\",\n" +
                "  \"producer_inn\": \"string\",\n" +
                "  \"production_date\": \"2020-01-23\",\n" +
                "  \"production_type\": \"string\",\n" +
                "  \"products\": [\n" +
                "    {\n" +
                "      \"certificate_document\": \"string\",\n" +
                "      \"certificate_document_date\": \"2020-01-23\",\n" +
                "      \"certificate_document_number\": \"string\",\n" +
                "      \"owner_inn\": \"string\",\n" +
                "      \"producer_inn\": \"string\",\n" +
                "      \"production_date\": \"2020-01-23\",\n" +
                "      \"tnved_code\": \"string\",\n" +
                "      \"uit_code\": \"string\",\n" +
                "      \"uitu_code\": \"string\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"reg_date\": \"2020-01-23\",\n" +
                "  \"reg_number\": \"string\"\n" +
                "}";
        Gson gson = new Gson();

        document = gson.fromJson(json, CrptApi.Document.class);
        signature = "";
    }

    @Test
    public void testRequestForDocument() {
        TimeUnit timeUnit = TimeUnit.valueOf("SECONDS");
        CrptApi crptApi = new CrptApi(timeUnit, 2);
        crptApi.createDocument(document, signature);
    }

    @Test
    public void checkTimeUnit() {
        TimeUnit timeUnit = TimeUnit.valueOf("SECONDS");
    }
}