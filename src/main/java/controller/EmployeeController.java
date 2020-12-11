package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.models.Employee;
import database.models.User;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import service.EmployeeService;
import util.commands.employee.EmployeeCrudPermissions;

import java.sql.SQLException;

public class EmployeeController implements Controller {
    EmployeeService service;
    ObjectMapper om;
    public EmployeeController(EmployeeService service, ObjectMapper om) {
        this.service = service;
        this.om = om;
    }
    @Override
    public void post(Context context) {
        try {
            User sender = findSender(context);
            Employee created = om.readValue(context.body(), Employee.class);
            if (EmployeeCrudPermissions.createPermits.contains(sender.getDepartment())) {
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
            if (EmployeeCrudPermissions.readPermits.contains(sender.getDepartment())) {
                context.result(om.writeValueAsString(service.get(id)));
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
            if (EmployeeCrudPermissions.deletePermits.contains(sender.getDepartment())) {
                service.delete(id);
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
            if (service.authenticate(login, pw)) {
                Employee employee = service.findByLogin(login);
                return employee;
            }
            else {
                throw new UnauthorizedResponse("Invalid login or password");
            }
        }
    }
}
