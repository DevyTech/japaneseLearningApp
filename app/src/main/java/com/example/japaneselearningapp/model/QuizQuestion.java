package com.example.japaneselearningapp.model;

import java.util.List;

public class QuizQuestion {
    private String question; //huruf kana
    private String correctAnswer; //huruf romaji
    private List<String> options;

    public QuizQuestion(String question, String correctAnswer, List<String> options){
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.options = options;
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
}
