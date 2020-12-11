package database.models;

import io.javalin.core.security.Role;

public enum Department implements Role {
    SALES, PRODUCTION, MANAGEMENT, NONE
}
