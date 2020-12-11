package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.models.Employee;
import database.models.Order;
import database.models.User;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import service.*;
import util.commands.order.OrderCrudPermissions;

import java.sql.SQLException;

public class OrderController implements Controller {
    private UserService customerService;
    private UserService employeeService;
    private OrderService orderService;
    private ObjectMapper om;
    public OrderController(UserService customerService, UserService employeeService, OrderService orderService, ObjectMapper om) {
        this.customerService = customerService;
        this.employeeService = employeeService;
        this.orderService = orderService;
        this.om = om;
    }
    public void post(Context context) {
        try {
            User sender = findSender(context);
            Order target = om.readValue(context.body(), Order.class);
            if (sender.equals(target.getCustomer())) {
                orderService.post(target);
            }
            else if (OrderCrudPermissions.createPermits.contains(sender.getDepartment())) {
                orderService.post(target);
            }
            else {
                throw new ForbiddenResponse();
            }

        } catch (SQLException | JsonProcessingException e){
            e.printStackTrace();
        }
    }

    public void patch(Context context) {
        try {
            User sender = findSender(context);
            Integer id = Integer.parseInt(context.pathParam("id"));
            Order target = orderService.get(id);
            Order updated = om.readValue(context.body(), Order.class);
            if (target.getCustomer().equals(sender)) {
                orderService.patch(target);
            }
            else if (OrderCrudPermissions.updatePermits.contains(sender.getDepartment())) {
                target.saveChanges(updated);
                orderService.patch(target);
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
            Order target = orderService.get(id);
            if (target.getCustomer().equals(sender)) {
                orderService.get(id);
            }
            else if (OrderCrudPermissions.readPermits.contains(sender.getDepartment())) {
                orderService.get(id);
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
            Order target = orderService.get(id);
            if (target.getCustomer().equals(target)) {
                orderService.delete(id);
            }
            else if (OrderCrudPermissions.deletePermits.contains(sender.getDepartment())) {
                orderService.delete(id);
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
