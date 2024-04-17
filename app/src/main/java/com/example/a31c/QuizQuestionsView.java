package com.example.a31c;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.widget.ProgressBar;

public class QuizQuestionsView extends AppCompatActivity {
    private final int NEXT_QUESTION_DELAY = 5000;

    // Method to reset the background color of all buttons
    private void resetButtonColors(Button one, Button two, Button three) {
        one.setBackgroundColor(getResources().getColor(android.R.color.system_outline_light));
        two.setBackgroundColor(getResources().getColor(android.R.color.system_outline_light));
        three.setBackgroundColor(getResources().getColor(android.R.color.system_outline_light));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setup and set content
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz_questions_view);

        ProgressBar nextQuestionProgressBar = findViewById(R.id.nextQuestionProgressBar);   // used to show user progress until next question
        // Reset the progress bar
        nextQuestionProgressBar.setProgress(0);

//        Get question component and set text value
        final Question[] currentQuestion = {QuizManager.getCurrentQuestion()};
        TextView questionDescription = findViewById(R.id.questionDescriptionTextView);
        questionDescription.setText(currentQuestion[0].getDescription());

//        set answer button component references and set text values
        Button answer1Button = findViewById(R.id.answerButton1);
        Button answer2Button = findViewById(R.id.answerButton2);
        Button answer3Button = findViewById(R.id.answerButton3);

        answer1Button.setText(currentQuestion[0].getAnswers()[0].description);
        answer2Button.setText(currentQuestion[0].getAnswers()[1].description);
        answer3Button.setText(currentQuestion[0].getAnswers()[2].description);

//        store which answer has been selected
        final int[] selectedAnswerIndex = {-1};

// Set button click listeners
        answer1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("QuizQuestionsView", "Answer 1 clicked");
                resetButtonColors(answer1Button, answer2Button, answer3Button);
                answer1Button.setBackgroundColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));
                answer1Button.setTextColor(getColor(android.R.color.white));
                selectedAnswerIndex[0] = 0;
            }
        });
        answer2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("QuizQuestionsView", "Answer 2 clicked");
                resetButtonColors(answer1Button, answer2Button, answer3Button);
                answer2Button.setBackgroundColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));
                answer2Button.setTextColor(getColor(android.R.color.white));
                selectedAnswerIndex[0] = 1;
            }
        });
        answer3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("QuizQuestionsView", "Answer 3 clicked");
                resetButtonColors(answer1Button, answer2Button, answer3Button);
                answer3Button.setBackgroundColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));
                answer3Button.setTextColor(getColor(android.R.color.white));
                selectedAnswerIndex[0] = 2;
            }
        });

//        check if answer was right
        Button nextQuestionButton = findViewById(R.id.nextQuestionButton);
        nextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start the next question progress animation
                ObjectAnimator progressAnimator = ObjectAnimator.ofInt(nextQuestionProgressBar, "Next question progress", 0, 100);
                progressAnimator.setDuration(NEXT_QUESTION_DELAY);
                progressAnimator.start();

                if (selectedAnswerIndex[0] == -1) {
                    nextQuestionButton.setText("Please select an answer");
                    return;
                }
                boolean isCorrect = QuizManager.answerQuestion(selectedAnswerIndex[0]);
                int correctAnswerIndex = currentQuestion[0].getCorrectAnswerIndex();
                if (isCorrect) {
                    nextQuestionButton.setText("Correct!");
                } else {
                    nextQuestionButton.setText("Incorrect");
                }
                // Set the background color of the correct button to green and the selected incorrect button to red
                for (int i = 0; i < 3; i++) {
                    Button button;
                    switch (i) {
                        case 0:
                            button = answer1Button;
                            break;
                        case 1:
                            button = answer2Button;
                            break;
                        default:
                            button = answer3Button;
                            break;
                    }
                    if (i == correctAnswerIndex) {
                        button.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    } else if (i == selectedAnswerIndex[0]) {
                        button.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    }
                }
                nextQuestionButton.setEnabled(false);
                answer1Button.setEnabled(true);
                answer2Button.setEnabled(true);
                answer3Button.setEnabled(true);

                // Delay the execution of the next question
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Call next question
                        QuizManager.nextQuestion();
                        // Reset the states of the answer buttons
                        answer1Button.setBackgroundColor(getResources().getColor(android.R.color.system_outline_light));
                        answer2Button.setBackgroundColor(getResources().getColor(android.R.color.system_outline_light));
                        answer3Button.setBackgroundColor(getResources().getColor(android.R.color.system_outline_light));
                        // Change all the textView values appropriately
                        currentQuestion[0] = QuizManager.getCurrentQuestion();
                        questionDescription.setText(currentQuestion[0].getDescription());
                        answer1Button.setText(currentQuestion[0].getAnswers()[0].description);
                        answer2Button.setText(currentQuestion[0].getAnswers()[1].description);
                        answer3Button.setText(currentQuestion[0].getAnswers()[2].description);
                        nextQuestionButton.setText("Submit");
                        nextQuestionButton.setEnabled(true);
                        selectedAnswerIndex[0] = -1;
                    }
                }, NEXT_QUESTION_DELAY);
            }
        });
    }
}