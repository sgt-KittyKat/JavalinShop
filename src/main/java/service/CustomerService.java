package service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.spring.DaoFactory;
import database.config.DatabaseConfig;
import database.models.Customer;
import io.javalin.http.UnauthorizedResponse;
import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class CustomerService implements UserService {
    Dao <Customer, Integer> dao;
    public CustomerService() {
        try {
            dao = DaoFactory.createDao(DatabaseConfig.connectionSource, Customer.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public Customer findByLogin(String login) throws SQLException {
        Customer customer = dao.queryBuilder().where().eq("login", login).queryForFirst();
        return customer;
    }

    public boolean isTokenAlive(Customer customer) {
        if (customer.getToken() == null) return false;
        if (customer.getTokenExpireTime().compareTo(LocalDate.now()) < 0) return false;
        return true;
    }
    public boolean authenticate(String login, String password, String token) throws SQLException {
        return false;
    }
    CustomerService(Dao<Customer, Integer> dao) {
        this.dao = dao;
    }
    public Customer get(Integer id) throws SQLException {
        return dao.queryForId(id);
    }

    public void post(Customer target) throws SQLException {
        target.setPassword(BCrypt.hashpw(target.getPassword(), BCrypt.gensalt()));
        dao.create(target);
    }

    public void patch(Customer target) throws SQLException {
        if (!target.getPassword().equals(get(target.getId()).getPassword())) {
            target.setPassword(BCrypt.hashpw(target.getPassword(), BCrypt.gensalt()));
        }
        dao.update(target);
    }

    public void delete(Integer id) throws SQLException {
        dao.deleteById(id);
    }
    public Customer findByEmail(String email) throws SQLException {
        Customer customer = dao.queryBuilder().where().eq("email", email).queryForFirst();
        return customer;
    }
    public boolean validateEmail(String email) throws SQLException {
        EmailValidator validator = EmailValidator.getInstance();
        Customer customer = findByEmail(email);
        if (validator.isValid(email) && customer == null) {
            return true;
        }
        return false;
    }
    public boolean validateNumber(String number) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(number, "");
            Customer customer = findByPhoneNumber(number);
            if (phoneUtil.isValidNumber(phoneNumber) && customer == null)
                return true;
            return false;
        } catch (NumberParseException | SQLException e) {
            throw new UnauthorizedResponse("Invalid phone number");
        }
    }
    public Customer findByPhoneNumber(String number) throws SQLException {
        Customer customer = dao.queryBuilder().where().eq("phoneNumber", number).queryForFirst();
        return customer;
    }
    public String genSmolToken() {
        String token = new String();
        for (int i = 0 ; i < 30 ; i++) {
            token += (char)ThreadLocalRandom.current().nextInt() % 90 + 33;
        }
        return token;
    }
    public boolean authenticateWithBasicAuth(String login, String password) throws SQLException {
        Customer customer = findByLogin(login);
        if (customer == null) {
            return false;
        } else {
            if (BCrypt.checkpw(password, customer.getPassword())) {
                customer.setToken(genSmolToken());
                customer.setTokenExpireTime(LocalDate.now().plusDays(14));
                patch(customer);
                return true;
            }
            else {
                return false;
            }

        }
    }
    public Customer findByToken(String token) throws SQLException {
        return dao.queryBuilder().where().eq("token", token).queryForFirst();
    }
    public boolean authenticateWithToken(String token) throws SQLException {
        Customer customer = findByToken(token);
        if (customer == null) {
            return false;
        }
        if (LocalDate.now().compareTo(customer.getTokenExpireTime()) > 0) {
            customer.setToken(null);
            customer.setTokenExpireTime(null);
            patch(customer);
        }
        return true;
    }
    public void saveChanges(Customer target, Customer updated) { //applyPartialChange
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
        if (updated.getPassword() != null && !updated.getPassword().equals(target.getPassword())) {
            target.setPassword(updated.getPassword());
        }
    }
}
