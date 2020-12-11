package service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.spring.DaoFactory;
import database.config.DatabaseConfig;
import database.models.Customer;
import database.models.Order;

import java.sql.SQLException;

public class OrderService implements Service {
    Dao<Order, Integer> dao;
    public OrderService() {
        try {
            dao = DaoFactory.createDao(DatabaseConfig.connectionSource, Order.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    OrderService(Dao<Order, Integer> dao) {
        this.dao = dao;
    }
    public Order get(Integer id) throws SQLException {
        return dao.queryForId(id);
    }

    public void post(Order target) throws SQLException {
        dao.create(target);
    }

    public void patch(Order target) throws SQLException {
        dao.update(target);
    }

    public void delete(Integer id) throws SQLException {
        dao.deleteById(id);
    }
}
