package com.example.japaneselearningapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.japaneselearningapp.model.Vocabulary;

import java.util.List;

@Dao
public interface VocabularyDao {
    @Query("SELECT * FROM vocabulary ORDER BY RANDOM() LIMIT 1")
    LiveData<Vocabulary> getRandomWord();
    @Query("SELECT * FROM vocabulary")
    LiveData<List<Vocabulary>> getAllWords();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWord(Vocabulary word);

    @Delete
    void deleteWord(Vocabulary word);
}
