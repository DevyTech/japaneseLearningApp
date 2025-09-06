package com.example.japaneselearningapp.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.japaneselearningapp.database.AppDatabase;
import com.example.japaneselearningapp.database.VocabularyDao;
import com.example.japaneselearningapp.model.Vocabulary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.Executors;

public class VocabularyRepository {
    private final VocabularyDao dao;

    // Constructor menerima Application → ambil instance DB → ambil DAO
    public VocabularyRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        dao = db.vocabularyDao();
    }

    public LiveData<Vocabulary> getRandomWord() {
        return dao.getRandomWord();
    }

    public LiveData<List<Vocabulary>> getAllWords() {
        return dao.getAllWords();
    }

    public void insertWord(final Vocabulary word) {
        Executors.newSingleThreadExecutor().execute(() -> dao.insertWord(word));
    }

    public void deleteWord(final Vocabulary word) {
        Executors.newSingleThreadExecutor().execute(() -> dao.deleteWord(word));
    }

    public void importFromJson(Context context) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                InputStream is = context.getAssets().open("words.json");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();

                JSONArray jsonArray = new JSONArray(sb.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Vocabulary vocab = new Vocabulary(
                            obj.getString("kanji"),
                            obj.getString("hiragana"),
                            obj.getString("romaji"),
                            obj.getString("meaning"),
                            obj.getString("example")
                    );
                    insertWord(vocab);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
