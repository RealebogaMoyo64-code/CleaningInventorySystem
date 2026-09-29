# CleaningInventorySystem
This project is designed to simulate the development of a real-world university Cleaning Inventory & Issuance System
The Cleaning Inventory System is a Java desktop application (Java Swing +
JDBC + PostgreSQL) that helps an organisation manage cleaning stock: tracking
materials, suppliers, cleaning staff, and the issuance of materials to staff,
with role-based access for Storekeepers and Supervisors.

Core modules:
- Authentication: LoginForm / RegisterForm, backed by UserDAO, with session
  state tracked in Session.java and passwords hashed via PasswordUtil.
- Dashboard: DashboardPanel gives an overview on login (MainShell as the
  main application shell/navigation).
- Materials: MaterialsPanel + MaterialDAO - add/edit/track stock levels,
  reorder levels, and units.
- Suppliers: SuppliersPanel + SupplierDAO - manage supplier contact records.
- Cleaners: CleanersPanel + CleanerDAO - manage cleaning staff records.
- Issuances: IssuancePanel + IssuanceDAO - record materials issued to a
  cleaner by a logged-in user, with stock validation (see
  InsufficientStockException) so materials can't be over-issued.
- Reports: ReportsPanel - reporting view over the above data.

- --------------------------------------------------------
TECHNOLOGIES USED
--------------------------------------------------------
- Java (developed in NetBeans, Ant build via build.xml)
- Java Swing (custom UI theming in ui/Theme.java, ui/RoundedPanel.java)
- PostgreSQL (JDBC driver: postgresql-42.7.13.jar, included in /lib and /dist/lib)
- JDBC (DAO pattern: one DAO class per entity)
- Git & GitHub for version control (feature-branch workflow)

--------------------------------------------------------
DATABASE / LOGIN CREDENTIALS USED
--------------------------------------------------------
IMPORTANT FOR MARKER: The application does not ship with a pre-seeded demo
user account. A user account must be created via the "Register" screen on
first run (choose role: Storekeeper or Supervisor) before logging in.

Database connection (src/database/DatabaseConnection.java):
- Default DB host:      127.0.0.1
- Default DB port:      5432
- Default DB name:      cleaning_inventory_db
- Default DB username:  postgres
- Default DB password:  Xerials19

These can be overridden without editing code via environment variables or
JVM system properties, so you do NOT need to hard-code your real DB
credentials in the source before submitting:
    DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD, DB_URL

Application login used in the demo video:
- Username: [FILL IN]
- Password: [FILL IN]
- Role:     [FILL IN - Storekeeper / Supervisor]

If the database is unavailable, the app falls back to read-only sample
data via utils/DemoData.java (materials, cleaners, one supplier, one
issuance) purely so the UI can still be demonstrated.

--------------------------------------------------------
 HOW TO RUN
--------------------------------------------------------
Option A - From NetBeans:
1. Open the CleaningInventorySystem project folder in NetBeans.
2. Ensure PostgreSQL is running and the credentials in section 5 are set
   (or leave defaults, or set the DB_* environment variables above).
3. Run src/main/Main.java.

Option B - From the built JAR:
1. Open a terminal in the /dist folder.
2. Run: java -jar "CleaningInventorySystem.jar"
   (the postgresql-42.7.13.jar dependency is already included in /dist/lib
   and referenced in the manifest)

On first run, register a new user via the Register screen, then log in.

--------------------------------------------------------
 KNOWN LIMITATIONS / MISSING FEATURES
--------------------------------------------------------
No missing features - all planned functionality (authentication, materials,
suppliers, cleaners, issuances, reports) is implemented and working.
