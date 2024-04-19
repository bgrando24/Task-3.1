package com.example.a31c;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class QuizEndView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz_end_view);

//        Set 'congratulation' message
        TextView quizFinishedTextView = findViewById(R.id.quizFinishedTextView);
        quizFinishedTextView.setText("Congratulations " + QuizManager.getPlayerName() + "!");

//        Set player's score text view
        TextView playerScoreTextView = findViewById(R.id.quizScoreTextView);
        playerScoreTextView.setText("Your score: " + QuizManager.getPlayerScore() + "/" + QuizManager.getNumberOfQuizQuestions());

//        Set 'play again' button
        Button startQuizAgainButton = findViewById(R.id.startQuizAgainButton);
        startQuizAgainButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

//        Set 'finish' button
        Button quizFinishButton = findViewById(R.id.quizFinishButton);
        quizFinishButton.setOnClickListener(v -> {
            // Exit app
            finishAffinity();
        });
    }
}