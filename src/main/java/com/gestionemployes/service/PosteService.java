package com.gestionemployes.service;

import java.util.List;
import java.util.Map;

import com.gestionemployes.dao.EmployeDAO;
import com.gestionemployes.dao.PosteDAO;
import com.gestionemployes.model.Employe;
import com.gestionemployes.model.Poste;

public class PosteService {

    private final PosteDAO dao = new PosteDAO();
    private final EmployeDAO employeDAO = new EmployeDAO();

    public List<Poste> lister() {
        return dao.tous();
    }

    public Poste trouver(Long id) {
        return dao.trouver(id);
    }

    public long compter() {
        return dao.compter();
    }

    public Map<Long, Long> effectifs() {
        return employeDAO.effectifsParPoste();
    }

    public List<Employe> employes(Long idPoste) {
        return employeDAO.rechercher(null, null, idPoste);
    }

    public String enregistrer(Poste p) {

        if (p.getIntitule().isEmpty()) {
            return "L'intitulé du poste est obligatoire.";
        }
        if (p.getIntitule().length() > 100) {
            return "L'intitulé du poste est trop long (100 caractères maximum).";
        }
        if (p.getDescription().length() > 500) {
            return "La description est trop longue (500 caractères maximum).";
        }

        Poste existant = dao.trouverParIntitule(p.getIntitule());
        if (existant != null && !existant.getIdPoste().equals(p.getIdPoste())) {
            return "Un poste portant cet intitulé existe déjà.";
        }

        if (p.getIdPoste() == null) {
            dao.ajouter(p);
        } else if (dao.trouver(p.getIdPoste()) == null) {
            return "Poste introuvable.";
        } else {
            dao.modifier(p);
        }
        return null;
    }

    public String supprimer(Long id) {

        if (dao.trouver(id) == null) {
            return "Poste introuvable.";
        }
        if (employeDAO.compterParPoste(id) > 0) {
            return "Suppression impossible : ce poste est attribué à des employés.";
        }
        dao.supprimer(id);
        return null;
    }
}
