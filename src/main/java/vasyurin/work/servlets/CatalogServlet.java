package vasyurin.work.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vasyurin.work.dto.Product;
import vasyurin.work.services.ProductService;
import vasyurin.work.services.ProductValidator;

import java.io.IOException;
import java.util.List;

@WebServlet("/catalog")
public class CatalogServlet extends HttpServlet {

    private final ProductService productService;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ProductValidator validator;

    private CatalogServlet() {
        this.productService = ProductService.getInstance();
        this.validator = ProductValidator.getInstance();
    }

    public CatalogServlet(ProductService productServiceMock, ProductValidator validatorMock) {
        this.productService = productServiceMock;
        this.validator = validatorMock;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write("<h1>КАТАЛОГ! Сервер работает 🚀</h1>");
        resp.getWriter().flush();
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json;charset=UTF-8");

        Product filter;
        try {
            filter = mapper.readValue(req.getInputStream(), Product.class);
        } catch (JsonParseException | JsonMappingException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Неверный формат JSON\"}");
            resp.getWriter().flush();
            return;
        }

        List<String> validationErrors = validator.validate(filter);
        if (!validationErrors.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String jsonErrors = mapper.writeValueAsString(validationErrors);
            resp.getWriter().write("{\"validationErrors\": " + jsonErrors + "}");
            resp.getWriter().flush();
            return;
        }

        List<Product> filteredProducts = productService.getFilteredProducts(filter);

        String json = mapper.writeValueAsString(filteredProducts);
        resp.getWriter().write(json);
        resp.getWriter().flush();
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
