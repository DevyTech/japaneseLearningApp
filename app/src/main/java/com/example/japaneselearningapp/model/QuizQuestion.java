package com.example.japaneselearningapp.model;

import java.util.List;

public class QuizQuestion {
    private final String question; //huruf kana
    private final String correctAnswer; //huruf romaji
    private final List<String> options;
    private final int correctId;

    public QuizQuestion(String question, String correctAnswer, List<String> options, int correctId){
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.options = options;
        this.correctId = correctId;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getOptions() {
        return options;
    }
    public int getCorrectId(){
        return correctId;
    }
}
