package utils;

import model.User;

/**
 * Holds the currently logged-in user for the lifetime of the application run.
 *
 * Avoids passing the User object through every form's constructor and gives
 * a single place for role checks (e.g. Session.isSupervisor()) and for
 * recording who performed an action (e.g. who issued stock).
 *
 * @author Sean
 */
public final class Session
{
    private static User currentUser;

    private Session()
    {
    }

    public static void setCurrentUser(User user)
    {
        currentUser = user;
    }

    public static User getCurrentUser()
    {
        return currentUser;
    }

    public static void clear()
    {
        currentUser = null;
    }

    public static boolean isSupervisor()
    {
        return currentUser != null && Roles.SUPERVISOR.equals(currentUser.getRole());
    }
} //Session
