package service;

import database.models.User;

import java.sql.SQLException;

public interface UserService extends Service {
    boolean authenticate(String login, String pw) throws SQLException;
    User findByLogin(String login) throws SQLException;
}
