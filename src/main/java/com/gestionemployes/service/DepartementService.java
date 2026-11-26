package com.gestionemployes.service;

import java.util.List;
import java.util.Map;

import com.gestionemployes.dao.DepartementDAO;
import com.gestionemployes.dao.EmployeDAO;
import com.gestionemployes.model.Departement;
import com.gestionemployes.model.Employe;

public class DepartementService {

    private final DepartementDAO dao = new DepartementDAO();
    private final EmployeDAO employeDAO = new EmployeDAO();

    public List<Departement> lister() {
        return dao.tous();
    }

    public Departement trouver(Long id) {
        return dao.trouver(id);
    }

    public long compter() {
        return dao.compter();
    }

    public Map<Long, Long> effectifs() {
        return employeDAO.effectifsParDepartement();
    }

    public List<Employe> employes(Long idDepartement) {
        return employeDAO.rechercher(null, idDepartement, null);
    }

    public String enregistrer(Departement d) {

        if (d.getNom().isEmpty()) {
            return "Le nom du département est obligatoire.";
        }
        if (d.getNom().length() > 100) {
            return "Le nom du département est trop long (100 caractères maximum).";
        }
        if (d.getDescription().length() > 500) {
            return "La description est trop longue (500 caractères maximum).";
        }

        Departement existant = dao.trouverParNom(d.getNom());
        if (existant != null && !existant.getIdDepartement().equals(d.getIdDepartement())) {
            return "Un département portant ce nom existe déjà.";
        }

        if (d.getIdDepartement() == null) {
            dao.ajouter(d);
        } else if (dao.trouver(d.getIdDepartement()) == null) {
            return "Département introuvable.";
        } else {
            dao.modifier(d);
        }
        return null;
    }

    public String supprimer(Long id) {

        if (dao.trouver(id) == null) {
            return "Département introuvable.";
        }
        if (employeDAO.compterParDepartement(id) > 0) {
            return "Suppression impossible : ce département contient encore des employés.";
        }
        dao.supprimer(id);
        return null;
    }
}
