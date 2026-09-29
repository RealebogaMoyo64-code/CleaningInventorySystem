package utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Cleaner;
import model.Issuance;
import model.Material;
import model.Supplier;

/** Read-only sample records used when the database is unavailable. */
public final class DemoData
{
    private DemoData() { }

    public static List<Material> materials()
    {
        return new ArrayList<>(List.of(
                material(1, "Disinfectant", 42, "bottles", 10),
                material(2, "Floor Cleaner", 8, "litres", 10),
                material(3, "Microfiber Cloths", 120, "pieces", 25),
                material(4, "Rubber Gloves", 18, "pairs", 20)));
    }

    public static List<Cleaner> cleaners()
    {
        return new ArrayList<>(List.of(
                cleaner(1, "Amina Patel", "Residence", "071 555 0101"),
                cleaner(2, "Thabo Mokoena", "Library", "071 555 0102"),
                cleaner(3, "Lerato Dlamini", "Science Block", "071 555 0103")));
    }

    public static List<Supplier> suppliers()
    {
        Supplier supplier = new Supplier();
        supplier.setId(1);
        supplier.setName("Campus Hygiene Supplies");
        supplier.setContactPerson("Nadia Jacobs");
        supplier.setPhone("011 555 0199");
        supplier.setEmail("orders@campushygiene.example");
        supplier.setAddress("12 Market Street");
        return new ArrayList<>(List.of(supplier));
    }

    public static List<Issuance> issuances()
    {
        Issuance issuance = new Issuance();
        issuance.setId(1);
        issuance.setMaterialId(1);
        issuance.setMaterialName("Disinfectant");
        issuance.setCleanerId(1);
        issuance.setCleanerName("Amina Patel");
        issuance.setQuantity(6);
        issuance.setIssuedBy(1);
        issuance.setIssuedByUsername("demo");
        issuance.setIssuedAt(new Timestamp(System.currentTimeMillis() - 86_400_000L));
        return new ArrayList<>(List.of(issuance));
    }

    private static Material material(int id, String name, int quantity, String unit, int reorderLevel)
    {
        Material material = new Material(name, quantity, unit, reorderLevel);
        material.setId(id);
        return material;
    }

    private static Cleaner cleaner(int id, String name, String department, String contact)
    {
        Cleaner cleaner = new Cleaner(name, department, contact);
        cleaner.setId(id);
        return cleaner;
    }
}