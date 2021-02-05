package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlUserStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            req.getRequestDispatcher("/reg.jsp").forward(req, resp);
            return;
        }
        User user = (User) PsqlUserStore.instOf().save(new User(name, email, password));
        if (user.getId() == 0) {
            req.setAttribute("error", "Такая почта уже существует, введите другую!");
            req.getRequestDispatcher("/reg.jsp").forward(req, resp);
        } else {
            HttpSession sc = req.getSession();
            sc.setAttribute("user", user);
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }
}
