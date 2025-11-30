package org.storemate.pos.dto;

public class SecurityQuestionsDTO {
    private int questionID;
    private String question;

    public SecurityQuestionsDTO() {
    }

    public SecurityQuestionsDTO(String question) {
        this.question = question;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "SecurityQuestionsDTO{" +
                "questionID=" + questionID +
                ", question='" + question + '\'' +
                '}';
    }
}
