package controller.tokenControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import controller.simpleControllers.OrderController;
import database.models.User;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import service.OrderService;
import service.UserService;

import java.sql.SQLException;

public class OrderControllerToken extends OrderController {

    public OrderControllerToken(UserService customerService, UserService employeeService, OrderService orderService, ObjectMapper om) {
        super(customerService, employeeService, orderService, om);
    }

    public User findSender(Context context) throws SQLException {
        if (!context.basicAuthCredentialsExist()) {
            String token = context.header("token");
            if (getCustomerService().authenticateWithToken(token)) {
                User user = getCustomerService().findByToken(token);
                context.header("token", user.getToken());
                return user;
            }
            else if (getEmployeeService().authenticateWithToken(token)) {
                User user = getEmployeeService().findByLogin(token);
                context.header("token", user.getToken());
                return user;
            }

            else {
                throw new UnauthorizedResponse("Invalid login or password");
            }
        }
        else {
            String login = context.basicAuthCredentials().component1();
            String pw = context.basicAuthCredentials().component2();
            if (getCustomerService().authenticateWithBasicAuth(login, pw)) {
                User user = getCustomerService().findByLogin(login);
                return user;
            }
            else if (getEmployeeService().authenticateWithBasicAuth(login, pw)) {
                User user = getEmployeeService().findByLogin(login);
                return user;
            }
            else {
                throw new UnauthorizedResponse("Invalid login or password");
            }
        }
    }
}
