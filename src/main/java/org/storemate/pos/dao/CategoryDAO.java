package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.models.Category;
import org.storemate.pos.models.enums.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.storemate.pos.database.TableConstants.*;

public class CategoryDAO {

    private final Connection conn;

    public CategoryDAO() {
        conn = DatabaseConnector.getConnection();
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY name";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Category category = new Category();
                String statusSTR = rs.getString(CATEGORY_COLUMN_STATUS);
                Status status = Status.valueOf(statusSTR);
                category.setCategory_id(rs.getInt(CATEGORY_COLUMN_ID));
                category.setCategory_name(rs.getString(CATEGORY_COLUMN_NAME));
                category.setCategory_description(rs.getString(CATEGORY_COLUMN_DESCRIPTION));
                category.setStatus(status);
                categories.add(category);
            }
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }
        return categories;
    }
}
