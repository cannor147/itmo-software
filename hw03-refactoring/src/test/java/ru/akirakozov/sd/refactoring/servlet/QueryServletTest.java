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

public class QueryServletTest extends AbstractServletTest<QueryServlet> {
    @Override
    protected QueryServlet createServlet() {
        return new QueryServlet();
    }

    @Test
    void testEmpty() throws IOException {
        testCommands("", "", 0, 0);
    }

    @Test
    void testOneElement() throws IOException, SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement statement = c.createStatement();
            statement.executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"book\", 599)");
        }
        testCommands("book 599</br>", "book 599</br>", 599, 1);
    }

    @Test
    void testManyElements() throws IOException, SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement statement = c.createStatement();
            statement.executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"book\", 599)");
            statement.executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"globe\", 1499)");
            statement.executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"pen\", 39)");
        }
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
