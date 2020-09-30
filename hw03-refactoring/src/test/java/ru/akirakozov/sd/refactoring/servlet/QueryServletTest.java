package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QueryServletTest extends AbstractServletTest<QueryServlet> {
    public static final Product BOOK = new Product("book", 599);
    public static final Product GLOBE = new Product("globe", 1499);
    public static final Product PEN = new Product("pen", 39);

    @Override
    protected QueryServlet createServlet() {
        return new QueryServlet(getProductDao());
    }

    @Test
    void testEmpty() throws IOException, SQLException {
        when(getProductDao().findMax()).thenReturn(Optional.empty());
        when(getProductDao().findMin()).thenReturn(Optional.empty());
        when(getProductDao().sumPrices()).thenReturn(0);
        when(getProductDao().count()).thenReturn(0);
        testCommands("", "", 0, 0);
    }

    @Test
    void testOneElement() throws IOException, SQLException {
        when(getProductDao().findMax()).thenReturn(Optional.of(BOOK));
        when(getProductDao().findMin()).thenReturn(Optional.of(BOOK));
        when(getProductDao().sumPrices()).thenReturn(599);
        when(getProductDao().count()).thenReturn(1);
        testCommands("book 599</br>", "book 599</br>", 599, 1);
    }

    @Test
    void testManyElements() throws IOException, SQLException {
        when(getProductDao().findMax()).thenReturn(Optional.of(GLOBE));
        when(getProductDao().findMin()).thenReturn(Optional.of(PEN));
        when(getProductDao().sumPrices()).thenReturn(599 + 1499 + 39);
        when(getProductDao().count()).thenReturn(3);
        testCommands("globe 1499</br>", "pen 39</br>", 599 + 1499 + 39, 3);
    }

    private void testCommands(String maxProduct, String minProduct, int sum, int count) throws IOException {
        testCommand("max", "<html><body><h1>Product with max price:</h1>" + maxProduct + "</body></html>");
        testCommand("min", "<html><body><h1>Product with min price:</h1>" + minProduct + "</body></html>");
        testCommand("sum", "<html><body>Summary price:" + sum + "</body></html>");
        testCommand("count", "<html><body>Number of products:" + count + "</body></html>");
        testCommand("kek", "Unknown command: kek");
    }

    private void testCommand(String command, String expectedResponseBody) throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("command")).thenReturn(command);

        HttpServletResponse response = mock(HttpServletResponse.class);
        AbstractServletTest.TestWriter testWriter = new AbstractServletTest.TestWriter();
        when(response.getWriter()).thenReturn(testWriter);

        getServlet().doGet(request, response);
        assertThat(testWriter.toString()).isEqualToIgnoringWhitespace(expectedResponseBody);
    }
}
