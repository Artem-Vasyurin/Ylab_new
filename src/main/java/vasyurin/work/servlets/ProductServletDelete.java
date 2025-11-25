package vasyurin.work.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;
import vasyurin.work.dto.Product;
import vasyurin.work.services.SaveService;

import java.io.IOException;

@WebServlet("/product/delete")
public class ProductServletDelete extends HttpServlet {

    private final SaveService saveService;
    private final ObjectMapper mapper = new ObjectMapper();

    private ProductServletDelete() {
        this.saveService = SaveService.getInstance();
    }

    public ProductServletDelete(SaveService saveServiceMock) {
        this.saveService = saveServiceMock;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        Product product = mapper.readValue(req.getReader(), Product.class);

        saveService.delete(product);
        resp.setStatus(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);

    }
}
