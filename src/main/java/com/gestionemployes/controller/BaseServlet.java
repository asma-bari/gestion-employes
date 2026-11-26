package com.gestionemployes.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class BaseServlet extends HttpServlet {

    protected static final String FLASH_SUCCES = "flash_ok";
    protected static final String FLASH_ERREUR = "flash_err";

    protected Long parseId(String s) {
        try {
            return (s == null || s.isBlank()) ? null : Long.valueOf(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    protected String valeur(HttpServletRequest request, String nom) {
        String v = request.getParameter(nom);
        return (v == null) ? "" : v.trim();
    }

    protected void succes(HttpServletRequest request, String message) {
        request.getSession().setAttribute(FLASH_SUCCES, message);
    }

    protected void erreur(HttpServletRequest request, String message) {
        request.getSession().setAttribute(FLASH_ERREUR, message);
    }

    protected void rediriger(HttpServletRequest request, HttpServletResponse response, String chemin)
            throws IOException {
        response.sendRedirect(request.getContextPath() + chemin);
    }

    protected void afficher(HttpServletRequest request, HttpServletResponse response, String vue)
            throws ServletException, IOException {
        request.getRequestDispatcher(vue).forward(request, response);
    }
}
