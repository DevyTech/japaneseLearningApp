package com.example.japaneselearningapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.japaneselearningapp.fragment.DailyWordFragment;
import com.example.japaneselearningapp.fragment.FlashCardFragment;
import com.example.japaneselearningapp.fragment.QuizFragment;
import com.example.japaneselearningapp.repository.CardRepository;
import com.example.japaneselearningapp.repository.VocabularyRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private Fragment cardFragment, wordFragment, quizFragment;
    private int selectedMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        VocabularyRepository repo = new VocabularyRepository(getApplication());
        CardRepository cardRepo = new CardRepository(getApplication());
        cardRepo.importFromJson(this);
        repo.importFromJson(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();
        cardFragment = fragmentManager.findFragmentByTag("cardFragment");
        wordFragment = fragmentManager.findFragmentByTag("wordFragment");
        quizFragment = fragmentManager.findFragmentByTag("quizFragment");

        selectedMenu = R.id.card_menu;
        createFragment();
        showDefaultFragment();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            fragmentManager.beginTransaction().hide(cardFragment).hide(wordFragment).hide(quizFragment).commit();
            int id = item.getItemId();
            if (id == R.id.card_menu){
                fragmentManager.beginTransaction().show(cardFragment).commit();
                return true;
            } else if (id == R.id.word_menu) {
                fragmentManager.beginTransaction().show(wordFragment).commit();
                return true;
            } else if (id == R.id.quiz_menu) {
                fragmentManager.beginTransaction().show(quizFragment).commit();
                return true;
            }
            return false;
        });
    }

    private void showDefaultFragment() {
        if (selectedMenu == R.id.card_menu){
            fragmentManager.beginTransaction().show(cardFragment).hide(wordFragment).hide(quizFragment).commit();
        } else if (selectedMenu == R.id.word_menu) {
            fragmentManager.beginTransaction().show(wordFragment).hide(cardFragment).hide(quizFragment).commit();
        } else if (selectedMenu == R.id.quiz_menu) {
            fragmentManager.beginTransaction().show(quizFragment).hide(cardFragment).hide(wordFragment).commit();
        }
        bottomNavigationView.setSelectedItemId(selectedMenu);
    }

    private void createFragment() {
        if (cardFragment == null) {
            cardFragment = new FlashCardFragment();
            fragmentManager.beginTransaction().add(R.id.frame, cardFragment, "cardFragment").commit();
        }
        if (wordFragment == null) {
            wordFragment = new DailyWordFragment();
            fragmentManager.beginTransaction().add(R.id.frame, wordFragment, "wordFragment").commit();
        }
        if (quizFragment == null) {
            quizFragment = new QuizFragment();
            fragmentManager.beginTransaction().add(R.id.frame, quizFragment, "quizFragment").commit();
        }
    }
}