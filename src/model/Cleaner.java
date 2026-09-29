package model;

/**
 * Represents a cleaner (staff member) who can be issued cleaning materials.
 *
 * @author Jordann
 */
public class Cleaner
{
    private int id;
    private String name;
    private String department;
    private String contactNumber;

    public Cleaner()
    {
    }

    public Cleaner(String name, String department, String contactNumber)
    {
        this.name = name;
        this.department = department;
        this.contactNumber = contactNumber;
    }

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

    public String getDepartment()
    {
        return department;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    public String getContactNumber()
    {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber)
    {
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString()
    {
        // Used directly in JComboBox dropdowns on the Issuance screen.
        return name + (department != null && !department.isEmpty() ? " (" + department + ")" : "");
    }
} //Cleaner
