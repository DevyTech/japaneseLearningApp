package com.example.japaneselearningapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.japaneselearningapp.model.Card;

import java.util.List;

@Dao
public interface CardDao {
    @Query("SELECT * FROM hirakana ORDER BY RANDOM() LIMIT 1")
    LiveData<Card> getRandomCard();
    @Query("SELECT * FROM hirakana")
    LiveData<List<Card>> getAllCards();
    @Query("SELECT * FROM hirakana WHERE type = :type ORDER BY RANDOM() LIMIT 1")
    LiveData<Card> getRandomCardByType(String type);
    @Query("SELECT * FROM hirakana ORDER BY RANDOM() LIMIT 1")
    LiveData<Card> getRandomQuestion();
    @Query("SELECT * FROM hirakana WHERE id != :correctId ORDER BY RANDOM() LIMIT :limit")
    LiveData<List<Card>> getRandomOptions(int correctId, int limit);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCard(Card card);
    @Delete
    void deleteCard(Card card);
}
