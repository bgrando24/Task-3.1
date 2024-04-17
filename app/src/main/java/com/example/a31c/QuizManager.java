package com.example.a31c;

import android.util.Log;

import java.io.Console;
import java.util.ArrayList;
import java.util.Random;


/**
 * The com.example.a31c.QuizManager class is a state management class, responsible for the operation of the quiz
 * This class implements the singleton pattern - There should only ever be one instance of the com.example.a31c.QuizManager
 * State:
 *  - Player's name
 *  - Is the quiz running
 *  - Array of questions
 *  - Current question index
 *  - Answered questions
 *  - Quiz progress
 *  - Player's score
 */
public class QuizManager {
    private static final String DEBUG_TAG = "QuizManager";  // for debug logs
    private static QuizManager instance = null;
    private static String playerName;
    private static boolean isQuizRunning;
    private static Question[] allQuestions;
    private static int currentQuestionIndex;
    private static int answeredQuestions;
    private static int playerScore;
    private static ArrayList<Question> selectedQuestions;
    private static int numberOfQuizQuestions;

    // Private constructor to enforce singleton pattern
    private QuizManager(String name, Question[] questions) {
        playerName = name;
        isQuizRunning = false;
        allQuestions = questions;

        // Log initial state
        Log.d(DEBUG_TAG, "QuizManager initialized");
        Log.d(DEBUG_TAG, "Player Name: " + playerName);
        Log.d(DEBUG_TAG, "Is Quiz Running: " + isQuizRunning);
        Log.d(DEBUG_TAG, "Number of Questions: " + allQuestions.length);
    }

    // Gives access to the com.example.a31c.QuizManager instance
    public static void init(String playerName, Question[] allQuestions) {
        if (instance == null) {
            instance = new QuizManager(playerName, allQuestions);
        }
    }

    // Getters
    public static String getPlayerName() {
        return QuizManager.playerName;
    }
    public static boolean isQuizRunning() {
        return QuizManager.isQuizRunning;
    }
    public static Question getCurrentQuestion() {
        return QuizManager.selectedQuestions.get(currentQuestionIndex);
    }
    public static int getAnsweredQuestions() {
        return QuizManager.answeredQuestions;
    }
    public static int getPlayerScore() {
        return QuizManager.playerScore;
    }
    public static ArrayList<Question> getSelectedQuestions() {
        return QuizManager.selectedQuestions;
    }


//    Methods

//    start the quiz -> set quiz initial state
    static public void startQuiz() {
        Log.d(DEBUG_TAG, "startQuiz()");

        if (playerName == null || playerName.isBlank()) {
            throw new IllegalArgumentException("Player name cannot be empty or blank");
        }
        if (isQuizRunning) {
            Log.d(DEBUG_TAG, "[ERROR] Quiz is already running in QuizManager.startQuiz()");
            throw new IllegalStateException("Quiz is already running");
        }
        isQuizRunning = true;
        numberOfQuizQuestions = new Random().nextInt(8 - 4 + 1) + 4;    // (max questions - min questions + 1) + min questions
        selectedQuestions = selectQuestions(allQuestions);
        currentQuestionIndex = 0;
        answeredQuestions = 0;
        playerScore = 0;
    }

//    Update the quiz state and move to the next question
    static public void nextQuestion() {
        if (!isQuizRunning) {
            throw new IllegalStateException("Quiz is not running");
        }
        if (currentQuestionIndex == selectedQuestions.size() - 1) {
            endQuiz();
            return;
        }
        currentQuestionIndex++;
    }

//    end the quiz
    static public void endQuiz() {
        if (!isQuizRunning) {
            throw new IllegalStateException("Quiz is not running");
        }
        isQuizRunning = false;
    }

//    Randomly select the questions to use in the current quiz instance from allQuestions
    static private ArrayList<Question> selectQuestions(Question[] allQuestions) {
        ArrayList<Question> selectedQuestions = new ArrayList<>();
        for (int i = 0; i < numberOfQuizQuestions; i++) {
            int randomIndex = (int) (Math.random() * allQuestions.length);
            selectedQuestions.add(allQuestions[randomIndex]);
        }
        return selectedQuestions;
    }

//    Check if the answer is correct, update score and progress
    static public boolean answerQuestion(int answerIndex) {
        if (!isQuizRunning) {
            throw new IllegalStateException("Quiz is not running");
        }
        if (answerIndex < 0 || answerIndex > 2) {
            throw new IllegalArgumentException("Invalid answer index");
        }
        Question currentQuestion = selectedQuestions.get(currentQuestionIndex);
        Answer[] shuffledAnswers = currentQuestion.getShuffledAnswers();
        if (shuffledAnswers[answerIndex].isCorrect) {
            playerScore++;
            return true;
        }
        answeredQuestions++;
        return false;
    }


}
