package com.example.japaneselearningapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.japaneselearningapp.model.Card;
import com.example.japaneselearningapp.model.QuizQuestion;
import com.example.japaneselearningapp.repository.CardRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizViewModel extends AndroidViewModel {
    private CardRepository repository;
    private MutableLiveData<List<Card>> options = new MutableLiveData<>();
    private LiveData<Card> question;
    private int score = 0;
    private int curentQuestion = 0;
    private int totalQuestion = 10;

    public QuizViewModel(Application application){
        super(application);
        repository = new CardRepository(application);
        question = repository.getRandomQuestion();
    }
    public LiveData<Card> getQuestion(){
        return question;
    }
    public LiveData<List<Card>> getOptions(int correctId){
        return repository.getRandomOptions(correctId,4);
    }

    public void increseScore(){
        score++;
    }
    public int getScore(){
        return score;
    }
    public void nextQuestion(){
        curentQuestion++;
        question = repository.getRandomQuestion();
    }
    public int getCurentQuestion(){
        return curentQuestion;
    }
    public int getTotalQuestion(){
        return totalQuestion;
    }
    public boolean isQuizFinished(){
        return curentQuestion >= totalQuestion;
    }
    public void reset(){
        curentQuestion = 0;
        score = 0;
    }
}
