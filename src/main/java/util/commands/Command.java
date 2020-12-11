package util.commands;

import database.models.Department;

import java.util.Set;

public interface Command {
    Set <Department> permittedDepartments = null;
    Set <Department> forbiddenDepartments = null;
}
