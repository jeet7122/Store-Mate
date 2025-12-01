package org.storemate.pos.dto;

/**
 * Data Transfer Object (DTO) representing a security question.
 * @author Khushi
 */
public class SecurityQuestionsDTO {

    /** ID of the security question */
    private int questionID;

    /** Text of the security question */
    private String question;

    /**
     * Default constructor.
     */
    public SecurityQuestionsDTO() {
    }

    /**
     * Constructs a SecurityQuestionsDTO with a question text.
     *
     * @param question Text of the security question
     */
    public SecurityQuestionsDTO(String question) {
        this.question = question;
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
     * Returns a string representation of the SecurityQuestionsDTO.
     *
     * @return String describing the question ID and text
     */
    @Override
    public String toString() {
        return "SecurityQuestionsDTO{" +
                "questionID=" + questionID +
                ", question='" + question + '\'' +
                '}';
    }
}
