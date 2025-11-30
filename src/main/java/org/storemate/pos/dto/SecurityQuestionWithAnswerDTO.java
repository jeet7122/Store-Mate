package org.storemate.pos.dto;

public class SecurityQuestionWithAnswerDTO {
    private int questionID;
    private String question;
    private String answer;

    @Override
    public String toString() {
        return "SecurityQuestionWithAnswerDTO{" +
                "questionID=" + questionID +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }

    public SecurityQuestionWithAnswerDTO(int questionID, String question, String answer) {
        this.questionID = questionID;
        this.question = question;
        this.answer = answer;
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
