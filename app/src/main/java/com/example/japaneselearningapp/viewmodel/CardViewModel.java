package com.example.japaneselearningapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.japaneselearningapp.model.Card;
import com.example.japaneselearningapp.repository.CardRepository;

import java.util.List;

public class CardViewModel extends AndroidViewModel {
    private CardRepository repository;

    public CardViewModel(Application application){
        super(application);
        repository = new CardRepository(application);
    }

    public LiveData<List<Card>> getAllCards(){
        return repository.getAllCards();
    }

    public LiveData<Card> getRandomCard(){
        return repository.getRandomCard();
    }
    public LiveData<Card> getRandomCardByType(String type){
        return repository.getRandomCardByType(type);
    }

    public void insert(Card card){
        repository.insertCard(card);
    }

    public void delete(Card card){
        repository.deleteCard(card);
    }
}
