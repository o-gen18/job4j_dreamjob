package ru.job4j.dream.servlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.store.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlPostStore.class)
public class PostServletTest {

    @Test
    public void whenAddPostThenStoreIt() throws ServletException, IOException {
        Store store = new PsqlPostStoreStub();
        PowerMockito.mockStatic(PsqlPostStore.class);
        Mockito.when(PsqlPostStore.instOf()).thenReturn(store);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getParameter("name")).thenReturn("Oleg Generalov");
        new PostServlet().doPost(req, resp);
        assertThat(store.findAll().iterator().next().getName(), is("Oleg Generalov"));
    }
}