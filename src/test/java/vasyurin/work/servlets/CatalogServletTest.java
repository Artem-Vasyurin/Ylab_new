package vasyurin.work.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import vasyurin.work.dto.Product;
import vasyurin.work.enams.ProductCategory;
import vasyurin.work.services.ProductService;
import vasyurin.work.services.ProductValidator;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Testcontainers
public class CatalogServletTest {

    private CatalogServlet servlet;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private ProductService productServiceMock;
    private ProductValidator validatorMock;
    private final ObjectMapper mapper = new ObjectMapper();
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws IOException {
        productServiceMock = mock(ProductService.class);
        validatorMock = mock(ProductValidator.class);


        productServiceMock = mock(ProductService.class);
        validatorMock = mock(ProductValidator.class);

        servlet = new CatalogServlet(productServiceMock, validatorMock);

        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);

        responseWriter = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    void testDoGet() throws IOException {
        servlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_OK);
        String html = responseWriter.toString();
        assertEquals("<h1>КАТАЛОГ! Сервер работает 🚀</h1>", html);
    }

    @Test
    void testDoPost_ValidProduct() throws Exception {
        Product filter = new Product();
        filter.setName("Test");
        filter.setPrice(BigDecimal.valueOf(100));
        filter.setCategory(ProductCategory.ELECTRONICS);

        String jsonFilter = mapper.writeValueAsString(filter);
        when(req.getInputStream()).thenReturn(
                new DelegatingServletInputStream(new ByteArrayInputStream(jsonFilter.getBytes()))
        );

        Product p1 = new Product();
        p1.setName("Product1");
        Product p2 = new Product();
        p2.setName("Product2");

        when(validatorMock.validate(any())).thenReturn(List.of()); // без ошибок
        when(productServiceMock.getFilteredProducts(any())).thenReturn(List.of(p1, p2));

        servlet.doPost(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_OK);
        String responseJson = responseWriter.toString();
        List<Product> products = List.of(mapper.readValue(responseJson, Product[].class));
        assertEquals(2, products.size());
        assertEquals("Product1", products.get(0).getName());
    }

    @Test
    void testDoPost_InvalidJson() throws Exception {
        String invalidJson = "{bad json}";
        when(req.getInputStream()).thenReturn(
                new DelegatingServletInputStream(new ByteArrayInputStream(invalidJson.getBytes()))
        );

        servlet.doPost(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String response = responseWriter.toString();
        assertEquals("{\"error\": \"Неверный формат JSON\"}", response);
    }

    @Test
    void testDoPost_ValidationErrors() throws Exception {
        Product filter = new Product();
        String jsonFilter = mapper.writeValueAsString(filter);
        when(req.getInputStream()).thenReturn(
                new DelegatingServletInputStream(new ByteArrayInputStream(jsonFilter.getBytes()))
        );

        when(validatorMock.validate(any())).thenReturn(List.of("Name is required", "Price must be positive"));

        servlet.doPost(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String response = responseWriter.toString();

        assertEquals("{\"validationErrors\": [\"Name is required\",\"Price must be positive\"]}", response);
    }


    public static class DelegatingServletInputStream extends jakarta.servlet.ServletInputStream {
        private final InputStream sourceStream;

        public DelegatingServletInputStream(InputStream sourceStream) {
            this.sourceStream = sourceStream;
        }

        @Override
        public int read() throws IOException {
            return sourceStream.read();
        }

        @Override
        public boolean isFinished() {
            try {
                return sourceStream.available() == 0;
            } catch (IOException e) {
                return true;
            }
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(jakarta.servlet.ReadListener readListener) {
        }
    }
}
