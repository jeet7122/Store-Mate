package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.dto.SecurityAnswersDTO;
import org.storemate.pos.dto.SecurityQuestionWithAnswerDTO;
import org.storemate.pos.dto.SecurityQuestionsDTO;

import static org.storemate.pos.database.TableConstants.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for managing security questions and answers in the database.
 *
 * <p>This class provides methods to:
 * <ul>
 *     <li>Retrieve all security questions</li>
 *     <li>Fetch a user's security questions along with their answers</li>
 *     <li>Get the ID of a question based on its text</li>
 *     <li>Update a user's security questions and answers</li>
 * </ul>
 *
 * <p>All queries use JDBC and the {@link DatabaseConnector} for obtaining database connections.
 * @author Khushi
 */
public class SecurityQuestionsDAO {

    /**
     * Retrieves all security questions from the database.
     *
     * @return a list of {@link SecurityQuestionsDTO} objects containing question IDs and texts;
     *         returns an empty list if no questions are found or on SQL error.
     */
    public List<SecurityQuestionsDTO> getAllQuestions(){
        List<SecurityQuestionsDTO> questionList = new ArrayList<>();
        String sql = "SELECT * FROM " + SECURITY_QUESTIONS_TABLE_NAME + " ORDER BY " + SECURITY_QUESTIONS_COLUMN_ID + ";";
        try
                (
                        Connection connection = DatabaseConnector.getConnection();
                        PreparedStatement ps = connection.prepareStatement(sql)
                        )
        {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                SecurityQuestionsDTO result = new SecurityQuestionsDTO();
                result.setQuestionID(resultSet.getInt(SECURITY_QUESTIONS_COLUMN_ID));
                result.setQuestion(resultSet.getString(SECURITY_QUESTIONS_COLUMN_QUESTION));
                questionList.add(result);
            }
        }
        catch (SQLException exception){
            System.out.println("SQL EXCEPTION: " + exception.getMessage());
        }
        catch (Exception exception){
            System.out.println("EXCEPTION: " + exception.getMessage());
        }
        return questionList;

    }

    /**
     * Retrieves all security questions and corresponding answers for a specific employee.
     *
     * @param employeeID the ID of the employee
     * @return a list of {@link SecurityQuestionWithAnswerDTO} objects containing question IDs, texts, and answers
     *         returns an empty list if no questions are found or on SQL error
     */
    public List<SecurityQuestionWithAnswerDTO> getAllQuestionsForUser(int employeeID) {
        List<SecurityQuestionWithAnswerDTO> securityQuestionWithAnswerDTOList = new ArrayList<>();
        String query = "SELECT a.question_id, q.question, a.answer FROM "
                + EMPLOYEE_SECURITY_ANSWERS_TABLE_NAME + " a JOIN "
                + SECURITY_QUESTIONS_TABLE_NAME + " q ON a." + EMPLOYEE_SECURITY_ANSWERS_COLUMN_QUESTION_ID +
                "= q." + SECURITY_QUESTIONS_COLUMN_ID +
                " WHERE a." + EMPLOYEE_SECURITY_ANSWERS_COLUMN_EMPLOYEE_ID + " = ?";

        try
                (
                        Connection connection = DatabaseConnector.getConnection();
                        PreparedStatement ps = connection.prepareStatement(query)
                )
        {
            ps.setInt(1, employeeID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int qID = rs.getInt(EMPLOYEE_SECURITY_ANSWERS_COLUMN_QUESTION_ID);
                String questionText = rs.getString(SECURITY_QUESTIONS_COLUMN_QUESTION);
                String answerText = rs.getString(EMPLOYEE_SECURITY_ANSWERS_COLUMN_ANSWER);
                SecurityQuestionWithAnswerDTO dto = new SecurityQuestionWithAnswerDTO(qID, questionText, answerText);
                securityQuestionWithAnswerDTOList.add(dto);
            }

        }
        catch (SQLException e){
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }
        catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
        }
        return securityQuestionWithAnswerDTOList;
    }

    /**
     * Retrieves the ID of a security question based on its text.
     *
     * @param questionText the text of the security question
     * @return the question ID if found; returns -1 if not found or on SQL error.
     */
    public int getQuestionIdByText(String questionText) {
        String sql = "SELECT question_id FROM security_questions WHERE question = ?";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, questionText);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("question_id");
            }
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Updates the security questions and answers for a specific employee.
     *
     * <p>This method deletes existing answers for the employee and inserts the new ones
     * in a single transaction.
     *
     * @param employeeId the ID of the employee whose security answers are being updated
     * @param list a list of {@link SecurityAnswersDTO} containing question IDs and corresponding answers
     * @return true if the update was successful; false if there was an error
     */

    public boolean updateSecurityQuestions(int employeeId, List<SecurityAnswersDTO> list) {

        String deleteSQL = "DELETE FROM security_answers WHERE employee_id = ?";
        String insertSQL = "INSERT INTO security_answers (employee_id, question_id, answer) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false);

            // 1. Remove old answers
            try (PreparedStatement ps = conn.prepareStatement(deleteSQL)) {
                ps.setInt(1, employeeId);
                ps.executeUpdate();
            }

            // 2. Insert new answers
            try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                for (SecurityAnswersDTO dto : list) {
                    ps.setInt(1, employeeId);
                    ps.setInt(2, dto.getQuestion_id());
                    ps.setString(3, dto.getAnswer());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
            return false;
        }
    }

}
