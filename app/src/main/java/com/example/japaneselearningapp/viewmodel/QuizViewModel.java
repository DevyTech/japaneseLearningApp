package com.example.japaneselearningapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.japaneselearningapp.model.Card;
import com.example.japaneselearningapp.model.QuizQuestion;
import com.example.japaneselearningapp.repository.CardRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuizViewModel extends AndroidViewModel {
    private final CardRepository repository;
//    trigger untuk membuat soal baru
    private final MutableLiveData<Long> refreshTrigger = new MutableLiveData<>();
//    LiveData yang berasal dari DAO
    private final LiveData<Card> currentCardLive;
    private final LiveData<List<Card>> optionsRawLive;
//    MediatorLiveData yang menggabungkan currentCardLive dan optionsRawLive
    private final MediatorLiveData<QuizQuestion> quizLive = new MediatorLiveData<>();
    private Card cachedCard = null;
    private List<Card> cachedOptions = null;

//    quiz state
    private int score = 0;
    private int currentQuestion = 0;
    private final int totalQuestions = 10;

    public QuizViewModel(Application application){
        super(application);
        repository = new CardRepository(application);
        //saat refreshTrigger berubah -> repository random dijalankan
        currentCardLive = Transformations.switchMap(refreshTrigger, t -> repository.getRandomQuestion());
//        options diambil setiap kali currentCardLive berubah
        optionsRawLive = Transformations.switchMap(currentCardLive, card -> {
            if (card!=null){
                MutableLiveData<List<Card>> empty = new MediatorLiveData<>(new ArrayList<>());
                return empty;
            }else {
//                ambil lebih banyak opsi supaya mudah memilih 3 unique yang bukan correct
                return repository.getRandomOptions(card.id, 20);
            }
        });

//        gabungkan sources
        quizLive.addSource(currentCardLive, card -> {
            cachedCard = card;
            buildQuizIfReady();
        });
        quizLive.addSource(optionsRawLive, opts -> {
            cachedOptions = opts;
            buildQuizIfReady();
        });
        nextQuestion();
    }

    private void buildQuizIfReady() {
        if (cachedCard==null) return;
//        create wrong options list (romaji) excluding the correct one
        List<String> wrongs = new ArrayList<>();
        if (cachedOptions!=null){
            Set<String> added = new HashSet<>();
            for (Card c : cachedOptions){
                if (c == null) continue;
                if (c.id == cachedCard.id) continue;
                if (added.contains(c.romaji)) continue;
                added.add(c.romaji);
                wrongs.add(c.romaji);
                if (wrongs.size()>=3) break;
            }
        }

//        fallback : kalau kurang dari 3 wrong options, isi dengan placeholders atau ulangi
//        beberapa wrong yang ada (agar tidak out-of-bounds)
        if (wrongs.size() < 3){
//            ambil dari cachedOptions romaji lain (boleh duplicate) atau pakai "-"
            if (cachedOptions!=null&&!cachedOptions.isEmpty()){
                int i = 0;
                while (wrongs.size() < 3 && i < cachedOptions.size()){
                    String r = cachedOptions.get(i).romaji;
                    if (!r.equals(cachedCard.romaji)) wrongs.add(r);
                    i++;
                }
            }
            while (wrongs.size() < 3){
                wrongs.add("???"); //fallback terakhir
            }
        }

        List<String> finalOptions = new ArrayList<>(wrongs);
        finalOptions.add(cachedCard.romaji);//tambahkan jawaban benar
        Collections.shuffle(finalOptions);

        quizLive.setValue(new QuizQuestion(cachedCard.character, cachedCard.romaji, finalOptions, cachedCard.id));
    }
    public LiveData<QuizQuestion> getQuizLive(){
        return quizLive;
    }

//    minta soal baru
    public void nextQuestion(){
//        reset caches agar buildQuizIfReady() menunggu value baru
        cachedCard = null;
        cachedOptions = null;
        refreshTrigger.setValue(System.currentTimeMillis());
        currentQuestion++;
    }
    public void increaseScore() { score++; }
    public int getScore() { return score; }
    public int getCurrentQuestion() { return currentQuestion; }
    public int getTotalQuestions() { return totalQuestions; }
    public boolean isFinished() { return currentQuestion >= totalQuestions; }

    public void resetQuiz() {
        score = 0;
        currentQuestion = 0;
        nextQuestion();
    }
}
