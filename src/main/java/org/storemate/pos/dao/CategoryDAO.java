package org.storemate.pos.dao;

import org.storemate.pos.database.DatabaseConnector;
import org.storemate.pos.models.Category;
import org.storemate.pos.models.enums.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.storemate.pos.database.TableConstants.*;

public class CategoryDAO {
    private final Connection conn;

    public CategoryDAO() {
        conn = DatabaseConnector.getConnection();
    }


    public List<Category> getAllCategories() {
        String sql = "SELECT * FROM " + CATEGORY_TABLE_NAME;
        List<Category> categories = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Category category = new Category();
                category.setCategory_id(rs.getInt(CATEGORY_COLUMN_ID));
                category.setCategory_name(rs.getString(CATEGORY_COLUMN_NAME));
                category.setCategory_description(rs.getString(CATEGORY_COLUMN_DESCRIPTION));
                category.setStatus(Status.valueOf(rs.getString(CATEGORY_COLUMN_STATUS)));
                categories.add(category);
            }
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }
        return categories;
    }

    public List<Category> searchCategories(String keyword) {
        String sql = "SELECT * FROM " + CATEGORY_TABLE_NAME +
                " WHERE " + CATEGORY_COLUMN_NAME + " LIKE ? OR " +
                CATEGORY_COLUMN_DESCRIPTION + " LIKE ?";
        List<Category> categories = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Category category = new Category();
                category.setCategory_id(rs.getInt(CATEGORY_COLUMN_ID));
                category.setCategory_name(rs.getString(CATEGORY_COLUMN_NAME));
                category.setCategory_description(rs.getString(CATEGORY_COLUMN_DESCRIPTION));
                categories.add(category);
            }
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }
        return categories;
    }

    public Category getCategoryById(int categoryId) {
        String sql = "SELECT * FROM " + CATEGORY_TABLE_NAME + " WHERE " + CATEGORY_COLUMN_ID + " = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Category category = new Category();
                category.setCategory_id(rs.getInt(CATEGORY_COLUMN_ID));
                category.setCategory_name(rs.getString(CATEGORY_COLUMN_NAME));
                category.setCategory_description(rs.getString(CATEGORY_COLUMN_DESCRIPTION));
                category.setStatus(Status.valueOf(rs.getString(CATEGORY_COLUMN_STATUS)));
                return category;
            }
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION: " + e.getMessage());
        }
        return null;
    }
}
