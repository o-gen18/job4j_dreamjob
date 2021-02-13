package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.MemCandidateStore;
import ru.job4j.dream.store.PsqlCandidateStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CandidateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        req.setAttribute("candidates", PsqlCandidateStore.instOf().findAll());
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        PsqlCandidateStore.instOf().save(
                new Candidate(
                        Integer.valueOf(req.getParameter("id")),
                        req.getParameter("name"),
                        req.getParameter("photoId"),
                        req.getParameter("city")));
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}