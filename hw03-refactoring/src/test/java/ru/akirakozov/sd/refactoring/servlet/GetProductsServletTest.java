package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetProductsServletTest extends AbstractServletTest<GetProductsServlet> {
    @Override
    protected GetProductsServlet createServlet() {
        return new GetProductsServlet();
    }

    @Test
    void testEmpty() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);

        HttpServletResponse response = mock(HttpServletResponse.class);
        TestWriter testWriter = new TestWriter();
        when(response.getWriter()).thenReturn(testWriter);

        getServlet().doGet(request, response);
        assertThat(testWriter.toString()).isEqualToIgnoringWhitespace("<html><body></body></html>");
    }

    @Test
    void testOneElement() throws IOException, SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement statement = c.createStatement();
            statement.executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"book\", 599)");
            statement.close();
        }

        HttpServletRequest request = mock(HttpServletRequest.class);

        HttpServletResponse response = mock(HttpServletResponse.class);
        TestWriter testWriter = new TestWriter();
        when(response.getWriter()).thenReturn(testWriter);

        getServlet().doGet(request, response);
        assertThat(testWriter.toString()).isEqualToIgnoringWhitespace("<html><body>book 599</br></body></html>");
    }

    @Test
    void testManyElements() throws IOException, SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement statement = c.createStatement();
            statement.executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"book\", 599)");
            statement.executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"globe\", 1499)");
            statement.executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"pen\", 39)");
            statement.close();
        }

        HttpServletRequest request = mock(HttpServletRequest.class);

        HttpServletResponse response = mock(HttpServletResponse.class);
        TestWriter testWriter = new TestWriter();
        when(response.getWriter()).thenReturn(testWriter);

        getServlet().doGet(request, response);
        assertThat(testWriter.toString()).isEqualToIgnoringWhitespace("<html><body>" +
                "book 599</br> globe 1499</br> pen 39</br> </body></html>");
    }
}
