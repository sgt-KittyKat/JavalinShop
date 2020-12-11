import com.fasterxml.jackson.databind.ObjectMapper;
import controller.CustomerController;
import controller.EmployeeController;
import controller.OrderController;
import controller.ProductController;
import io.javalin.Javalin;
import service.CustomerService;
import service.EmployeeService;
import service.OrderService;
import service.ProductService;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
    public static void main(String[] args) {

        Javalin app = Javalin.create().start(8080);

        ObjectMapper om = new ObjectMapper();

        CustomerService customerService = new CustomerService();
        EmployeeService employeeService = new EmployeeService();
        OrderService orderService = new OrderService();
        ProductService productService = new ProductService();

        CustomerController customerController = new CustomerController(customerService, om);
        EmployeeController employeeController = new EmployeeController(employeeService, om);
        OrderController orderController = new OrderController(customerService, employeeService, orderService, om);
        ProductController productController = new ProductController(customerService, employeeService, productService, om);
        app.routes(() -> {
            path("customers", () -> {
                post(customerController::post);
                path(":id",() -> {
                    get(customerController::get);
                    patch(customerController::patch);
                    delete(customerController::delete);
                });
            });
            path("employees", () -> {
                post(employeeController::post);
                path(":id",() -> {
                    get(employeeController::get);
                    patch(employeeController::patch);
                    delete(employeeController::delete);
                });
            });
            path("orders", () -> {
                post(orderController::post);
                path(":id", () -> {
                    get(orderController::get);
                    patch(orderController::patch);
                    delete(orderController::delete);
                });
            });
            path("product", () -> {
                post(productController::post);
                path(":id", () -> {
                    get(productController::get);
                    patch(orderController::patch);
                    delete(orderController::delete);
                });
            });
        });
    }
}
