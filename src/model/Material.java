package model;

/**
 * Represents a cleaning material held in inventory.
 *
 * Stores the stock level and reorder threshold used to trigger
 * low-stock alerts on the Dashboard.
 *
 * @author Jordann
 */
public class Material
{
    private int id;
    private String name;
    private int quantity;
    private String unit;
    private int reorderLevel;

    // ==========
    // Constructors
    // ==========

    public Material()
    {
    }

    public Material(String name, int quantity, String unit, int reorderLevel)
    {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.reorderLevel = reorderLevel;
    }

    // ==============
    // Getters and Setters
    // ==============

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public int getReorderLevel()
    {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel)
    {
        this.reorderLevel = reorderLevel;
    }

    // Returns true when stock has fallen to or below the reorder threshold.
    public boolean isLowStock()
    {
        return quantity <= reorderLevel;
    }

    @Override
    public String toString()
    {
        return "Material{" + "id=" + id + ", name='" + name + '\'' + ", quantity=" + quantity
                + ", unit='" + unit + '\'' + ", reorderLevel=" + reorderLevel + '}';
    }
} //Material
