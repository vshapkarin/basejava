package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.SqlStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class ResumeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String uuid = request.getParameter("uuid");
        Map<String, Resume> resumes = new SqlStorage(getInitParameter("dbURL"), getInitParameter("dbUser"), getInitParameter("dbPassword")).getAllSorted()
                .stream()
                .collect(Collectors.toMap(Resume::getUuid, r -> r));

        response.getWriter().write("<table border = \"1\">");
        if (uuid == null) {
            for (Map.Entry<String, Resume> entry : resumes.entrySet()) {
                printLine(entry.getKey(), entry.getValue().getFullName(), response);
            }
        } else {
            printLine(uuid, resumes.get(uuid).getFullName(), response);
        }
        response.getWriter().write("</table>");
    }

    private void printLine(String uuid, String fullName, HttpServletResponse response) throws IOException {
        response.getWriter().write("<tr>");

        response.getWriter().write("<td>");
        response.getWriter().write(uuid);
        response.getWriter().write("</td>");

        response.getWriter().write("<td>");
        response.getWriter().write(fullName);
        response.getWriter().write("</td>");

        response.getWriter().write("</tr>");
    }
}