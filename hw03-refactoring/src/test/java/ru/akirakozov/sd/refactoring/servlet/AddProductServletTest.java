package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddProductServletTest extends AbstractServletTest<AddProductServlet> {
    @Override
    protected AddProductServlet createServlet() {
        return new AddProductServlet(getProductDao());
    }

    @Test
    void testAdd() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        AbstractServletTest.TestWriter testWriter = new AbstractServletTest.TestWriter();
        when(response.getWriter()).thenReturn(testWriter);
        when(request.getParameter("name")).thenReturn("book");
        when(request.getParameter("price")).thenReturn("599");

        getServlet().doGet(request, response);
        assertThat(testWriter.toString()).isEqualToIgnoringWhitespace("OK");
    }
}
