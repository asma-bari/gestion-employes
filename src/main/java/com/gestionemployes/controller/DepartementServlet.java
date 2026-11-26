package com.gestionemployes.controller;

import java.io.IOException;

import com.gestionemployes.model.Departement;
import com.gestionemployes.service.DepartementService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "DepartementServlet", urlPatterns = {"/departements"})
public class DepartementServlet extends BaseServlet {

    private static final String LISTE = "/WEB-INF/views/departements/liste.jsp";
    private static final String FORMULAIRE = "/WEB-INF/views/departements/formulaire.jsp";
    private static final String DETAILS = "/WEB-INF/views/departements/details.jsp";

    private final DepartementService service = new DepartementService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "liste";
        }

        switch (action) {

            case "nouveau" -> afficher(request, response, FORMULAIRE);

            case "modifier", "details" -> {
                Departement departement = service.trouver(parseId(request.getParameter("id")));

                if (departement == null) {
                    erreur(request, "Département introuvable.");
                    rediriger(request, response, "/departements");
                    return;
                }

                request.setAttribute("departement", departement);

                if ("modifier".equals(action)) {
                    afficher(request, response, FORMULAIRE);
                } else {
                    request.setAttribute("employes", service.employes(departement.getIdDepartement()));
                    afficher(request, response, DETAILS);
                }
            }

            default -> {
                request.setAttribute("departements", service.lister());
                request.setAttribute("effectifs", service.effectifs());
                afficher(request, response, LISTE);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("supprimer".equals(action)) {
            String erreur;
            try {
                erreur = service.supprimer(parseId(request.getParameter("id")));
            } catch (RuntimeException e) {
                getServletContext().log("Erreur de suppression du département", e);
                erreur = "Erreur lors de la suppression du département.";
            }

            if (erreur == null) {
                succes(request, "Département supprimé avec succès.");
            } else {
                erreur(request, erreur);
            }
            rediriger(request, response, "/departements");

        } else if ("enregistrer".equals(action)) {
            enregistrer(request, response);

        } else {
            rediriger(request, response, "/departements");
        }
    }

    private void enregistrer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long id = parseId(request.getParameter("idDepartement"));

        Departement departement = new Departement();
        departement.setIdDepartement(id);
        departement.setNom(valeur(request, "nom"));
        departement.setDescription(valeur(request, "description"));

        String erreur;
        try {
            erreur = service.enregistrer(departement);
        } catch (RuntimeException e) {
            getServletContext().log("Erreur d'enregistrement du département", e);
            erreur = "Erreur lors de l'enregistrement. Vérifiez les données.";
        }

        if (erreur == null) {
            succes(request, id == null ? "Département ajouté avec succès." : "Département modifié avec succès.");
            rediriger(request, response, "/departements");
            return;
        }

        request.setAttribute("erreur", erreur);
        request.setAttribute("departement", departement);
        afficher(request, response, FORMULAIRE);
    }
}
