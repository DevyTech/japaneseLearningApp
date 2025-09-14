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

//observer hanya satu LiveData gabungan
        quizViewModel.getQuizLive().observe(getViewLifecycleOwner(), quiz -> {
            if (quiz==null) return;
            currentQuiz = quiz;

            textQuestion.setText(quiz.getQuestion());
            textProgress.setText(String.format("%d/%d", quizViewModel.getCurrentQuestion(), quizViewModel.getTotalQuestions()));
            textScore.setText(String.format("Score: %d", quizViewModel.getScore()));
            btnOption1.setText(quiz.getOptions().get(0));
            btnOption2.setText(quiz.getOptions().get(1));
            btnOption3.setText(quiz.getOptions().get(2));
            btnOption4.setText(quiz.getOptions().get(3));
        });

//        pasang listener sekali
        View.OnClickListener answerListener = v -> {
            if (currentQuiz == null) return;
            String selected = ((Button) v).getText().toString();
            if (selected.equals(currentQuiz.getCorrectAnswer())){
                quizViewModel.increaseScore();
//                Toast.makeText(getContext(), "Correct!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(), "Wrong!, Answer Is : "+currentQuiz.getCorrectAnswer(), Toast.LENGTH_SHORT).show();
            }
            if (quizViewModel.isFinished()){
                showResult();
            }else {
                quizViewModel.nextQuestion();
            }
        };
        btnOption1.setOnClickListener(answerListener);
        btnOption2.setOnClickListener(answerListener);
        btnOption3.setOnClickListener(answerListener);
        btnOption4.setOnClickListener(answerListener);
        return view;
    }

    private void showResult(){
        new AlertDialog.Builder(getContext())
                .setTitle("Quiz Selesai")
                .setMessage("Skor Anda : " + quizViewModel.getScore() + "/" + quizViewModel.getTotalQuestions())
                .setPositiveButton("Main Lagi", (d, w) -> {
                    quizViewModel.resetQuiz();
                })
                .setNegativeButton("Tutup", null)
                .show();
    }
}