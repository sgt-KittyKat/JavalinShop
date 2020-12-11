package database.config;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import database.models.Customer;
import database.models.Employee;
import database.models.Order;
import database.models.Product;

import java.sql.SQLException;

public class DatabaseConfig {
    public static final String JDBC_CONNECTION_STRING = "jdbc:sqlite:C:\\SQL\\DBs\\shop.db";
    public static ConnectionSource connectionSource;
    static {
        try {
            connectionSource = new JdbcConnectionSource(JDBC_CONNECTION_STRING);
            TableUtils.createTableIfNotExists(connectionSource, Customer.class);
            TableUtils.createTableIfNotExists(connectionSource, Product.class);
            TableUtils.createTableIfNotExists(connectionSource, Employee.class);
            TableUtils.createTableIfNotExists(connectionSource, Order.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
