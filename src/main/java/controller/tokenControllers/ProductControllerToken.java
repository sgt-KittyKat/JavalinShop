package controller.tokenControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import controller.simpleControllers.ProductController;
import database.models.User;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import service.ProductService;
import service.UserService;

import java.sql.SQLException;

public class ProductControllerToken extends ProductController {
    public ProductControllerToken(UserService customerService, UserService employeeService, ProductService productService, ObjectMapper om) {
        super(customerService, employeeService, productService, om);
    }

    public User findSender(Context context) throws SQLException {
        if (!context.basicAuthCredentialsExist()) {
            String token = context.header("token");
            if (getCustomerService().authenticateWithToken(token)) {
                User user = getCustomerService().findByToken(token);
                return user;
            }
            else if (getEmployeeService().authenticateWithToken(token)) {
                User user = getEmployeeService().findByLogin(token);
                return user;
            }

            else {
                throw new UnauthorizedResponse("Invalid token");
            }
        }
        else {
            String login = context.basicAuthCredentials().component1();
            String pw = context.basicAuthCredentials().component2();
            if (getCustomerService().authenticateWithBasicAuth(login, pw)) {
                User user = getCustomerService().findByLogin(login);
                context.header("token", user.getToken());
                return user;
            }
            else if (getEmployeeService().authenticateWithBasicAuth(login, pw)) {
                User user = getEmployeeService().findByLogin(login);
                context.header("token", user.getToken());
                return user;
            }
            else {
                throw new UnauthorizedResponse("Invalid login or password");
            }
        }
    }
}
