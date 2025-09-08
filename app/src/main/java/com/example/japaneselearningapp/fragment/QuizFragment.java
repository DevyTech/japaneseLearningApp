package com.example.japaneselearningapp.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.japaneselearningapp.R;
import com.example.japaneselearningapp.model.Card;
import com.example.japaneselearningapp.model.QuizQuestion;
import com.example.japaneselearningapp.viewmodel.QuizViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizFragment extends Fragment {
    private QuizViewModel quizViewModel;
    private TextView textQuestion, textProgress, textScore;
    private Button btnOption1, btnOption2, btnOption3, btnOption4;
    private QuizQuestion currentQuiz;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        textQuestion = view.findViewById(R.id.textQuestion);
        textProgress = view.findViewById(R.id.textProgress);
        textScore = view.findViewById(R.id.textScore);

        btnOption1 = view.findViewById(R.id.btnOption1);
        btnOption2 = view.findViewById(R.id.btnOption2);
        btnOption3 = view.findViewById(R.id.btnOption3);
        btnOption4 = view.findViewById(R.id.btnOption4);

        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        loadNextQuestion();

        return view;
    }
    private void loadNextQuestion(){
        quizViewModel.getQuestion().observe(getViewLifecycleOwner(),question -> {
            if (question!=null){
                textQuestion.setText("Huruf apakah ini : " + question.character);
                textProgress.setText("Soal : " + quizViewModel.getCurentQuestion()+"/"+quizViewModel.getTotalQuestion());
                textScore.setText("Skor : " + quizViewModel.getScore());

                quizViewModel.getOptions(question.id).observe(getViewLifecycleOwner(), options ->{
                    if (options!=null){
                        List<Card> alloptions = new ArrayList<>(options);
                        alloptions.add(question);
                        while (alloptions.size() < 4){
                            alloptions.add(question);
                        }
                        Collections.shuffle(alloptions);
                        btnOption1.setText(alloptions.get(0).romaji);
                        btnOption2.setText(alloptions.get(1).romaji);
                        btnOption3.setText(alloptions.get(2).romaji);
                        btnOption4.setText(alloptions.get(3).romaji);

                        setAnswerListener(btnOption1, alloptions.get(0),question);
                        setAnswerListener(btnOption2, alloptions.get(1),question);
                        setAnswerListener(btnOption3, alloptions.get(2),question);
                        setAnswerListener(btnOption4, alloptions.get(3),question);
                    }
                });
            }
        });
    }

    private void setAnswerListener(Button button, Card option, Card correct) {
        button.setOnClickListener(v -> {
            if (option.id == correct.id){
                Toast.makeText(getContext(), "Jawaban Benar", Toast.LENGTH_SHORT).show();
                quizViewModel.increseScore();
            }else {
                Toast.makeText(getContext(), "Salah, Jawabannya adalah : "+correct.romaji, Toast.LENGTH_SHORT).show();
            }
            quizViewModel.nextQuestion();
            if (quizViewModel.isQuizFinished()){
                showResult();
            }else {
                loadNextQuestion();
            }
        });
    }

    private void showResult(){
        new AlertDialog.Builder(getContext())
                .setTitle("Quiz Selesai") .setMessage("Skor Anda : "+quizViewModel.getScore()+"/"+quizViewModel.getTotalQuestion())
                .setPositiveButton("Main Lagi", (dialogInterface, i) -> {
                    quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);
                    quizViewModel.reset();
                    loadNextQuestion();
                })
                .setNegativeButton("Tutup",null) .show();
    }
}