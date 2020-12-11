package service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.spring.DaoFactory;
import database.config.DatabaseConfig;
import database.models.Customer;
import io.javalin.http.UnauthorizedResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.time.LocalDate;

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
    public boolean authenticate(String login, String password) throws SQLException {
        Customer customer = findByLogin(login);
        if (customer == null) {
            throw new UnauthorizedResponse("Login doesn't exist");
        }
        else {
            if (BCrypt.checkpw(password, customer.getPassword())) {
                return true;
            }
            else {
                return false;
            }
        }
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
}
