package org.example;

import com.google.gson.annotations.SerializedName;
import retrofit2.Retrofit;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CrptApi {
    private final TimeUnit timeUnit;
    private final int requestLimit;
    private long startTime;
    private long endTime;

    public CrptApi(final TimeUnit timeUnit, final int requestLimit) {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;

        startTime = System.currentTimeMillis();
    }

    public void createDocument(Document document, String signature) {
        endTime = System.currentTimeMillis();
//        if (endTime - startTime < timeUnit) {
//
//        }
//        Использование симафора Semaphore
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ismp.crpt.ru/api/")
                .build();

        CrptService service = retrofit.create(CrptService.class);
        service.createDocument(document, signature);
    }

    class Document {
        private Description description;
        @SerializedName("doc_id")
        private String docId;
        @SerializedName("doc_status")
        private String docStatus;
        @SerializedName("doc_type")
        private String docType;
        private boolean importRequest;
        @SerializedName("owner_inn")
        private String ownerInn;
        @SerializedName("participant_inn")
        private String participantInn;
        @SerializedName("producer_inn")
        private String producerInn;
        @SerializedName("production_date")
        private String productionDate;
        @SerializedName("production_type")
        private String productionType;
        private List<Product> products = new ArrayList<>();
        @SerializedName("reg_date")
        private String regDate;
        @SerializedName("reg_number")
        private String regNumber;
    }

    private class Description {
        private String participantInn;
    }

    private class Product {
        @SerializedName("certificate_document")
        private String certificateDocument;
        @SerializedName("certificate_document_date")
        private String certificateDocumentDate;
        @SerializedName("certificate_document_number")
        private String certificateDocumentNumber;
        @SerializedName("owner_inn")
        private String ownerInn;
        @SerializedName("producer_inn")
        private String producerInn;
        @SerializedName("production_date")
        private String productionDate;
        @SerializedName("tnved_code")
        private String tnvedCode;
        @SerializedName("uit_code")
        private String uitCode;
        @SerializedName("uitu_code")
        private String uituCode;
    }

    private interface CrptService {
        @POST("v3/lk/documents/create")
        void createDocument(@Path("document") Document document,
                            @Path("signature") String signature);
    }
}
