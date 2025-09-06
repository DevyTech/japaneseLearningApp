package com.example.japaneselearningapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "hirakana")
public class Card {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String character;
    public String type;
    public String romaji;

    public Card(String character, String type, String romaji) {
        this.character = character;
        this.type = type;
        this.romaji = romaji;
    }
}
