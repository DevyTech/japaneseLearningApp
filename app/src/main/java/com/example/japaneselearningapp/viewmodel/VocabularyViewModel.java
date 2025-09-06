package com.example.japaneselearningapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.japaneselearningapp.database.AppDatabase;
import com.example.japaneselearningapp.model.Vocabulary;
import com.example.japaneselearningapp.repository.VocabularyRepository;

import java.util.List;

public class VocabularyViewModel extends AndroidViewModel {
    private VocabularyRepository repository;

    public VocabularyViewModel(Application application){
        super(application);
        repository = new VocabularyRepository(application);
    }

    public LiveData<List<Vocabulary>> getAllWords(){
        return repository.getAllWords();
    }

    public LiveData<Vocabulary> getRandomWord(){
        return repository.getRandomWord();
    }

    public void insert(Vocabulary word){
        repository.insertWord(word);
    }

    public void delete(Vocabulary word){
        repository.deleteWord(word);
    }
}
