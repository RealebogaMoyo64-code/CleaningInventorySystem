package model;

import java.sql.Timestamp;

/**
 * Represents a single stock issuance: materials given to a cleaner by a staff member.
 *
 * The name/username fields are populated from JOINs for display purposes only —
 * the actual foreign keys (materialId, cleanerId, issuedBy) are what's persisted.
 *
 * @author Jordann
 */
public class Issuance
{
    private int id;
    private int materialId;
    private String materialName;   // display only, from JOIN
    private int cleanerId;
    private String cleanerName;    // display only, from JOIN
    private int quantity;
    private int issuedBy;
    private String issuedByUsername; // display only, from JOIN
    private Timestamp issuedAt;

    public Issuance()
    {
    }

    public Issuance(int materialId, int cleanerId, int quantity, int issuedBy)
    {
        this.materialId = materialId;
        this.cleanerId = cleanerId;
        this.quantity = quantity;
        this.issuedBy = issuedBy;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getMaterialId()
    {
        return materialId;
    }

    public void setMaterialId(int materialId)
    {
        this.materialId = materialId;
    }

    public String getMaterialName()
    {
        return materialName;
    }

    public void setMaterialName(String materialName)
    {
        this.materialName = materialName;
    }

    public int getCleanerId()
    {
        return cleanerId;
    }

    public void setCleanerId(int cleanerId)
    {
        this.cleanerId = cleanerId;
    }

    public String getCleanerName()
    {
        return cleanerName;
    }

    public void setCleanerName(String cleanerName)
    {
        this.cleanerName = cleanerName;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public int getIssuedBy()
    {
        return issuedBy;
    }

    public void setIssuedBy(int issuedBy)
    {
        this.issuedBy = issuedBy;
    }

    public String getIssuedByUsername()
    {
        return issuedByUsername;
    }

    public void setIssuedByUsername(String issuedByUsername)
    {
        this.issuedByUsername = issuedByUsername;
    }

    public Timestamp getIssuedAt()
    {
        return issuedAt;
    }

    public void setIssuedAt(Timestamp issuedAt)
    {
        this.issuedAt = issuedAt;
    }
} //Issuance
