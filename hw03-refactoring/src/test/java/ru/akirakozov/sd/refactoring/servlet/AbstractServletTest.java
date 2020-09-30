package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractServletTest<S> {
    protected S servlet;

    @AfterEach
    @BeforeEach
    public final void cleanDatabase() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement statement = c.createStatement();
            statement.executeUpdate("DELETE FROM PRODUCT");
            statement.close();
        }
    }

    @BeforeEach
    public final void initializeServlet() {
        servlet = createServlet();
    }

    protected abstract S createServlet();

    protected S getServlet() {
        return servlet;
    }

    protected static class TestWriter extends PrintWriter {
        public TestWriter() {
            super(new StringWriter());
        }

        @Override
        public String toString() {
            return out.toString();
        }
    }
}
