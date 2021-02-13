package ru.job4j.dream.servlet;

import ru.job4j.dream.store.PsqlCandidateStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CityServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PsqlCandidateStore store = (PsqlCandidateStore) PsqlCandidateStore.instOf();
        String cities = store.getCities();
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        writer.println("{\"cities\":" + cities + "}");
        writer.flush();
    }
}
