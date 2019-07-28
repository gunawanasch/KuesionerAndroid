package com.gunawan.kuesioner.model;

import com.google.gson.annotations.SerializedName;

public class Kuesioner {
    @SerializedName("id_title")
    private int idTitle;

    @SerializedName("title")
    private String title;

    @SerializedName("id_question")
    private int idQuestion;

    @SerializedName("question")
    private String question;

    private int selectedRadioButtonId;

    private String customTag;

    public int getIdTitle() {
        return idTitle;
    }

    public void setIdTitle(int idTitle) {
        this.idTitle = idTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getSelectedRadioButtonId() {
        return selectedRadioButtonId;
    }

    public void setSelectedRadioButtonId(int selectedRadioButtonId) {
        this.selectedRadioButtonId = selectedRadioButtonId;
    }

    public String getCustomTag() {
        return customTag;
    }

    public void setCustomTag(String customTag) {
        this.customTag = customTag;
    }
}
