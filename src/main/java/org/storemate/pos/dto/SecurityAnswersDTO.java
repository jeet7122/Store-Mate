package org.storemate.pos.dto;

public class SecurityAnswersDTO {
    private int employee_id;
    private int question_id;
    private int answer_id;
    private String answer;

    public SecurityAnswersDTO(int question_id, String answer){
        this.answer = answer;
        this.question_id = question_id;
    }

    public SecurityAnswersDTO() {
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(int answer_id) {
        this.answer_id = answer_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
