package vasyurin.work.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import vasyurin.work.dto.Product;
import vasyurin.work.services.SaveService;

import java.io.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class ProductServletCreateTest {

    private ProductServletCreate servlet;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private SaveService saveServiceMock;
    private ObjectMapper mapper = new ObjectMapper();
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws IOException {
        saveServiceMock = mock(SaveService.class);
        servlet = new ProductServletCreate(saveServiceMock);

        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);

        responseWriter = new StringWriter();
        when(resp.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    void testDoPost_SaveProduct() throws Exception {
        Product product = new Product();
        product.setName("TestProduct");
        product.setPrice(java.math.BigDecimal.valueOf(123));

        String json = mapper.writeValueAsString(product);

        when(req.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        servlet.doPost(req, resp);

        verify(saveServiceMock, times(1)).save(product);

        verify(resp).setStatus(HttpServletResponse.SC_CREATED);

        String responseJson = responseWriter.toString();
        Product responseProduct = mapper.readValue(responseJson, Product.class);
        assertEquals("TestProduct", responseProduct.getName());
        assertEquals(java.math.BigDecimal.valueOf(123), responseProduct.getPrice());
    }

}
