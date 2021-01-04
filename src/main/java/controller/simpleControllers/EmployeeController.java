package controller.simpleControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.Controller;
import database.models.Customer;
import database.models.Employee;
import database.models.User;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import org.mindrot.jbcrypt.BCrypt;
import service.EmployeeService;
import util.commands.employee.EmployeeCrudPermissions;

import java.sql.SQLException;

public class EmployeeController implements Controller {
    private EmployeeService service;
    private ObjectMapper om;
    public EmployeeController(EmployeeService service, ObjectMapper om) {
        this.service = service;
        this.om = om;
    }
    @Override
    public void post(Context context) {
        try {
            User sender = findSender(context);
            Employee created = om.readValue(context.body(), Employee.class);
            if ((sender == null || EmployeeCrudPermissions.createPermits.contains(sender.getDepartment()))) {
                if (!service.validateEmail(created.getEmail())) throw new UnauthorizedResponse("Invalid email");
                if (!service.validateNumber(created.getPhoneNumber())) throw new UnauthorizedResponse("Invalid phone number");
                service.post(created);
            }
            else {
                throw new ForbiddenResponse();
            }
        } catch (JsonProcessingException | SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void patch(Context context) {
        try {
            User sender = findSender(context);
            Integer id = Integer.parseInt(context.pathParam("id"));
            Employee target = service.get(id);
            Employee updated = om.readValue(context.body(), Employee.class);
            if (EmployeeCrudPermissions.updatePermits.contains(sender.getDepartment())
                    || target.equals(sender)) {
                target.saveChanges(updated);
                service.patch(target);
            }
            else {
                throw new ForbiddenResponse();
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void get(Context context) {
        try {
            User sender = findSender(context);
            Integer id = Integer.parseInt(context.pathParam("id"));
            Employee target = service.get(id);
            if (sender.equals(target) || EmployeeCrudPermissions.readPermits.contains(sender.getDepartment())) {
                context.result(om.writeValueAsString(target));
            }
            else {
                throw new ForbiddenResponse();
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void delete(Context context) {
        try {
            User sender = findSender(context);
            Integer id = Integer.parseInt(context.pathParam("id"));
            Employee target = service.get(id);
            if (sender.equals(target) || EmployeeCrudPermissions.deletePermits.contains(sender.getDepartment())) {
                service.delete(id);
            }
            else {
                throw new ForbiddenResponse();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findSender(Context context) throws SQLException {
        if (!context.basicAuthCredentialsExist()) {
            return null;
        }
        else {
            String login = context.basicAuthCredentials().component1();
            String pw = context.basicAuthCredentials().component2();
            String token = context.header("token");
            if (service.authenticate(login, pw, token)) {
                Employee employee = service.findByLogin(login);
                return employee;
            }
            else {
                throw new UnauthorizedResponse("Invalid login or password");
            }
        }
    }

    public EmployeeService getService() {
        return service;
    }

    public void setService(EmployeeService service) {
        this.service = service;
    }

    public ObjectMapper getOm() {
        return om;
    }

    public void setOm(ObjectMapper om) {
        this.om = om;
    }
}
