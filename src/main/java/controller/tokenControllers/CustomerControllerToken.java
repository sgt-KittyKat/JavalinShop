package controller.tokenControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import controller.simpleControllers.CustomerController;
import database.models.Customer;
import database.models.Employee;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import service.CustomerService;

import java.sql.SQLException;

public class CustomerControllerToken extends CustomerController {
    public CustomerControllerToken(CustomerService service, ObjectMapper om) {
        super(service, om);
    }

    public Customer findSender(Context context) throws SQLException {
        if (!context.basicAuthCredentialsExist()) {
            if (context.header("token") != null) {
                String token = context.header("token");
                if (getService().authenticateWithToken(token)) {
                    Customer customer = getService().findByToken(token);
                    context.header("token", customer.getToken());
                    return customer;
                }
                else {
                    throw new ForbiddenResponse("Invalid token");
                }
            }
            else {
                return null;
            }
        }
        else {
            String login = context.basicAuthCredentials().component1();
            String pw = context.basicAuthCredentials().component2();
            if (getService().authenticateWithBasicAuth(login, pw)) {
                Customer customer = getService().findByLogin(login);
                context.header("token", customer.getToken());
                return customer;
            }
        }
        throw new UnauthorizedResponse("Invalid auth credentials");
    }
}
