import com.fasterxml.jackson.databind.ObjectMapper;
import controller.simpleControllers.CustomerController;
import controller.simpleControllers.EmployeeController;
import controller.simpleControllers.OrderController;
import controller.simpleControllers.ProductController;
import controller.tokenControllers.CustomerControllerToken;
import controller.tokenControllers.EmployeeControllerToken;
import controller.tokenControllers.OrderControllerToken;
import controller.tokenControllers.ProductControllerToken;
import io.javalin.Javalin;
import service.CustomerService;
import service.EmployeeService;
import service.OrderService;
import service.ProductService;

import static io.javalin.apibuilder.ApiBuilder.*;
//*remove password*, 'merge perms commands', *token endpoint*, deserializer/serializer, *remove token control over yourself*.
public class Main {
    public static void main(String[] args) {

        Javalin app = Javalin.create().start(8080);

        ObjectMapper om = new ObjectMapper();

        CustomerService customerService = new CustomerService();
        EmployeeService employeeService = new EmployeeService();
        OrderService orderService = new OrderService();
        ProductService productService = new ProductService();

        CustomerController customerController = new CustomerControllerToken(customerService, om);
        EmployeeController employeeController = new EmployeeControllerToken(employeeService, om);
        OrderController orderController = new OrderControllerToken(customerService, employeeService, orderService, om);
        ProductController productController = new ProductControllerToken(customerService, employeeService, productService, om);
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
            path("products", () -> {
                post(productController::post);
                path(":id", () -> {
                    get(productController::get);
                    patch(productController::patch);
                    delete(productController::delete);
                });
            });
        });
    }
}
