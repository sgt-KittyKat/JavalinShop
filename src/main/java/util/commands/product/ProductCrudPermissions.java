package util.commands.product;

import database.models.Department;
import io.javalin.core.security.Role;
import util.commands.CrudPermissions;

import java.util.HashSet;
import java.util.Set;

public class ProductCrudPermissions implements CrudPermissions {
    public static final Set<Role> readPermits = new HashSet<>();
    public static final Set<Role> deletePermits = new HashSet<>();
    public static final Set<Role> updatePermits = new HashSet<>();
    public static final Set<Role> createPermits = new HashSet<>();
    static {
        readPermits.add(Department.PRODUCTION);
        deletePermits.add(Department.PRODUCTION);
        updatePermits.add(Department.PRODUCTION);
        createPermits.add(Department.PRODUCTION);
        readPermits.add(Department.NONE);
    }
}
