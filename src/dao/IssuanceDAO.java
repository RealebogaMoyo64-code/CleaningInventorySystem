package dao;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Issuance;
import utils.DemoData;
import utils.InsufficientStockException;

/**
 * Handles stock issuance: the core business-logic module of the system.
 *
 * issueMaterial() runs as a single database transaction so the stock check,
 * the stock deduction, and the history record either all succeed together or
 * all roll back together — this prevents a race where two staff members issue
 * the last few units at the same time and stock goes negative.
 *
 * @author Sean
 */
public class IssuanceDAO
{
    // =========
    // Core business logic
    // =========

    /**
     * Issues stock to a cleaner.
     *
     * @throws InsufficientStockException if the requested quantity exceeds what's currently available.
     * @return true if the issuance succeeded, false on an unexpected database error.
     */
    public boolean issueMaterial(Issuance issuance) throws InsufficientStockException
    {
        String lockSql = "SELECT quantity FROM materials WHERE id = ? FOR UPDATE";
        String deductSql = "UPDATE materials SET quantity = quantity - ? WHERE id = ?";
        String insertSql = "INSERT INTO issuances (material_id, cleaner_id, quantity, issued_by) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try
        {
            conn = DatabaseConnection.getConnection();
            if (conn == null)
            {
                return false; // connection failed; DatabaseConnection already logged the cause
            }
            conn.setAutoCommit(false); // start transaction

            int availableQuantity;
            // Lock the material row so a second, concurrent issuance can't read stale stock.
            try (PreparedStatement lockStmt = conn.prepareStatement(lockSql))
            {
                lockStmt.setInt(1, issuance.getMaterialId());
                try (ResultSet rs = lockStmt.executeQuery())
                {
                    if (!rs.next())
                    {
                        conn.rollback();
                        return false; // material no longer exists
                    }
                    availableQuantity = rs.getInt("quantity");
                }
            }

            // Business rule: never allow issuing more than what's in stock.
            if (issuance.getQuantity() > availableQuantity)
            {
                conn.rollback();
                throw new InsufficientStockException(
                        "Only " + availableQuantity + " unit(s) available, but " + issuance.getQuantity() + " were requested.");
            }

            try (PreparedStatement deductStmt = conn.prepareStatement(deductSql))
            {
                deductStmt.setInt(1, issuance.getQuantity());
                deductStmt.setInt(2, issuance.getMaterialId());
                deductStmt.executeUpdate();
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql))
            {
                insertStmt.setInt(1, issuance.getMaterialId());
                insertStmt.setInt(2, issuance.getCleanerId());
                insertStmt.setInt(3, issuance.getQuantity());
                insertStmt.setInt(4, issuance.getIssuedBy());
                insertStmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (InsufficientStockException ex)
        {
            throw ex; // already rolled back above; let the UI show this specific message
        } catch (SQLException ex)
        {
            ex.printStackTrace();
            if (conn != null)
            {
                try { conn.rollback(); } catch (SQLException rollbackEx) { rollbackEx.printStackTrace(); }
            }
            return false;
        } finally
        {
            if (conn != null)
            {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    } //issueMaterial

    // =========
    // History / reporting queries
    // =========

    private static final String HISTORY_SELECT =
            "SELECT i.id, i.material_id, m.name AS material_name, i.cleaner_id, c.name AS cleaner_name, "
            + "i.quantity, i.issued_by, u.username AS issued_by_username, i.issued_at "
            + "FROM issuances i "
            + "JOIN materials m ON i.material_id = m.id "
            + "JOIN cleaners c ON i.cleaner_id = c.id "
            + "JOIN users u ON i.issued_by = u.id ";

    public List<Issuance> getAllIssuances()
    {
        List<Issuance> issuances = new ArrayList<>();
        String sql = HISTORY_SELECT + "ORDER BY i.issued_at DESC";

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            return DemoData.issuances();
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                issuances.add(mapRow(rs));
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return issuances;
    } //getAllIssuances

    public List<Issuance> getRecentIssuances(int limit)
    {
        List<Issuance> issuances = new ArrayList<>();
        String sql = HISTORY_SELECT + "ORDER BY i.issued_at DESC LIMIT ?";

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            return DemoData.issuances().subList(0, Math.min(limit, DemoData.issuances().size()));
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    issuances.add(mapRow(rs));
                }
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return issuances;
    } //getRecentIssuances

    public List<Issuance> searchIssuances(String keyword)
    {
        List<Issuance> issuances = new ArrayList<>();
        String sql = HISTORY_SELECT + "WHERE m.name ILIKE ? OR c.name ILIKE ? ORDER BY i.issued_at DESC";

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            for (Issuance issuance : DemoData.issuances())
            {
                if (issuance.getMaterialName().toLowerCase().contains(keyword.toLowerCase())
                        || issuance.getCleanerName().toLowerCase().contains(keyword.toLowerCase()))
                {
                    issuances.add(issuance);
                }
            }
            return issuances;
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
                    issuances.add(mapRow(rs));
                }
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return issuances;
    } //searchIssuances

    // Total quantity issued per material — used for the Material Usage Report.
    public List<Object[]> getMaterialUsageSummary()
    {
        List<Object[]> summary = new ArrayList<>();
        String sql = "SELECT m.name, COALESCE(SUM(i.quantity), 0) AS total_issued, COUNT(i.id) AS times_issued "
                + "FROM materials m LEFT JOIN issuances i ON m.id = i.material_id "
                + "GROUP BY m.name ORDER BY total_issued DESC";

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            for (model.Material material : DemoData.materials())
            {
                int issued = material.getId() == 1 ? 6 : 0;
                summary.add(new Object[]{material.getName(), issued, issued > 0 ? 1 : 0});
            }
            return summary;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                summary.add(new Object[]{rs.getString("name"), rs.getInt("total_issued"), rs.getInt("times_issued")});
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return summary;
    } //getMaterialUsageSummary

    private Issuance mapRow(ResultSet rs) throws SQLException
    {
        Issuance issuance = new Issuance();
        issuance.setId(rs.getInt("id"));
        issuance.setMaterialId(rs.getInt("material_id"));
        issuance.setMaterialName(rs.getString("material_name"));
        issuance.setCleanerId(rs.getInt("cleaner_id"));
        issuance.setCleanerName(rs.getString("cleaner_name"));
        issuance.setQuantity(rs.getInt("quantity"));
        issuance.setIssuedBy(rs.getInt("issued_by"));
        issuance.setIssuedByUsername(rs.getString("issued_by_username"));
        issuance.setIssuedAt(rs.getTimestamp("issued_at"));
        return issuance;
    } //mapRow
} //IssuanceDAO
