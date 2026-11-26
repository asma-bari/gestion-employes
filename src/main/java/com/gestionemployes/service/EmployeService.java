package com.gestionemployes.service;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

import com.gestionemployes.dao.DepartementDAO;
import com.gestionemployes.dao.EmployeDAO;
import com.gestionemployes.dao.PosteDAO;
import com.gestionemployes.model.Departement;
import com.gestionemployes.model.Employe;
import com.gestionemployes.model.Poste;

public class EmployeService {

    private static final Pattern EMAIL =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern TELEPHONE =
            Pattern.compile("^[0-9+()\\s.-]{6,20}$");

    private final EmployeDAO dao = new EmployeDAO();
    private final DepartementDAO departementDAO = new DepartementDAO();
    private final PosteDAO posteDAO = new PosteDAO();

    public List<Employe> rechercher(String terme, Long idDepartement, Long idPoste) {
        return dao.rechercher(terme, idDepartement, idPoste);
    }

    public Employe trouver(Long id) {
        return dao.trouver(id);
    }

    public long compter() {
        return dao.compter();
    }

    public String enregistrer(Employe e, Long idDepartement, Long idPoste) {

        String erreur = valider(e);
        if (erreur != null) {
            return erreur;
        }

        if (idDepartement == null) {
            return "Veuillez choisir un département.";
        }
        if (idPoste == null) {
            return "Veuillez choisir un poste.";
        }

        Departement departement = departementDAO.trouver(idDepartement);
        if (departement == null) {
            return "Le département sélectionné n'existe pas.";
        }

        Poste poste = posteDAO.trouver(idPoste);
        if (poste == null) {
            return "Le poste sélectionné n'existe pas.";
        }

        e.setDepartement(departement);
        e.setPoste(poste);

        if (e.getIdEmploye() == null) {
            dao.ajouter(e);
        } else if (dao.trouver(e.getIdEmploye()) == null) {
            return "Employé introuvable.";
        } else {
            dao.modifier(e);
        }
        return null;
    }

    public void supprimer(Long id) {
        dao.supprimer(id);
    }

    private String valider(Employe e) {

        if (e.getMatricule().isEmpty() || e.getNom().isEmpty()
                || e.getPrenom().isEmpty() || e.getEmail().isEmpty()
                || e.getDateRecrutement() == null) {
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

        if (e.getDateRecrutement().isAfter(LocalDate.now())) {
            return "La date de recrutement ne peut pas être dans le futur.";
        }

        Employe parMatricule = dao.trouverParMatricule(e.getMatricule());
        if (parMatricule != null
                && !parMatricule.getIdEmploye().equals(e.getIdEmploye())) {
            return "Ce matricule existe déjà.";
        }

        Employe parEmail = dao.trouverParEmail(e.getEmail());
        if (parEmail != null
                && !parEmail.getIdEmploye().equals(e.getIdEmploye())) {
            return "Cet e-mail est déjà utilisé.";
        }

        return null;
    }
}
