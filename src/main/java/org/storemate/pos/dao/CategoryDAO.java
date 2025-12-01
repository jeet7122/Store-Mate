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

/**
 * Data Access Object (DAO) responsible for performing CRUD operations
 * related to the {@link Category} entity.
 *
 * This class interacts with the CATEGORY table, allowing:
 * <ul>
 *     <li>Fetching all categories</li>
 *     <li>Searching categories by keyword</li>
 *     <li>Retrieving category by ID</li>
 *     <li>Inserting new categories</li>
 *     <li>Updating existing categories</li>
 *     <li>Deleting categories</li>
 * </ul>
 *
 * The DAO uses JDBC prepared statements to prevent SQL injection and
 * maintains a single connection instance provided by {@link DatabaseConnector}.
 *
 * @author Billen
 * @version 1.0
 */
public class CategoryDAO {
    private final Connection conn;

    /**
     * Default constructor creates a DAO instance with an active database connection.
     * <p>
     * The connection is obtained from {@link DatabaseConnector#getConnection()}.
     */
    public CategoryDAO() {
        conn = DatabaseConnector.getConnection();
    }

    // GET ALL CATEGORIES
    /**
     * Retrieves all categories from the CATEGORY table.
     *
     * @return a list of all {@link Category} records.
     *         Returns an empty list if no records or an error occurs.
     */
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
    /**
     * Performs search on categories based on keyword in name or description.
     *
     * @param keyword text fragment used for LIKE query matching.
     * @return a list of categories matching the keyword criteria.
     *         Returns an empty list when no matches or an error occurs.
     */
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
    /**
     * Retrieves a single category record by its ID.
     *
     * @param categoryId unique identifier of the category.
     * @return {@link Category} object if found, otherwise null.
     */
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
    /**
     * Inserts a new category record into the database.
     *
     * @param category the {@link Category} object containing data to insert.
     * @return true if category was successfully inserted, false otherwise.
     */
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

    /**
     * Updates an existing category record.
     *
     * @param category updated {@link Category} object containing modified fields.
     * @return true if the update succeeded, false if no row was affected or an error occurred.
     */
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
    /**
     * Deletes a category using its ID.
     *
     * @param categoryId database ID of the category to remove.
     * @return true if deletion succeeded, false if failed or the record didn't exist.
     */
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

    /**
     * Utility mapper method that converts a {@link ResultSet} row
     * into a fully initialized {@link Category} model object.
     *
     * @param rs active SQL result set pointer
     * @return {@link Category} instance
     * @throws SQLException if column read fails or data type mismatch occurs
     */
    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setCategory_id(rs.getInt(CATEGORY_COLUMN_ID));
        category.setCategory_name(rs.getString(CATEGORY_COLUMN_NAME));
        category.setCategory_description(rs.getString(CATEGORY_COLUMN_DESCRIPTION));
        category.setStatus(Status.valueOf(rs.getString(CATEGORY_COLUMN_STATUS)));
        return category;
    }
}
