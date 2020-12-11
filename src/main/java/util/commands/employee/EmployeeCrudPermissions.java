package util.commands.employee;

import database.models.Department;
import io.javalin.core.security.Role;

import java.util.HashSet;
import java.util.Set;

public class EmployeeCrudPermissions {
    public static final Set<Role> readPermits = new HashSet<>();
    public static final Set<Role> deletePermits = new HashSet<>();
    public static final Set<Role> updatePermits = new HashSet<>();
    public static final Set<Role> createPermits = new HashSet<>();
    static {
        readPermits.add(Department.MANAGEMENT);
        deletePermits.add(Department.MANAGEMENT);
        updatePermits.add(Department.MANAGEMENT);
        createPermits.add(Department.MANAGEMENT);
    }
}
