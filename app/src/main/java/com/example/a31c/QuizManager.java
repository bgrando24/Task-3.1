package com.example.a31c;

import android.util.Log;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;


/**
 * The QuizManager class is a state management class, responsible for the operation of the quiz
 * This class implements the singleton pattern - There should only ever be one instance of the QuizManager
 * State:
 *  - Quiz instance
 *  - Player's name
 *  - Is the quiz running
 *  - Array of all possible questions
 *  - Current question index
 *  - Player's score
 *  - Selected questions for the current quiz
 *  - Number of selected questions
 */
public class QuizManager {
    private static final String DEBUG_TAG = "QuizManager";
    private static QuizManager instance = null;
    private static String playerName = "";
    private static boolean isQuizRunning;
    private static Question[] allQuestions;
    private static int currentQuestionIndex;
    private static int playerScore;
    private static ArrayList<Question> selectedQuestions;
    private static int numberOfQuizQuestions;

    // Private constructor to enforce singleton pattern
    private QuizManager(String name, Question[] questions) {
        playerName = name;
        isQuizRunning = false;
        allQuestions = questions;
        Log.d(DEBUG_TAG, "QuizManager initialized");
        Log.d(DEBUG_TAG, "Player Name: " + playerName);
    }

    // Gives access to QuizManager instance
    public static void init(String playerName, Question[] allQuestions) {
        if (instance == null) {
            instance = new QuizManager(playerName, allQuestions);
        }
    }

    // Getters
    public static String getPlayerName() {
        return QuizManager.playerName;
    }
    public static boolean getIsQuizRunning() {
        return QuizManager.isQuizRunning;
    }
    public static Question getCurrentQuestion() { return QuizManager.selectedQuestions.get(currentQuestionIndex); }
    public static int getCurrentQuestionIndex() { return QuizManager.currentQuestionIndex; }
    public static int getNumberOfQuizQuestions() { return QuizManager.numberOfQuizQuestions; }
    public static int getPlayerScore() {
        return QuizManager.playerScore;
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
        numberOfQuizQuestions = new Random().nextInt(5) + 4; // a random number between 4 and 8 inclusive
//        numberOfQuizQuestions = 3;    // used in testing
        selectedQuestions = selectQuestions(allQuestions);
        currentQuestionIndex = 0;
        playerScore = 0;
        Log.d(DEBUG_TAG, "Is Quiz Running: " + isQuizRunning);
        Log.d(DEBUG_TAG, "Number of Questions: " + numberOfQuizQuestions);
    }

//    Update the quiz state and move to the next question
    static public void nextQuestion() {
        Log.d(DEBUG_TAG, "nextQuestion()");
        if (!isQuizRunning) {
            throw new IllegalStateException("Quiz is not running");
        }
        if (currentQuestionIndex == selectedQuestions.size() - 1) {
            Log.d(DEBUG_TAG, "Quiz should end, calling endQuiz()");
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
        Log.d(DEBUG_TAG, "endQuiz()");
        isQuizRunning = false;
    }

//    Randomly select the questions to use in the current quiz instance from allQuestions
//    HashSet used to help ensure no duplicate questions are selected
static private ArrayList<Question> selectQuestions(Question[] allQuestions) {
    ArrayList<Question> selectedQuestions = new ArrayList<>();
    HashSet<Integer> selectedIndices = new HashSet<>();

    while (selectedQuestions.size() < numberOfQuizQuestions) {
        int randomIndex = (int) (Math.random() * allQuestions.length);

        // If this index has not been selected before, add the question to selectedQuestions
        if (!selectedIndices.contains(randomIndex)) {
            selectedQuestions.add(allQuestions[randomIndex]);
            selectedIndices.add(randomIndex);
        }
    }

    return selectedQuestions;
}

//    Check if the answer is correct, update score and progress
    static public boolean answerQuestion(Answer selectedAnswer) {
        Log.d(DEBUG_TAG, "answerQuestion()");
       if (selectedAnswer.isCorrect) {
           playerScore++;
           return true;
       } else {
           return false;
       }
    }
}
