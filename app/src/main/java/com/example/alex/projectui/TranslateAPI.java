package com.example.alex.projectui;

import android.content.Context;

import java.net.URLEncoder;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Query;

public class TranslateAPI {
    private static final String ENDPOINT = "http://api.mymemory.translated.net";
    public final static String FRENCH = "FR";
    public final static String ENGLISH = "EN";
    public final static String ITALIAN = "IT";
    public final static String SPANISH = "ES";
    private final TranslateService mService;
    private String translation = "";
    Callbacks listener;

    public interface TranslateService {
        @GET("/get")
        Call<TranslatedData> getTranslation(
                @Query("q") String textToTranslate,
                @Query(value = "langpair", encoded = true)
                String languagePair);
    }

    public interface Callbacks {
        void onTranslationReceived();
        void onTranslationFailed();
    }

    public TranslateAPI(Callbacks listener){
         Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(TranslateService.class);
        this.listener = listener;
    }

    public void translate(final String textToTranslate, final String fromLanguage, final String toLanguage) {
        mService.getTranslation(textToTranslate, URLEncoder.encode(fromLanguage + "|" + toLanguage))
                .enqueue(new Callback<TranslatedData>() {

                    @Override
                    public void onResponse(Response<TranslatedData> response, Retrofit retrofit) {
                        String output =
                                String.format(response.body().responseData.translatedText);
                        String.format("Translation of: %s, %s->%s = %s", textToTranslate,
                                fromLanguage, toLanguage, response.body().responseData.translatedText);
//                        System.out.println("Result: " + output);
                        translation = output;
//                        System.out.println("The result of the field translation is: " + translation);
                        listener.onTranslationReceived();


                    }

                    @Override
                    public void onFailure(Throwable t) {
//                        System.out.println("[DEBUG]" + " RestApi onFailure - " + "");
                        listener.onTranslationFailed();
                    }
                });
    }

    public String getTranslation() {
        return translation;
    }
}