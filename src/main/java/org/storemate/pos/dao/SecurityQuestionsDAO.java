package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.dto.SecurityQuestionsDTO;

import static org.storemate.pos.database.TableConstants.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SecurityQuestionsDAO {

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
}
