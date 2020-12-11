package service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.spring.DaoFactory;
import database.config.DatabaseConfig;
import database.models.Customer;
import database.models.Product;

import java.sql.SQLException;

public class ProductService implements Service {
    Dao<Product, Integer> dao;
    public ProductService() {
        try {
            dao = DaoFactory.createDao(DatabaseConfig.connectionSource, Product.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    ProductService(Dao<Product, Integer> dao) {
        this.dao = dao;
    }
    public Product get(Integer id) throws SQLException {
        return dao.queryForId(id);
    }

    public void post(Product target) throws SQLException {
        dao.create(target);
    }

    public void patch(Product target) throws SQLException {
        dao.update(target);
    }

    public void delete(Integer id) throws SQLException {
        dao.deleteById(id);
    }
}
