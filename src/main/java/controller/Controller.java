package controller;

import database.models.User;
import io.javalin.http.Context;

import java.sql.SQLException;

public interface Controller {
    void post(Context context);
    void patch(Context context);
    void get(Context context);
    void delete(Context context);
    User findSender(Context context) throws SQLException;
}
