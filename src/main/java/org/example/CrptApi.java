package org.example;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.concurrent.TimedSemaphore;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrptApi {
    private Logger logger = Logger.getLogger(CrptApi.class.getName());
    final TimedSemaphore timedSemaphore;

    public CrptApi(final TimeUnit timeUnit,
                   final int timePeriod,
                   final int requestLimit) {
        timedSemaphore = new TimedSemaphore(timePeriod, timeUnit, requestLimit);
    }

    public Runnable createDocument(Document document) {
        return () -> {
            if(timedSemaphore.tryAcquire()) {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("https://ismp.crpt.ru/api/")
                        .build();
                CrptService service = retrofit.create(CrptService.class);
                Call<String> call = service.createDocument(document);

                try {
                    Response<String> response = call.execute();
                    logger.log(Level.INFO, response.message());
                } catch (IOException e) {
                    logger.log(Level.WARNING, e.getMessage());
                }
            }
        };
    }

    @Builder
    static class Document {
        Description description;
        @SerializedName("doc_id")
        String docId;
        @SerializedName("doc_status")
        String docStatus;
        @SerializedName("doc_type")
        String docType;
        boolean importRequest;
        @SerializedName("owner_inn")
        String ownerInn;
        @SerializedName("participant_inn")
        String participantInn;
        @SerializedName("producer_inn")
        String producerInn;
        @SerializedName("production_date")
        String productionDate;
        @SerializedName("production_type")
        String productionType;
        List<Product> products;
        @SerializedName("reg_date")
        String regDate;
        @SerializedName("reg_number")
        String regNumber;
    }

    @Builder
    @Data
    static class Description {
        String participantInn;
    }

    @Builder
    @Data
    static class Product {
        @SerializedName("certificate_document")
        String certificateDocument;
        @SerializedName("certificate_document_date")
        String certificateDocumentDate;
        @SerializedName("certificate_document_number")
        String certificateDocumentNumber;
        @SerializedName("owner_inn")
        String ownerInn;
        @SerializedName("producer_inn")
        String producerInn;
        @SerializedName("production_date")
        String productionDate;
        @SerializedName("tnved_code")
        String tnvedCode;
        @SerializedName("uit_code")
        String uitCode;
        @SerializedName("uitu_code")
        String uituCode;
    }

    private interface CrptService {
        @POST("v3/lk/documents/create")
        Call<String> createDocument(@Body Document document);
    }
}
