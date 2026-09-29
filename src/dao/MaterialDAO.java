package dao;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Material;
import utils.DemoData;

/**
 * Handles all database operations related to cleaning materials.
 *
 * Provides CRUD operations, name-based search/filtering, and a
 * low-stock lookup used by the Dashboard.
 *
 * @author Sean
 */
public class MaterialDAO
{
    // =========
    // Create
    // =========

    // Adds a new material. Returns false (without throwing) if the name already exists,
    // so the UI can show a friendly message instead of a raw SQL error.
    public boolean addMaterial(Material material)
    {
        if (nameExists(material.getName()))
        {
            System.out.println("A material with that name already exists.");
            return false;
        }

        String sql = "INSERT INTO materials (name, quantity, unit, reorder_level) VALUES (?, ?, ?, ?)";

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, material.getName());
            stmt.setInt(2, material.getQuantity());
            stmt.setString(3, material.getUnit());
            stmt.setInt(4, material.getReorderLevel());

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    } //addMaterial

    // =========
    // Read
    // =========

    // Returns every material, ordered alphabetically.
    public List<Material> getAllMaterials()
    {
        List<Material> materials = new ArrayList<>();
        String sql = "SELECT * FROM materials ORDER BY name";

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            return DemoData.materials();
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                materials.add(mapRow(rs));
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return materials;
    } //getAllMaterials

    // Returns materials whose name contains the given keyword (case-insensitive).
    public List<Material> searchMaterials(String keyword)
    {
        List<Material> materials = new ArrayList<>();
        String sql = "SELECT * FROM materials WHERE name ILIKE ? ORDER BY name";

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            for (Material material : DemoData.materials())
            {
                if (material.getName().toLowerCase().contains(keyword.toLowerCase()))
                {
                    materials.add(material);
                }
            }
            return materials;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    materials.add(mapRow(rs));
                }
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return materials;
    } //searchMaterials

    // Returns materials at or below their reorder level, used for Dashboard alerts.
    public List<Material> getLowStockMaterials()
    {
        List<Material> materials = new ArrayList<>();
        String sql = "SELECT * FROM materials WHERE quantity <= reorder_level ORDER BY name";

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            for (Material material : DemoData.materials())
            {
                if (material.isLowStock())
                {
                    materials.add(material);
                }
            }
            return materials;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                materials.add(mapRow(rs));
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return materials;
    } //getLowStockMaterials

    // =========
    // Update
    // =========

    public boolean updateMaterial(Material material)
    {
        String sql = "UPDATE materials SET name = ?, quantity = ?, unit = ?, reorder_level = ? WHERE id = ?";

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, material.getName());
            stmt.setInt(2, material.getQuantity());
            stmt.setString(3, material.getUnit());
            stmt.setInt(4, material.getReorderLevel());
            stmt.setInt(5, material.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    } //updateMaterial

    // =========
    // Delete
    // =========

    public boolean deleteMaterial(int id)
    {
        String sql = "DELETE FROM materials WHERE id = ?";

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
    } //deleteMaterial

    // =========
    // Validation
    // =========

    public boolean nameExists(String name)
    {
        String sql = "SELECT * FROM materials WHERE name = ?";

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery())
            {
                return rs.next();
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    } //nameExists

    // =========
    // Helpers
    // =========

    private Material mapRow(ResultSet rs) throws SQLException
    {
        Material material = new Material();
        material.setId(rs.getInt("id"));
        material.setName(rs.getString("name"));
        material.setQuantity(rs.getInt("quantity"));
        material.setUnit(rs.getString("unit"));
        material.setReorderLevel(rs.getInt("reorder_level"));
        return material;
    } //mapRow
} //MaterialDAO
