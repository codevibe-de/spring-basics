package pizza;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pizza.customer.CustomerService;
import pizza.order.OrderService;
import pizza.product.ProductService;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

public class PizzaApp {

    public static void main(String[] args) {
        // Instantiate context
        ApplicationContext beanContainer = new ClassPathXmlApplicationContext("beans/default-beans.xml");


        // query and use beans
        beanContainer.getBean(DataLoader.class).run();
        ProductService productService = beanContainer.getBean(ProductService.class);
        CustomerService customerService = beanContainer.getBean(CustomerService.class);
        OrderService orderService = beanContainer.getBean(OrderService.class);

        // Get a product ---
        var product = productService.getProduct("P-10");
        System.out.println(product);

        // Get a customer ---
        var customer = customerService.getCustomerByPhoneNumber("+49 123 456789");
        System.out.println(customer);

        // Place an order ---
        var order = orderService.placeOrder(
                "+49 123 456789",
                ofEntries(
                        entry("P-10", 3),
                        entry("S-03", 1)
                )
        );
        System.out.println(order);
    }

}
