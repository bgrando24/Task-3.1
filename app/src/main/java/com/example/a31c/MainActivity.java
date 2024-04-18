package com.example.a31c;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

//    all possible questions
    private Question[] avaialableQuestions = {
            new Question("What keyword is used to declare a constant variable in Java?", new Answer[] {
                    new Answer("final", true),
                    new Answer("const", false),
                    new Answer("var", false),
            }),
        new Question("What does the operator '++' do in Java?", new Answer[] {
                new Answer("Increments a numerical value by 1", true),
                new Answer("Adds a number twice to another number", false),
                new Answer("Causes a kernel panic", false),
        }),
        new Question("What is the correct way to initialise an array in Java?", new Answer[] {
                new Answer("int[] arr = new int[5];", true),
                new Answer(" int arr = new int[5];", false),
                new Answer(" int[] arr = new int(5);", false),
        }),
        new Question("Which of the following statements is true about Java?", new Answer[] {
                new Answer("A class can implement multiple interfaces", true),
                new Answer("Interfaces can have constructors", false),
                new Answer("Interfaces can contain instance variables", false),
        }),
        new Question("What does the 'break' statement do in Java?", new Answer[] {
                new Answer("Exits code execution from a switch statement or loop", true),
                new Answer("Pauses code execution", false),
                new Answer("Skips the current iteration of a loop", false),
        }),
        new Question("Which lifecycle hook is called when an activity is no longer visible to the user?", new Answer[] {
                new Answer("onStop()", true),
                new Answer("onDestroy()", false),
                new Answer("onHide()", false),
        }),
        new Question("What development languages can Android apps be written in?", new Answer[] {
                new Answer("Java, Kotlin, C++", true),
                new Answer("HTML, CSS, JavaScript", false),
                new Answer("COBOL, x86 Assembly, Fortran", false),
        }),
        new Question("What is the purpose of XML layout files for Android apps?", new Answer[] {
                new Answer("To define the user interface layout", true),
                new Answer("To store application data in a structured format", false),
                new Answer("To track and define the application's dependencies", false),
        }),
        new Question("Which lifecycle method is called when an activity is first created?", new Answer[] {
                new Answer("onCreate()", true),
                new Answer("onActivityBirthday()", false),
                new Answer("onFirstDate()", false),
        }),
        new Question("What is a purpose of the Intent class in Android?", new Answer[] {
                new Answer("To pass data between components (such as activities)", true),
                new Answer("To send the app's intentions out to the universe", false),
                new Answer("To provide an abstraction layer over the built-in SQLite RDMS", false),
        }),
};

    @Override
    protected void onResume() {
        //        setup and set content
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        super.onResume();

        EditText userName = findViewById(R.id.nameEditText);
        Button startQuizButton = findViewById(R.id.startQuizButton);

        String existingPlayerName = QuizManager.getPlayerName();

        if (!existingPlayerName.isEmpty()) {
            userName.setText(existingPlayerName);
        }

        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameString = userName.getText().toString();

                if (userNameString.isEmpty()) {
                    userName.setError("Please enter your name");
                    return;
                }

                QuizManager.init(userNameString, avaialableQuestions);
                Intent quizViewIntent = new Intent(MainActivity.this, QuizQuestionsView.class);
                QuizManager.startQuiz();
                startActivity(quizViewIntent);
            }
        });
    }
}