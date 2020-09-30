package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetProductsServletTest extends AbstractServletTest<GetProductsServlet> {
    public static final Product BOOK = new Product("book", 599);
    public static final Product GLOBE = new Product("globe", 1499);
    public static final Product PEN = new Product("pen", 39);

    @Override
    protected GetProductsServlet createServlet() {
        return new GetProductsServlet(getProductDao());
    }

    @Test
    void testEmpty() throws IOException, SQLException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        TestWriter testWriter = new TestWriter();
        when(response.getWriter()).thenReturn(testWriter);
        when(getProductDao().findAll()).thenReturn(Collections.emptyList());

        getServlet().doGet(request, response);
        assertThat(testWriter.toString()).isEqualToIgnoringWhitespace("<html><body></body></html>");
    }

    @Test
    void testOneElement() throws IOException, SQLException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        TestWriter testWriter = new TestWriter();
        when(response.getWriter()).thenReturn(testWriter);
        when(getProductDao().findAll()).thenReturn(List.of(BOOK));

        getServlet().doGet(request, response);
        assertThat(testWriter.toString()).isEqualToIgnoringWhitespace("<html><body>book 599</br></body></html>");
    }

    @Test
    void testManyElements() throws IOException, SQLException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        TestWriter testWriter = new TestWriter();
        when(response.getWriter()).thenReturn(testWriter);
        when(getProductDao().findAll()).thenReturn(List.of(BOOK, GLOBE, PEN));

        getServlet().doGet(request, response);
        assertThat(testWriter.toString()).isEqualToIgnoringWhitespace("<html><body>" +
                "book 599</br> globe 1499</br> pen 39</br> </body></html>");
    }
}
