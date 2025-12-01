package org.storemate.pos.dto;

/**
 * Data Transfer Object (DTO) representing an employee's answer to a security question.
 * @author Khushi
 */
public class SecurityAnswersDTO {

    /** ID of the employee */
    private int employee_id;

    /** ID of the security question */
    private int question_id;

    /** ID of the answer record */
    private int answer_id;

    /** The answer text */
    private String answer;

    /**
     * Default constructor.
     */
    public SecurityAnswersDTO() {
    }

    /**
     * Constructs a SecurityAnswersDTO with a question ID and answer text.
     *
     * @param question_id ID of the security question
     * @param answer      Text of the answer
     */
    public SecurityAnswersDTO(int question_id, String answer) {
        this.question_id = question_id;
        this.answer = answer;
    }

    /**
     * @return ID of the employee
     */
    public int getEmployee_id() {
        return employee_id;
    }

    /**
     * Sets the ID of the employee.
     *
     * @param employee_id Employee ID
     */
    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    /**
     * @return ID of the security question
     */
    public int getQuestion_id() {
        return question_id;
    }

    /**
     * Sets the ID of the security question.
     *
     * @param question_id Security question ID
     */
    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    /**
     * @return ID of the answer record
     */
    public int getAnswer_id() {
        return answer_id;
    }

    /**
     * Sets the ID of the answer record.
     *
     * @param answer_id Answer ID
     */
    public void setAnswer_id(int answer_id) {
        this.answer_id = answer_id;
    }

    /**
     * @return Text of the answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Sets the text of the answer.
     *
     * @param answer Answer text
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
