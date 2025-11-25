package vasyurin.work.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vasyurin.work.dto.Product;
import vasyurin.work.services.SaveService;

import java.io.*;

import static org.mockito.Mockito.*;

public class ProductServletDeleteTest {

    private ProductServletDelete servlet;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private SaveService saveServiceMock;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws IOException {
        saveServiceMock = mock(SaveService.class);
        servlet = new ProductServletDelete(saveServiceMock);

        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);

        when(resp.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
    }

    @Test
    void testDoDelete_DeleteProduct() throws Exception {
        Product product = new Product();
        product.setName("TestProduct");
        product.setGtin(123);

        String json = mapper.writeValueAsString(product);
        when(req.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        servlet.doDelete(req, resp);

        verify(saveServiceMock, times(1)).delete(product);

        verify(resp).setStatus(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
    }
}
