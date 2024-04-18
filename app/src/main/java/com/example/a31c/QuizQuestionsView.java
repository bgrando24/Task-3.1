package com.example.a31c;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.widget.ProgressBar;

import org.w3c.dom.Text;

import java.util.Arrays;

public class QuizQuestionsView extends AppCompatActivity {
    private final int NEXT_QUESTION_DELAY = 3000;
    private final String DEBUG_TAG = "QuizQuestions";

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

//        setup basic UI values

//        player name
        TextView playerNameTextView = findViewById(R.id.welcomePlayerNameTextView);
        playerNameTextView.setText("Welcome " + QuizManager.getPlayerName() + "!");

//        question number
        TextView questionNumberTextView = findViewById(R.id.questionNumberTextView);    // note after this, number is updated in the next question button onClick
        questionNumberTextView.setText(String.valueOf(QuizManager.getCurrentQuestionIndex() + 1));
        TextView totalQuestionsTextView = findViewById(R.id.totalQuestionNumberTextView);
        totalQuestionsTextView.setText(String.valueOf(QuizManager.getNumberOfQuizQuestions()));

//        progress bars
        ProgressBar quizProgressBar = findViewById(R.id.quizProgressBar);   // used to show user progress through quiz
        ProgressBar nextQuestionProgressBar = findViewById(R.id.nextQuestionProgressBar);   // used to show user progress until next question
        // Reset the progress bars
        nextQuestionProgressBar.setProgress(0);
        quizProgressBar.setProgress(0);

//        Get question component and set text value
        Question[] currentQuestion = {QuizManager.getCurrentQuestion()};
        TextView questionDescription = findViewById(R.id.questionDescriptionTextView);
        questionDescription.setText(currentQuestion[0].getDescription());

//        answer button component references and set text values
        Button answer1Button = findViewById(R.id.answerButton1);
        Button answer2Button = findViewById(R.id.answerButton2);
        Button answer3Button = findViewById(R.id.answerButton3);

//        Get shuffled set of answers for current question
        QuizManager.getCurrentQuestion().shuffleAnswers();
        final Answer[][] currentAnswers = {QuizManager.getCurrentQuestion().getAnswers()};

        answer1Button.setText(currentAnswers[0][0].description);
        answer2Button.setText(currentAnswers[0][1].description);
        answer3Button.setText(currentAnswers[0][2].description);

//        store which answer has been selected
        final int[] selectedAnswerIndex = {-1};

// Set button click listeners
        answer1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(DEBUG_TAG, "Answer 1 clicked");
                resetButtonColors(answer1Button, answer2Button, answer3Button);
                answer1Button.setBackgroundColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));
                selectedAnswerIndex[0] = 0;
            }
        });
        answer2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG_TAG", "Answer 2 clicked");
                resetButtonColors(answer1Button, answer2Button, answer3Button);
                answer2Button.setBackgroundColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));
                selectedAnswerIndex[0] = 1;
            }
        });
        answer3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG_TAG", "Answer 3 clicked");
                resetButtonColors(answer1Button, answer2Button, answer3Button);
                answer3Button.setBackgroundColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));
                selectedAnswerIndex[0] = 2;
            }
        });

//        check if answer was right
        Button nextQuestionButton = findViewById(R.id.nextQuestionButton);
        nextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Update the quiz progress bar
                int progress = (QuizManager.getCurrentQuestionIndex() + 1) * 100 / QuizManager.getNumberOfQuizQuestions();
                quizProgressBar.setProgress(progress);

                // Start the next question progress animation
                ObjectAnimator progressAnimator = ObjectAnimator.ofInt(nextQuestionProgressBar, "progress", 0, 100);
                progressAnimator.setDuration(NEXT_QUESTION_DELAY);
                progressAnimator.start();

                // Check if an answer has been selected
                if (selectedAnswerIndex[0] == -1) {
                    nextQuestionButton.setText("Please select an answer");
                    return;
                }

                // Check if the answer is correct -> use the selected answer index to grab selected answer
                boolean isCorrect = QuizManager.answerQuestion(QuizManager.getCurrentQuestion().getAnswers()[selectedAnswerIndex[0]]);

                // Compare the index of the selected answer with the index of the correct answer in the shuffled array
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
                    if (i == QuizManager.getCurrentQuestion().getCorrectAnswerIndex()) {
                        button.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    } else if (i == selectedAnswerIndex[0]) {
                        button.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    }
                }
                // Disable next question button and enable answer buttons
                nextQuestionButton.setEnabled(false);
                answer1Button.setEnabled(true);
                answer2Button.setEnabled(true);
                answer3Button.setEnabled(true);

                // Delay next question by value of 'NEXT_QUESTION_DELAY'
                new Handler().postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                    @Override
                    public void run() {

                        //        check if quiz should finish -> if yes, navigate to finish screen
                        if (QuizManager.getCurrentQuestionIndex() + 1 == QuizManager.getNumberOfQuizQuestions()) {
                            Log.d(DEBUG_TAG, "Quiz should end");
//            navigate to quiz finish view
                            Intent quizFinishView = new Intent(QuizQuestionsView.this, QuizEndView.class);
                            startActivity(quizFinishView);
                        }

                        // Fallback check if the quiz is still running
                        if (!QuizManager.getIsQuizRunning()) {
                            Log.d(DEBUG_TAG, "Quiz is not running - nextQuestionButton delay handler");
                            return;
                        }
                        // Call next question
                        QuizManager.nextQuestion();

                        // Update the current question and answers
                        currentQuestion[0] = QuizManager.getCurrentQuestion();
                        QuizManager.getCurrentQuestion().shuffleAnswers();
                        currentAnswers[0] = currentQuestion[0].getAnswers();

                        // Change all the textView values appropriately
                        questionDescription.setText(currentQuestion[0].getDescription());
                        answer1Button.setText(currentAnswers[0][0].description);
                        answer2Button.setText(currentAnswers[0][1].description);
                        answer3Button.setText(currentAnswers[0][2].description);

                        // Update the question number
                        questionNumberTextView.setText(String.valueOf(QuizManager.getCurrentQuestionIndex() + 1));
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