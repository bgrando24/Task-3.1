package com.example.a31c;//The com.example.a31c.Answer class is used in the com.example.a31c.Question class to store the possible answers to a question, and a flag of whether the answer is correct or not

public class Answer {
    public String description;
    public boolean isCorrect;

    public Answer(String description, boolean isCorrect) {
        this.description = description;
        this.isCorrect = isCorrect;
    }
}
