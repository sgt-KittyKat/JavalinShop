package service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.spring.DaoFactory;
import database.config.DatabaseConfig;
import database.models.Customer;
import database.models.Employee;
import io.javalin.http.UnauthorizedResponse;
import org.apache.commons.validator.routines.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;


public class EmployeeService implements UserService {
    Dao<Employee, Integer> dao;
    public EmployeeService() {
        try {
            dao = DaoFactory.createDao(DatabaseConfig.connectionSource, Employee.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public boolean validateNumber(String number) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(number, "");
            Employee employee = findByPhoneNumber(number);
            if (phoneUtil.isValidNumber(phoneNumber) && employee == null)
                return true;
            return false;
        } catch (NumberParseException | SQLException e) {
            throw new UnauthorizedResponse("Invalid phone number");
        }
    }
    public Employee findByPhoneNumber(String number) throws SQLException {
        Employee employee = dao.queryBuilder().where().eq("phoneNumber", number).queryForFirst();
        return employee;
    }
    public Employee findByEmail(String email) throws SQLException {
        Employee employee = dao.queryBuilder().where().eq("email", email).queryForFirst();
        return employee;
    }
    public boolean validateEmail(String email) throws SQLException {
        EmailValidator validator = EmailValidator.getInstance();
        Employee employee = findByEmail(email);
        if (validator.isValid(email) && employee == null) {
            return true;
        }
        return false;
    }
    public boolean authenticateWithToken(String token) throws SQLException {
        Employee employee = findByToken(token);
        if (employee == null) {
            return false;
        }
        if (LocalDate.now().compareTo(employee.getTokenExpireTime()) > 0) {
            employee.setToken(null);
            employee.setTokenExpireTime(null);
            patch(employee);
        }
        return true;
    }
    public boolean authenticateWithBasicAuth(String login, String password) throws SQLException {
        Employee employee = findByLogin(login);
        if (employee == null) {
            return false;
        } else {
            if (BCrypt.checkpw(password, employee.getPassword())) {
                employee.setToken(genSmolToken());
                employee.setTokenExpireTime(LocalDate.now().plusDays(14));
                patch(employee);
                return true;
            }
            else {
                return false;
            }

        }
    }
    public Employee findByToken(String token) throws SQLException {
        return dao.queryBuilder().where().eq("token", token).queryForFirst();
    }
    public boolean authenticate(String login, String password, String token) throws SQLException {
        return false;
    }
    public Employee findByLogin(String login) throws SQLException {
        Employee employee = dao.queryBuilder().where().eq("login", login).queryForFirst();
        return employee;
    }
    public Employee get(Integer id) throws SQLException {
        return dao.queryForId(id);
    }

    public void post(Employee target) throws SQLException {
        target.setPassword(BCrypt.hashpw(target.getPassword(), BCrypt.gensalt()));
        dao.create(target);
    }

    public void patch(Employee target) throws SQLException {
        dao.update(target);
    }

    public void delete(Integer id) throws SQLException {
        dao.deleteById(id);
    }

    public String genSmolToken() {
        String token = new String();
        for (int i = 0 ; i < 30 ; i++) {
            token += (char)ThreadLocalRandom.current().nextInt() % 90 + 33;
        }
        return token;
    }
    EmployeeService(Dao<Employee, Integer> dao) {
        this.dao = dao;
    }
    public void saveChanges(Employee target, Employee updated) { //applyPartialChange
        if (updated.getEmail() != null && !updated.getEmail().equals(target.getEmail())) {
            target.setEmail(updated.getEmail());
        }
        if (updated.getName() != null && !updated.getName().equals(target.getName())) {
            target.setName(updated.getName());
        }
        if (updated.getLogin() != null && !updated.getLogin().equals(target.getLogin())) {
            target.setLogin(updated.getLogin());
        }
        if (updated.getSurname() != null && !updated.getSurname().equals(target.getSurname())) {
            target.setSurname(updated.getSurname());
        }
        if (updated.getPassword() != null && !BCrypt.checkpw(updated.getPassword(),target.getPassword())) {
            target.setPassword(BCrypt.hashpw(updated.getPassword(), BCrypt.gensalt()));
        }
    }
}
