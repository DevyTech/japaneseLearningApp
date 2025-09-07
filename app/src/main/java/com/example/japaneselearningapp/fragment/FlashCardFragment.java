package com.example.japaneselearningapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.japaneselearningapp.R;
import com.example.japaneselearningapp.viewmodel.CardViewModel;
import com.google.android.material.card.MaterialCardView;

public class FlashCardFragment extends Fragment {

    private MaterialCardView cardView;
    private TextView tvHiraKana;
    private CheckBox checkHira, checkKana;
    private Button btnNext;
    private boolean isHiragana = true, isKatakana, isHirakana;
    private CardViewModel cardViewModel;
    private String character,romaji;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flash_card, container, false);

        cardView = view.findViewById(R.id.card);
        tvHiraKana = view.findViewById(R.id.tvHiraKana);
        checkHira = view.findViewById(R.id.check_hira);
        checkKana = view.findViewById(R.id.check_kana);
        btnNext = view.findViewById(R.id.btn_next);

        cardViewModel = new ViewModelProvider(this).get(CardViewModel.class);

        checkHira.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                isHiragana = b;
            }
        });
        checkKana.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                isKatakana = b;
            }
        });

        shuffle();

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isHirakana){
                    isHirakana = false;
                    tvHiraKana.setText(character);
                }else{
                    isHirakana = true;
                    tvHiraKana.setText(romaji);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shuffle();
                isHirakana = false;
            }
        });
        return view;
    }

    private void shuffle(){
        if (isHiragana && isKatakana){
            cardViewModel.getRandomCard().observe(getViewLifecycleOwner(), card -> {
                if (card!=null){
                    character = card.character;
                    romaji = card.romaji;
                    tvHiraKana.setText(character);
                }
            });
        } else if (isHiragana) {
            cardViewModel.getRandomCardByType("hiragana").observe(getViewLifecycleOwner(), card -> {
                if (card!=null){
                    character = card.character;
                    romaji = card.romaji;
                    tvHiraKana.setText(character);
                }
            });
        } else if (isKatakana) {
            cardViewModel.getRandomCardByType("katakana").observe(getViewLifecycleOwner(), card -> {
                if (card!=null){
                    character = card.character;
                    romaji = card.romaji;
                    tvHiraKana.setText(character);
                }
            });
        }
    }
}