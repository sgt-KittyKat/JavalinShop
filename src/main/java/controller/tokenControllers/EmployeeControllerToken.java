package controller.tokenControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import controller.simpleControllers.EmployeeController;
import database.models.Employee;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import service.EmployeeService;

import java.sql.SQLException;

public class EmployeeControllerToken extends EmployeeController {

    public EmployeeControllerToken(EmployeeService service, ObjectMapper om) {
        super(service, om);
    }
    public Employee findSender(Context context) throws SQLException {
        if (!context.basicAuthCredentialsExist()) {
            if (context.header("token") != null) {
                String token = context.header("token");
                if (getService().authenticateWithToken(token)) {
                    Employee employee = getService().findByToken(token);
                    context.header("token", employee.getToken());
                    return employee;
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
                Employee employee = getService().findByLogin(login);
                context.header("token", employee.getToken());
                return employee;
            }
        }
        throw new UnauthorizedResponse("Invalid auth credentials");
    }
}
