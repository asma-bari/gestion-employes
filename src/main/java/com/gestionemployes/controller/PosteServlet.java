package com.gestionemployes.controller;

import java.io.IOException;

import com.gestionemployes.model.Poste;
import com.gestionemployes.service.PosteService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "PosteServlet", urlPatterns = {"/postes"})
public class PosteServlet extends BaseServlet {

    private static final String LISTE = "/WEB-INF/views/postes/liste.jsp";
    private static final String FORMULAIRE = "/WEB-INF/views/postes/formulaire.jsp";
    private static final String DETAILS = "/WEB-INF/views/postes/details.jsp";

    private final PosteService service = new PosteService();

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
                Poste poste = service.trouver(parseId(request.getParameter("id")));

                if (poste == null) {
                    erreur(request, "Poste introuvable.");
                    rediriger(request, response, "/postes");
                    return;
                }

                request.setAttribute("poste", poste);

                if ("modifier".equals(action)) {
                    afficher(request, response, FORMULAIRE);
                } else {
                    request.setAttribute("employes", service.employes(poste.getIdPoste()));
                    afficher(request, response, DETAILS);
                }
            }

            default -> {
                request.setAttribute("postes", service.lister());
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
                getServletContext().log("Erreur de suppression du poste", e);
                erreur = "Erreur lors de la suppression du poste.";
            }

            if (erreur == null) {
                succes(request, "Poste supprimé avec succès.");
            } else {
                erreur(request, erreur);
            }
            rediriger(request, response, "/postes");

        } else if ("enregistrer".equals(action)) {
            enregistrer(request, response);

        } else {
            rediriger(request, response, "/postes");
        }
    }

    private void enregistrer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long id = parseId(request.getParameter("idPoste"));

        Poste poste = new Poste();
        poste.setIdPoste(id);
        poste.setIntitule(valeur(request, "intitule"));
        poste.setDescription(valeur(request, "description"));

        String erreur;
        try {
            erreur = service.enregistrer(poste);
        } catch (RuntimeException e) {
            getServletContext().log("Erreur d'enregistrement du poste", e);
            erreur = "Erreur lors de l'enregistrement. Vérifiez les données.";
        }

        if (erreur == null) {
            succes(request, id == null ? "Poste ajouté avec succès." : "Poste modifié avec succès.");
            rediriger(request, response, "/postes");
            return;
        }

        request.setAttribute("erreur", erreur);
        request.setAttribute("poste", poste);
        afficher(request, response, FORMULAIRE);
    }
}
