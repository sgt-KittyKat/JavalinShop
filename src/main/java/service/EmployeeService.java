package service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.spring.DaoFactory;
import database.config.DatabaseConfig;
import database.models.Customer;
import database.models.Employee;
import io.javalin.http.UnauthorizedResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class EmployeeService implements UserService {
    Dao<Employee, Integer> dao;
    public EmployeeService() {
        try {
            dao = DaoFactory.createDao(DatabaseConfig.connectionSource, Employee.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public boolean authenticate(String login, String password) throws SQLException {
        Employee employee = findByLogin(login);
        if (employee == null) {
            throw new UnauthorizedResponse("Login doesn't exist");
        } else {
            if (BCrypt.checkpw(password, employee.getPassword())) {
                return true;
            }
            else {
                return false;
            }

        }
    }
    EmployeeService(Dao<Employee, Integer> dao) {
        this.dao = dao;
    }
    public Employee findByLogin(String login) throws SQLException {
        Employee employee = dao.queryBuilder().where().eq("login", login).queryForFirst();
        return employee;
    }
    public Employee get(Integer id) throws SQLException {
        return dao.queryForId(id);
    }

    public void post(Employee target) throws SQLException {
        dao.create(target);
    }

    public void patch(Employee target) throws SQLException {
        if (!target.getPassword().equals(get(target.getId()).getPassword())) {
            target.setPassword(BCrypt.hashpw(target.getPassword(), BCrypt.gensalt()));
        }
        dao.update(target);
    }

    public void delete(Integer id) throws SQLException {
        dao.deleteById(id);
    }
}
