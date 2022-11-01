package de.auinger.training.spring_boot;

import de.auinger.training.spring_boot.customer.Customer;
import de.auinger.training.spring_boot.customer.CustomerService;
import de.auinger.training.spring_boot.order.Order;
import de.auinger.training.spring_boot.order.OrderService;
import de.auinger.training.spring_boot.product.Product;
import de.auinger.training.spring_boot.product.ProductService;
import lombok.Setter;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(value = "/dump")
public class DumpDataServlet extends HttpServlet {

    @Setter
    private CustomerService customerService;

    @Setter
    private ProductService productService;

    @Setter
    private OrderService orderService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            response.setContentType("text/plain");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            response.getWriter().println("PRODUCTS:");
            for (Product p : productService.getAllProducts()) {
                var repr = String.format("  #%s: %s (%.2f€)",
                        p.getProductId(), p.getName(), p.getPrice());
                response.getWriter().println(repr);
            }

            response.getWriter().println("\nCUSTOMERS:");
            for (Customer customer : customerService.getAllCustomers()) {
                var repr = String.format("  #%d: %s, %s (ph: %s)",
                        customer.getId(), customer.getFullName(), customer.getAddress().getCity(), customer.getPhoneNumber());
                response.getWriter().println(repr);
            }

            response.getWriter().println("\nORDERS:");
            for (Order o : orderService.getOrders()) {
                var repr = String.format("  #%d: %s, %s (%.2f€) %s",
                        o.getId(), o.getCustomer().getFullName(), o.getCustomer().getAddress().getCity(),
                        o.getTotalPrice(), o.getEstimatedTimeOfDelivery().toString());
                response.getWriter().println(repr);
            }

        } catch (Exception e) {
            e.printStackTrace(response.getWriter());
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();

        // since this servlet has been created by Tomcat, not by Spring it hasn't been subject
        // to any CDI processing -- which is what we are doing now by hand
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        ctx.getAutowireCapableBeanFactory().autowireBeanProperties(
                this,
                AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE,
                true);
    }
}
