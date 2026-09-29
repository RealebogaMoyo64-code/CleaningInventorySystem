package dao;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Supplier;
import utils.DemoData;

/**
 * Handles all database operations related to suppliers.
 *
 * @author Sean
 */
public class SupplierDAO
{
    public boolean addSupplier(Supplier supplier)
    {
        if (nameExists(supplier.getName()))
        {
            System.out.println("A supplier with that name already exists.");
            return false;
        }

        String sql = "INSERT INTO suppliers (name, contact_person, phone, email, address) VALUES (?, ?, ?, ?, ?)";

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getContactPerson());
            stmt.setString(3, supplier.getPhone());
            stmt.setString(4, supplier.getEmail());
            stmt.setString(5, supplier.getAddress());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    } //addSupplier

    public List<Supplier> getAllSuppliers()
    {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers ORDER BY name";

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            return DemoData.suppliers();
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                suppliers.add(mapRow(rs));
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return suppliers;
    } //getAllSuppliers

    public List<Supplier> searchSuppliers(String keyword)
    {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers WHERE name ILIKE ? OR contact_person ILIKE ? ORDER BY name";

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            for (Supplier supplier : DemoData.suppliers())
            {
                if (supplier.getName().toLowerCase().contains(keyword.toLowerCase())
                        || supplier.getContactPerson().toLowerCase().contains(keyword.toLowerCase()))
                {
                    suppliers.add(supplier);
                }
            }
            return suppliers;
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
                    suppliers.add(mapRow(rs));
                }
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return suppliers;
    } //searchSuppliers

    public boolean updateSupplier(Supplier supplier)
    {
        String sql = "UPDATE suppliers SET name = ?, contact_person = ?, phone = ?, email = ?, address = ? WHERE id = ?";

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            return false;
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getContactPerson());
            stmt.setString(3, supplier.getPhone());
            stmt.setString(4, supplier.getEmail());
            stmt.setString(5, supplier.getAddress());
            stmt.setInt(6, supplier.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    } //updateSupplier

    public boolean deleteSupplier(int id)
    {
        String sql = "DELETE FROM suppliers WHERE id = ?";

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
    } //deleteSupplier

    public boolean nameExists(String name)
    {
        String sql = "SELECT * FROM suppliers WHERE name = ?";

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

    private Supplier mapRow(ResultSet rs) throws SQLException
    {
        Supplier supplier = new Supplier();
        supplier.setId(rs.getInt("id"));
        supplier.setName(rs.getString("name"));
        supplier.setContactPerson(rs.getString("contact_person"));
        supplier.setPhone(rs.getString("phone"));
        supplier.setEmail(rs.getString("email"));
        supplier.setAddress(rs.getString("address"));
        return supplier;
    } //mapRow
} //SupplierDAO
