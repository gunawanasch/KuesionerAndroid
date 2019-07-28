package com.gunawan.kuesioner.model;

public class Answer {
    private int idTitle;
    private int idQuestion;
    private String value;

    public Answer(int idTitle, int idQuestion, String value) {
        this.idTitle = idTitle;
        this.idQuestion = idQuestion;
        this.value = value;
    }

    public int getIdTitle() {
        return idTitle;
    }

    public void setIdTitle(int idTitle) {
        this.idTitle = idTitle;
    }

    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
