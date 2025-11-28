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

    // GET ALL CATEGORIES
    public List<Category> getAllCategories() {
        String sql = "SELECT * FROM " + CATEGORY_TABLE_NAME;
        List<Category> categories = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Category category = mapResultSetToCategory(rs);
                categories.add(category);
            }
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION (getAllCategories): " + e.getMessage());
        }
        return categories;
    }

    // SEARCH CATEGORIES BY KEYWORD
    public List<Category> searchCategories(String keyword) {
        String sql = "SELECT * FROM " + CATEGORY_TABLE_NAME +
                " WHERE " + CATEGORY_COLUMN_NAME + " LIKE ? OR " +
                CATEGORY_COLUMN_DESCRIPTION + " LIKE ?";
        List<Category> categories = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION (searchCategories): " + e.getMessage());
        }
        return categories;
    }

    // GET CATEGORY BY ID
    public Category getCategoryById(int categoryId) {
        String sql = "SELECT * FROM " + CATEGORY_TABLE_NAME + " WHERE " + CATEGORY_COLUMN_ID + " = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToCategory(rs);
            }
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION (getCategoryById): " + e.getMessage());
        }
        return null;
    }

    // Add Category to the database
    public boolean addCategory(Category category) {
        String sql = "INSERT INTO " + CATEGORY_TABLE_NAME +
                " (" + CATEGORY_COLUMN_NAME + ", " + CATEGORY_COLUMN_DESCRIPTION + ", " + CATEGORY_COLUMN_STATUS + ") " +
                "VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getCategory_name());
            stmt.setString(2, category.getCategory_description());
            stmt.setString(3, category.getStatus().name());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION (addCategory): " + e.getMessage());
            return false;
        }
    }

    // Update category to the database
    public boolean updateCategory(Category category) {
        String sql = "UPDATE " + CATEGORY_TABLE_NAME +
                " SET " + CATEGORY_COLUMN_NAME + " = ?, " +
                CATEGORY_COLUMN_DESCRIPTION + " = ?, " +
                CATEGORY_COLUMN_STATUS + " = ? " +
                "WHERE " + CATEGORY_COLUMN_ID + " = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getCategory_name());
            stmt.setString(2, category.getCategory_description());
            stmt.setString(3, category.getStatus().name());
            stmt.setInt(4, category.getCategory_id());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION (updateCategory): " + e.getMessage());
            return false;
        }
    }

    // DELETE CATEGORY
    public boolean deleteCategory(int categoryId) {
        String sql = "DELETE FROM " + CATEGORY_TABLE_NAME + " WHERE " + CATEGORY_COLUMN_ID + " = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION (deleteCategory): " + e.getMessage());
            return false;
        }
    }

    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setCategory_id(rs.getInt(CATEGORY_COLUMN_ID));
        category.setCategory_name(rs.getString(CATEGORY_COLUMN_NAME));
        category.setCategory_description(rs.getString(CATEGORY_COLUMN_DESCRIPTION));
        category.setStatus(Status.valueOf(rs.getString(CATEGORY_COLUMN_STATUS)));
        return category;
    }
}
