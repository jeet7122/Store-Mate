package org.storemate.pos.dto;

/**
 * Data Transfer Object (DTO) representing a security question along with its answer.
 * @author Khushi
 */
public class SecurityQuestionWithAnswerDTO {

    /** ID of the security question */
    private int questionID;

    /** Text of the security question */
    private String question;

    /** Answer provided for the security question */
    private String answer;

    /**
     * Constructs a SecurityQuestionWithAnswerDTO with specified question ID, text, and answer.
     *
     * @param questionID ID of the security question
     * @param question   Text of the security question
     * @param answer     Answer for the security question
     */
    public SecurityQuestionWithAnswerDTO(int questionID, String question, String answer) {
        this.questionID = questionID;
        this.question = question;
        this.answer = answer;
    }

    /**
     * @return ID of the security question
     */
    public int getQuestionID() {
        return questionID;
    }

    /**
     * Sets the ID of the security question.
     *
     * @param questionID Security question ID
     */
    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    /**
     * @return Text of the security question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Sets the text of the security question.
     *
     * @param question Security question text
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * @return Answer provided for the security question
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Sets the answer for the security question.
     *
     * @param answer Answer text
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * Returns a string representation of the SecurityQuestionWithAnswerDTO.
     *
     * @return String describing the question ID, text, and answer
     */
    @Override
    public String toString() {
        return "SecurityQuestionWithAnswerDTO{" +
                "questionID=" + questionID +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
