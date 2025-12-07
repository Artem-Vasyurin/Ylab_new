package vasyurin.work.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;
import vasyurin.work.dto.Product;
import vasyurin.work.services.SaveService;

import java.io.IOException;

@Slf4j
@WebServlet("/product/create")
public class ProductServletCreate extends HttpServlet {

    private final ObjectMapper mapper = new ObjectMapper();
    private final SaveService saveService;

    public ProductServletCreate() {
        this.saveService = SaveService.getInstance();
    }

    public ProductServletCreate(SaveService saveServiceMock) {
        this.saveService = saveServiceMock;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json;charset=UTF-8");
        Product product = mapper.readValue(req.getReader(), Product.class);

        String json = mapper.writeValueAsString(product);
        resp.getWriter().write(json);
        resp.getWriter().flush();

        saveService.save(product);

        resp.setStatus(HttpServletResponse.SC_CREATED);

    }

}
