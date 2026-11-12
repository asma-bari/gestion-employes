package com.gestionemployes.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

import com.gestionemployes.dao.EmployeDAO;
import com.gestionemployes.model.Employe;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "EmployeServlet", urlPatterns = {"/employes"})
public class EmployeServlet extends HttpServlet {

    private static final String LISTE = "/WEB-INF/views/employes/liste.jsp";
    private static final String FORMULAIRE = "/WEB-INF/views/employes/formulaire.jsp";
    private static final String DETAILS = "/WEB-INF/views/employes/details.jsp";

    private static final Pattern EMAIL =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern TELEPHONE =
            Pattern.compile("^[0-9+()\\s.-]{6,20}$");

    private final EmployeDAO dao = new EmployeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "liste";
        }

        switch (action) {

            case "nouveau" ->
                request.getRequestDispatcher(FORMULAIRE).forward(request, response);

            case "modifier", "details" -> {
                Employe employe = dao.rechercherParId(parseId(request.getParameter("id")));

                if (employe == null) {
                    flash(request, "Employé introuvable.");
                    response.sendRedirect(request.getContextPath() + "/employes");
                    return;
                }

                request.setAttribute("employe", employe);
                request.getRequestDispatcher(
                        "modifier".equals(action) ? FORMULAIRE : DETAILS)
                        .forward(request, response);
            }

            default -> {
                String q = request.getParameter("q");
                q = (q == null) ? "" : q.trim();

                List<Employe> employes = q.isEmpty()
                        ? dao.afficherTous()
                        : dao.rechercher(q);

                recupererMessage(request);
                request.setAttribute("employes", employes);
                request.setAttribute("q", q);
                request.getRequestDispatcher(LISTE).forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("supprimer".equals(action)) {
            dao.supprimer(parseId(request.getParameter("id")));
            flash(request, "Employé supprimé avec succès.");
            response.sendRedirect(request.getContextPath() + "/employes");

        } else if ("enregistrer".equals(action)) {
            enregistrer(request, response);

        } else {
            response.sendRedirect(request.getContextPath() + "/employes");
        }
    }

    private void enregistrer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long id = parseId(request.getParameter("idEmploye"));
        String dateTexte = valeur(request, "dateRecrutement");

        Employe employe = new Employe();
        employe.setIdEmploye(id);
        employe.setMatricule(valeur(request, "matricule"));
        employe.setNom(valeur(request, "nom"));
        employe.setPrenom(valeur(request, "prenom"));
        employe.setEmail(valeur(request, "email").toLowerCase());
        employe.setTelephone(valeur(request, "telephone"));
        employe.setAdresse(valeur(request, "adresse"));

        try {
            if (!dateTexte.isEmpty()) {
                employe.setDateRecrutement(LocalDate.parse(dateTexte));
            }
        } catch (DateTimeParseException e) {
            employe.setDateRecrutement(null);
        }

        String erreur = valider(employe, dateTexte);

        if (erreur == null) {
            try {
                if (id == null) {
                    dao.ajouter(employe);
                    flash(request, "Employé ajouté avec succès.");

                } else if (dao.rechercherParId(id) == null) {
                    flash(request, "Employé introuvable.");

                } else {
                    dao.modifier(employe);
                    flash(request, "Employé modifié avec succès.");
                }

                response.sendRedirect(request.getContextPath() + "/employes");
                return;

            } catch (RuntimeException e) {
                getServletContext().log("Erreur d'enregistrement", e);
                erreur = "Erreur lors de l'enregistrement. Vérifiez les données.";
            }
        }

        request.setAttribute("erreur", erreur);
        request.setAttribute("employe", employe);
        request.getRequestDispatcher(FORMULAIRE).forward(request, response);
    }

    private String valider(Employe e, String dateTexte) {

        if (e.getMatricule().isEmpty() || e.getNom().isEmpty()
                || e.getPrenom().isEmpty() || e.getEmail().isEmpty()
                || dateTexte.isEmpty()) {
            return "Tous les champs marqués d'un * sont obligatoires.";
        }

        if (e.getMatricule().length() > 30 || e.getNom().length() > 100
                || e.getPrenom().length() > 100 || e.getEmail().length() > 150
                || e.getAdresse().length() > 255) {
            return "Une des valeurs saisies est trop longue.";
        }

        if (!EMAIL.matcher(e.getEmail()).matches()) {
            return "Le format de l'e-mail est invalide.";
        }

        if (!e.getTelephone().isEmpty()
                && !TELEPHONE.matcher(e.getTelephone()).matches()) {
            return "Le numéro de téléphone est invalide.";
        }

        if (e.getDateRecrutement() == null) {
            return "La date de recrutement est invalide.";
        }

        if (e.getDateRecrutement().isAfter(LocalDate.now())) {
            return "La date de recrutement ne peut pas être dans le futur.";
        }

        Employe parMatricule = dao.rechercherParMatricule(e.getMatricule());
        if (parMatricule != null
                && !parMatricule.getIdEmploye().equals(e.getIdEmploye())) {
            return "Ce matricule existe déjà.";
        }

        Employe parEmail = dao.rechercherParEmail(e.getEmail());
        if (parEmail != null
                && !parEmail.getIdEmploye().equals(e.getIdEmploye())) {
            return "Cet e-mail est déjà utilisé.";
        }

        return null;
    }

    private Long parseId(String s) {
        try {
            return (s == null || s.isBlank()) ? null : Long.valueOf(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String valeur(HttpServletRequest request, String nom) {
        String v = request.getParameter(nom);
        return (v == null) ? "" : v.trim();
    }

    private void flash(HttpServletRequest request, String message) {
        request.getSession().setAttribute("message", message);
    }

    private void recupererMessage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("message") != null) {
            request.setAttribute("message", session.getAttribute("message"));
            session.removeAttribute("message");
        }
    }
}