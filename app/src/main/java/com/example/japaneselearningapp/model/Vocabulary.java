package com.example.japaneselearningapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vocabulary")
public class Vocabulary {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String kanji;
    public String hiragana;
    public String romaji;
    public String meaning;
    public String example;

    public Vocabulary(String kanji, String hiragana, String romaji, String meaning, String example){
        this.kanji = kanji;
        this.hiragana = hiragana;
        this.romaji = romaji;
        this.meaning = meaning;
        this.example = example;
    }
}
