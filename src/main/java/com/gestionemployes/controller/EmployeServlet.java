package com.gestionemployes.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.gestionemployes.model.Employe;
import com.gestionemployes.service.DepartementService;
import com.gestionemployes.service.EmployeService;
import com.gestionemployes.service.PosteService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "EmployeServlet", urlPatterns = {"/employes"})
public class EmployeServlet extends BaseServlet {

    private static final String LISTE = "/WEB-INF/views/employes/liste.jsp";
    private static final String FORMULAIRE = "/WEB-INF/views/employes/formulaire.jsp";
    private static final String DETAILS = "/WEB-INF/views/employes/details.jsp";

    private final EmployeService service = new EmployeService();
    private final DepartementService departementService = new DepartementService();
    private final PosteService posteService = new PosteService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "liste";
        }

        switch (action) {

            case "nouveau" -> {
                chargerListes(request);
                afficher(request, response, FORMULAIRE);
            }

            case "modifier", "details" -> {
                Employe employe = service.trouver(parseId(request.getParameter("id")));

                if (employe == null) {
                    erreur(request, "Employé introuvable.");
                    rediriger(request, response, "/employes");
                    return;
                }

                request.setAttribute("employe", employe);

                if ("modifier".equals(action)) {
                    chargerListes(request);
                    request.setAttribute("idDepartement",
                            employe.getDepartement() != null ? employe.getDepartement().getIdDepartement() : null);
                    request.setAttribute("idPoste",
                            employe.getPoste() != null ? employe.getPoste().getIdPoste() : null);
                    afficher(request, response, FORMULAIRE);
                } else {
                    afficher(request, response, DETAILS);
                }
            }

            default -> {
                String q = valeur(request, "q");
                Long idDepartement = parseId(request.getParameter("departement"));
                Long idPoste = parseId(request.getParameter("poste"));

                chargerListes(request);
                request.setAttribute("employes", service.rechercher(q, idDepartement, idPoste));
                request.setAttribute("q", q);
                request.setAttribute("filtreDepartement", idDepartement);
                request.setAttribute("filtrePoste", idPoste);
                afficher(request, response, LISTE);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("supprimer".equals(action)) {
            service.supprimer(parseId(request.getParameter("id")));
            succes(request, "Employé supprimé avec succès.");
            rediriger(request, response, "/employes");

        } else if ("enregistrer".equals(action)) {
            enregistrer(request, response);

        } else {
            rediriger(request, response, "/employes");
        }
    }

    private void enregistrer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long id = parseId(request.getParameter("idEmploye"));
        Long idDepartement = parseId(request.getParameter("departement"));
        Long idPoste = parseId(request.getParameter("poste"));

        Employe employe = new Employe();
        employe.setIdEmploye(id);
        employe.setMatricule(valeur(request, "matricule"));
        employe.setNom(valeur(request, "nom"));
        employe.setPrenom(valeur(request, "prenom"));
        employe.setEmail(valeur(request, "email").toLowerCase());
        employe.setTelephone(valeur(request, "telephone"));
        employe.setAdresse(valeur(request, "adresse"));

        String erreur;
        try {
            String dateTexte = valeur(request, "dateRecrutement");
            if (!dateTexte.isEmpty()) {
                employe.setDateRecrutement(LocalDate.parse(dateTexte));
            }
            erreur = service.enregistrer(employe, idDepartement, idPoste);

        } catch (DateTimeParseException e) {
            erreur = "La date de recrutement est invalide.";

        } catch (RuntimeException e) {
            getServletContext().log("Erreur d'enregistrement de l'employé", e);
            erreur = "Erreur lors de l'enregistrement. Vérifiez les données.";
        }

        if (erreur == null) {
            succes(request, id == null ? "Employé ajouté avec succès." : "Employé modifié avec succès.");
            rediriger(request, response, "/employes");
            return;
        }

        request.setAttribute("erreur", erreur);
        request.setAttribute("employe", employe);
        request.setAttribute("idDepartement", idDepartement);
        request.setAttribute("idPoste", idPoste);
        chargerListes(request);
        afficher(request, response, FORMULAIRE);
    }

    private void chargerListes(HttpServletRequest request) {
        request.setAttribute("departements", departementService.lister());
        request.setAttribute("postes", posteService.lister());
    }
}
