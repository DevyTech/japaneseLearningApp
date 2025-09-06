package com.example.japaneselearningapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.japaneselearningapp.R;
import com.example.japaneselearningapp.database.AppDatabase;
import com.example.japaneselearningapp.viewmodel.VocabularyViewModel;

public class DailyWordFragment extends Fragment {
    private TextView tvKanji, tvHiragana, tvRomaji, tvMeaning, tvExample;
    private Button btnShuffle;
    private VocabularyViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_word, container, false);

        tvKanji = view.findViewById(R.id.tvKanji);
        tvHiragana = view.findViewById(R.id.tvHiragana);
        tvRomaji = view.findViewById(R.id.tvRomaji);
        tvMeaning = view.findViewById(R.id.tvMeaning);
        tvExample = view.findViewById(R.id.tvExample);
        btnShuffle = view.findViewById(R.id.btn_shuffle);

        viewModel = new ViewModelProvider(this).get(VocabularyViewModel.class);

        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //        observe random word
                viewModel.getRandomWord().observe(getViewLifecycleOwner(), vocab -> {
                    if (vocab != null){
                        tvKanji.setText(vocab.kanji);
                        tvHiragana.setText(vocab.hiragana);
                        tvRomaji.setText(vocab.romaji);
                        tvMeaning.setText(vocab.meaning);
                        tvExample.setText(vocab.example);
                    }
                });
            }
        });

        return view;
    }
}