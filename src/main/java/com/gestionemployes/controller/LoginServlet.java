package com.gestionemployes.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login-page", "/login"})
public class LoginServlet extends HttpServlet {

    private static final String LOGIN = "admin";
    private static final String MOT_DE_PASSE = "admin123";
    private static final String VUE = "/WEB-INF/views/auth/login.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("utilisateur") != null) {
            response.sendRedirect(request.getContextPath() + "/employes");
            return;
        }

        request.getRequestDispatcher(VUE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        login = (login == null) ? "" : login.trim();

        if (LOGIN.equals(login) && MOT_DE_PASSE.equals(password)) {

            HttpSession ancienne = request.getSession(false);
            if (ancienne != null) {
                ancienne.invalidate();
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("utilisateur", login);

            response.sendRedirect(request.getContextPath() + "/employes");

        } else {
            request.setAttribute("erreur", "Identifiant ou mot de passe incorrect.");
            request.getRequestDispatcher(VUE).forward(request, response);
        }
    }
}