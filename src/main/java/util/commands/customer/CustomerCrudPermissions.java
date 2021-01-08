package util.commands.customer;

import database.models.Department;
import io.javalin.core.security.Role;
import util.commands.CrudPermissions;

import java.util.HashSet;
import java.util.Set;

public class CustomerCrudPermissions implements CrudPermissions {
    public static final Set<Role> readPermits = new HashSet<>();
    public static final Set<Role> deletePermits = new HashSet<>();
    public static final Set<Role> updatePermits = new HashSet<>();
    public static final Set<Role> createPermits = new HashSet<>();
    static {
        readPermits.add(Department.SALES);
        deletePermits.add(Department.SALES);
        updatePermits.add(Department.SALES);
        createPermits.add(Department.SALES);
    }
}
