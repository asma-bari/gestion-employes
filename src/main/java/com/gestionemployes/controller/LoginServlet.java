package com.gestionemployes.controller;

import java.io.IOException;

import com.gestionemployes.dao.EmployeDAO;
import com.gestionemployes.model.Employe;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login-page", "/login"})
public class LoginServlet extends HttpServlet {

    private final EmployeDAO dao = new EmployeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Employe employe = dao.rechercherParEmail(email);

        if (employe != null && password != null
                && password.equals(employe.getMotDePasse())) {

            HttpSession session = request.getSession(true);
            session.setAttribute("employe", employe);
            response.sendRedirect(request.getContextPath() + "/employes");

        } else {
            request.setAttribute("erreur", "E-mail ou mot de passe incorrect.");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp")
                   .forward(request, response);
        }
    }
}