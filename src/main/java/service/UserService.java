package service;

import database.models.User;

import java.sql.SQLException;

public interface UserService extends Service {
    boolean authenticate(String login, String pw, String token) throws SQLException;
    User findByLogin(String login) throws SQLException;
    boolean authenticateWithBasicAuth(String login, String pw) throws SQLException;
    boolean authenticateWithToken(String token) throws SQLException;
    User findByToken(String token) throws SQLException;
}
