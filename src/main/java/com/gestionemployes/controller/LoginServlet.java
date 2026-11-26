package com.gestionemployes.controller;

import java.io.IOException;

import com.gestionemployes.model.Utilisateur;
import com.gestionemployes.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login-page", "/login"})
public class LoginServlet extends BaseServlet {

    private static final String VUE = "/WEB-INF/views/auth/login.jsp";

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("utilisateur") != null) {
            rediriger(request, response, "/dashboard");
            return;
        }

        afficher(request, response, VUE);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String login = valeur(request, "login");
        String motDePasse = request.getParameter("password");

        Utilisateur utilisateur = authService.authentifier(login, motDePasse);

        if (utilisateur == null) {
            request.setAttribute("erreur", "Identifiant ou mot de passe incorrect.");
            afficher(request, response, VUE);
            return;
        }

        HttpSession ancienne = request.getSession(false);
        if (ancienne != null) {
            ancienne.invalidate();
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("utilisateur", utilisateur.getLogin());
        session.setAttribute("role", utilisateur.getRole());

        rediriger(request, response, "/dashboard");
    }
}
