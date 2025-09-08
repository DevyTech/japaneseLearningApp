package com.example.japaneselearningapp.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.japaneselearningapp.database.AppDatabase;
import com.example.japaneselearningapp.database.CardDao;
import com.example.japaneselearningapp.model.Card;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.Executors;

public class CardRepository {
    private final CardDao dao;

    public CardRepository(Application application){
        AppDatabase db = AppDatabase.getInstance(application);
        dao = db.cardDao();
    }
    public LiveData<Card> getRandomCard(){
        return dao.getRandomCard();
    }
    public LiveData<Card> getRandomCardByType(String type){
        return dao.getRandomCardByType(type);
    }

    public LiveData<List<Card>> getAllCards(){
        return dao.getAllCards();
    }

    public void insertCard(final Card card){
        Executors.newSingleThreadExecutor().execute(() -> dao.insertCard(card));
    }

    public void deleteCard(final Card card){
        Executors.newSingleThreadExecutor().execute(() -> dao.deleteCard(card));
    }

    public void importFromJson(Context context){
        Executors.newSingleThreadExecutor().execute(()->{
            try{
                InputStream is = context.getAssets().open("hirakana.json");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = reader.readLine())!=null){
                    sb.append(line);
                }
                reader.close();

                JSONArray jsonArray = new JSONArray(sb.toString());
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Card card = new Card(
                            obj.getString("character"),
                            obj.getString("type"),
                            obj.getString("romaji")
                    );
                    insertCard(card);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LiveData<Card> getRandomQuestion(){
        return dao.getRandomQuestion();
    }
    public LiveData<List<Card>> getRandomOptions(int correctId, int limit){
        return dao.getRandomOptions(correctId, limit);
    }
}
