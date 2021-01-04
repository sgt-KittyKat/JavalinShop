package controller.simpleControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.Controller;
import database.models.Customer;
import database.models.User;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import org.mindrot.jbcrypt.BCrypt;
import service.CustomerService;
import util.commands.customer.CustomerCrudPermissions;

import java.sql.SQLException;

public class CustomerController implements Controller {
    private CustomerService service;
    private ObjectMapper om;
    public CustomerController(CustomerService service, ObjectMapper om) {
        this.service = service;
        this.om = om;
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
                Customer customer = service.findByLogin(login);
                return customer;
            }
            else {
                throw new UnauthorizedResponse("Invalid login or password");
            }
        }
    }
    public void post(Context context) {
        try {
            User sender = findSender(context);
            Customer created = om.readValue(context.body(), Customer.class);
            if (sender == null || CustomerCrudPermissions.readPermits.contains(sender.getDepartment())) {
                service.post(created);
            } else {
                throw new ForbiddenResponse();
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void patch(Context context) {
        try {
            User sender = findSender(context);
            Customer target = service.get(Integer.parseInt(context.pathParam("id")));
            Customer updated = om.readValue(context.body(), Customer.class);
            if (sender == null) {
                throw new ForbiddenResponse();
            }
            if (CustomerCrudPermissions.updatePermits.contains(sender) || sender.equals(target)) {
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
            Customer target = service.get(id);
            if (sender == null) {
                throw new ForbiddenResponse();
            }
            else if (CustomerCrudPermissions.readPermits.contains(sender) || sender.equals(target)) {
                context.result(om.writeValueAsString(service.get(id)));
            }
            else {
                throw new ForbiddenResponse("Unauthorized to get a customer");
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
            Customer target = service.get(id);
            if (sender == null) {
                throw new ForbiddenResponse();
            }
            if (CustomerCrudPermissions.deletePermits.contains(sender) || sender.equals(target)) {
                service.delete(id);
            }
            else {
                throw new ForbiddenResponse();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CustomerService getService() {
        return service;
    }

    public void setService(CustomerService service) {
        this.service = service;
    }

    public ObjectMapper getOm() {
        return om;
    }

    public void setOm(ObjectMapper om) {
        this.om = om;
    }
}
