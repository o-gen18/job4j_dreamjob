package ru.job4j.dream.servlet;

import ru.job4j.dream.store.PsqlCandidateStore;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@MultipartConfig
public class UploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<String> images = new ArrayList<>();
        for (File name : new File("C:\\projects\\job4j_dreamjob\\images").listFiles()) {
            images.add(name.getName());
        }
        req.setAttribute("images", images);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/candidate/edit.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Part filePart = req.getPart("file");
        if (filePart.getSize() == 0) {
            req.setAttribute("photoId", req.getParameter("photoId"));
            req.getRequestDispatcher("/candidates.do").forward(req, resp);
            return;
        }
        File folder = new File("C:\\projects\\job4j_dreamjob\\images");
        if (!folder.exists()) {
            folder.mkdir();
        }
        PsqlCandidateStore store = (PsqlCandidateStore) PsqlCandidateStore.instOf();
        String photoId = store.savePhoto(filePart.getSubmittedFileName());
        File file = new File(folder + File.separator + photoId);
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(filePart.getInputStream().readAllBytes());
        }
        req.setAttribute("photoId", photoId);
        req.getRequestDispatcher("/candidates.do").forward(req, resp);
    }
}
