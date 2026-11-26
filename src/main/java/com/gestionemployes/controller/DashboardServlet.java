package com.gestionemployes.controller;

import java.io.IOException;

import com.gestionemployes.service.DepartementService;
import com.gestionemployes.service.EmployeService;
import com.gestionemployes.service.PosteService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends BaseServlet {

    private final EmployeService employeService = new EmployeService();
    private final DepartementService departementService = new DepartementService();
    private final PosteService posteService = new PosteService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("nbEmployes", employeService.compter());
        request.setAttribute("nbDepartements", departementService.compter());
        request.setAttribute("nbPostes", posteService.compter());
        request.setAttribute("departements", departementService.lister());
        request.setAttribute("effectifs", departementService.effectifs());

        afficher(request, response, "/WEB-INF/views/dashboard.jsp");
    }
}
