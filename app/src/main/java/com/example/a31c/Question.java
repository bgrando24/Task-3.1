package com.example.a31c;/*
* Question class represents a question that may be asked in the quiz
* Each Question object has a description and an array of Answer objects (representing the possible answers)
* */

import java.util.Random;

public class Question {
    private String description;
    private Answer[] answers;   // using array instead of ArrayList as length is fixed

    public Question(String description, Answer[] answers) {
        this.description = description;
        this.answers = answers;
    }

//    Getters
    public String getDescription() {
        return description;
    }
    public Answer[] getAnswers() {
        return answers;
    }

//    returns the index of the correct answer, otherwise -1
    public int getCorrectAnswerIndex() {
        for (int i = 0; i < answers.length; i++) {
            if (answers[i].isCorrect) {
                return i;
            }
        }
        return -1;
    }

//    Randomizes (shuffles) the order of the answers and returns the array
//    Uses the Fisher-Yates shuffle algo -> https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
    public void shuffleAnswers() {
        Answer[] shuffledAnswers = answers.clone();
        Random rand = new Random();
        for (int i = shuffledAnswers.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            // Simple swap
            Answer temp = shuffledAnswers[index];
            shuffledAnswers[index] = shuffledAnswers[i];
            shuffledAnswers[i] = temp;
        }
        answers = shuffledAnswers;
    }
}
