package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.models.Customer;
import database.models.Employee;
import database.models.Product;
import database.models.User;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import service.OrderService;
import service.ProductService;
import service.UserService;
import util.commands.product.ProductCrudPermissions;

import java.sql.SQLException;

public class ProductController implements Controller {
    private UserService customerService;
    private UserService employeeService;
    private ProductService productService;
    private ObjectMapper om;

    public ProductController(UserService customerService, UserService employeeService, ProductService productService, ObjectMapper om) {
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.productService = productService;
        this.om = om;
    }

    public void post(Context context) {
        try {
            User sender = findSender(context);
            Product target = om.readValue(context.body(), Product.class);
            if (sender instanceof Employee
                    && ProductCrudPermissions.createPermits.contains(sender.getDepartment())) {
                productService.post(target);
            }
            else {
                throw new ForbiddenResponse();
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void patch(Context context) {
        try {
            User sender = findSender(context);
            Integer id = Integer.parseInt(context.pathParam("id"));
            Product target = productService.get(id);
            Product updated = om.readValue(context.body(), Product.class);
            if (sender instanceof Employee
                    && ProductCrudPermissions.updatePermits.contains(sender.getDepartment())) {
                target.saveChanges(updated);
                productService.patch(target);
            }
            else {
                throw new ForbiddenResponse();
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void get(Context context) {
        try {
            User sender = findSender(context);
            Integer id = Integer.parseInt(context.pathParam("id"));
            if (sender instanceof Employee
                    && ProductCrudPermissions.readPermits.contains(sender.getDepartment())) {
                productService.get(id);
            }
            else if (sender instanceof Customer) {
                productService.get(id);
            }
            else {
                throw new ForbiddenResponse();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Context context) {
        try {
            User sender = findSender(context);
            Integer id = Integer.parseInt(context.pathParam("id"));
            if (sender instanceof Employee
                    && ProductCrudPermissions.readPermits.contains(sender.getDepartment())) {
                productService.delete(id);
            }
            else {
                throw new ForbiddenResponse();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public User findSender(Context context) throws SQLException {
        if (!context.basicAuthCredentialsExist()) {
            return null;
        }
        else {
            String login = context.basicAuthCredentials().component1();
            String pw = context.basicAuthCredentials().component2();
            if (customerService.authenticate(login, pw)) {
                User user = customerService.findByLogin(login);
                return user;
            }
            else if (employeeService.authenticate(login, pw)) {
                User user = employeeService.findByLogin(login);
                return user;
            }

            else {
                throw new UnauthorizedResponse("Invalid login or password");
            }
        }
    }
}
