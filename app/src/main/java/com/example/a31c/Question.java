package com.example.a31c;/*
* The com.example.a31c.Question class represents a question that may be asked in the quiz
* Each com.example.a31c.Question object has a description and an array of com.example.a31c.Answer objects (representing the possible answers)
* */

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
    public Answer[] getShuffledAnswers() {
        Answer[] shuffledAnswers = answers.clone();
        for (int i = 0; i < shuffledAnswers.length; i++) {
            int randomIndex = (int) (Math.random() * shuffledAnswers.length);
            Answer temp = shuffledAnswers[i];
            shuffledAnswers[i] = shuffledAnswers[randomIndex];
            shuffledAnswers[randomIndex] = temp;
        }
        return shuffledAnswers;
    }
}
