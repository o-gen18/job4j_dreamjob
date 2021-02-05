package ru.job4j.dream.servlet;

import ru.job4j.dream.store.PsqlCandidateStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        if (PsqlCandidateStore.instOf().delete(Integer.parseInt(req.getParameter("id")))) {
            if (req.getParameter("photoId") != null) {
                PsqlCandidateStore.instOf().deletePhoto(req.getParameter("photoId"));
            }
        }
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}