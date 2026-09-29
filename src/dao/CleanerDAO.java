package dao;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Cleaner;
import utils.DemoData;

/**
 * Handles all database operations related to cleaners.
 *
 * @author Sean
 */
public class CleanerDAO
{
    public boolean addCleaner(Cleaner cleaner)
    {
        String sql = "INSERT INTO cleaners (name, department, contact_number) VALUES (?, ?, ?)";

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, cleaner.getName());
            stmt.setString(2, cleaner.getDepartment());
            stmt.setString(3, cleaner.getContactNumber());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    } //addCleaner

    public List<Cleaner> getAllCleaners()
    {
        List<Cleaner> cleaners = new ArrayList<>();
        String sql = "SELECT * FROM cleaners ORDER BY name";

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            return DemoData.cleaners();
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                cleaners.add(mapRow(rs));
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return cleaners;
    } //getAllCleaners

    public List<Cleaner> searchCleaners(String keyword)
    {
        List<Cleaner> cleaners = new ArrayList<>();
        String sql = "SELECT * FROM cleaners WHERE name ILIKE ? OR department ILIKE ? ORDER BY name";

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            for (Cleaner cleaner : DemoData.cleaners())
            {
                if (cleaner.getName().toLowerCase().contains(keyword.toLowerCase())
                        || cleaner.getDepartment().toLowerCase().contains(keyword.toLowerCase()))
                {
                    cleaners.add(cleaner);
                }
            }
            return cleaners;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    cleaners.add(mapRow(rs));
                }
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return cleaners;
    } //searchCleaners

    public boolean updateCleaner(Cleaner cleaner)
    {
        String sql = "UPDATE cleaners SET name = ?, department = ?, contact_number = ? WHERE id = ?";

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, cleaner.getName());
            stmt.setString(2, cleaner.getDepartment());
            stmt.setString(3, cleaner.getContactNumber());
            stmt.setInt(4, cleaner.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    } //updateCleaner

    public boolean deleteCleaner(int id)
    {
        String sql = "DELETE FROM cleaners WHERE id = ?";

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    } //deleteCleaner

    private Cleaner mapRow(ResultSet rs) throws SQLException
    {
        Cleaner cleaner = new Cleaner();
        cleaner.setId(rs.getInt("id"));
        cleaner.setName(rs.getString("name"));
        cleaner.setDepartment(rs.getString("department"));
        cleaner.setContactNumber(rs.getString("contact_number"));
        return cleaner;
    } //mapRow
} //CleanerDAO
