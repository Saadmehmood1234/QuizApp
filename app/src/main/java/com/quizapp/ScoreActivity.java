package com.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.quizapp.databinding.ActivityScoreBinding;

import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {
    ActivityScoreBinding binding;
    private long timeTaken;
    private int totalQuestions,correctAnsw,wrongAnsw,skipQuestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        totalQuestions=getIntent().getIntExtra("total_questions",0);
        correctAnsw=getIntent().getIntExtra("correct",0);
        wrongAnsw=getIntent().getIntExtra("wrong",0);


        String remainingTime = String.format("%02d:%02d min",
                TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                TimeUnit.MILLISECONDS.toSeconds(timeTaken) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken)));

        binding.timeTaken.setText(remainingTime);

        skipQuestion=totalQuestions-(correctAnsw+wrongAnsw);
        binding.question.setText(totalQuestions+"");
        binding.txtcorect.setText(correctAnsw+"");
        binding.txtWrong.setText(wrongAnsw+"");
        binding.txtSkip.setText(skipQuestion+"");

        binding.score.setText(correctAnsw+"");

        binding.btnReAttempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ScoreActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


    }
}