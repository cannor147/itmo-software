package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.BeforeEach;
import ru.akirakozov.sd.refactoring.dao.ProductDao;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.mock;

public abstract class AbstractServletTest<S> {
    private final ProductDao productDao;

    protected S servlet;

    protected AbstractServletTest() {
        this.productDao = mock(ProductDao.class);
    }

    @BeforeEach
    public final void initializeServlet() {
        servlet = createServlet();
    }

    protected abstract S createServlet();

    protected S getServlet() {
        return servlet;
    }

    public ProductDao getProductDao() {
        return productDao;
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
