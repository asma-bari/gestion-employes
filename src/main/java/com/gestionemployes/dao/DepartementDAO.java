package com.gestionemployes.dao;

import java.util.List;

import org.hibernate.Session;

import com.gestionemployes.model.Departement;

public class DepartementDAO extends BaseDAO<Departement> {

    public static final String HQL_TOUS = "FROM Departement d ORDER BY d.nom";
    public static final String HQL_PAR_NOM = "FROM Departement d WHERE lower(d.nom) = :nom";

    public DepartementDAO() {
        super(Departement.class);
    }

    @Override
    public List<Departement> tous() {
        try (Session session = sessionFactory().openSession()) {
            return session.createQuery(HQL_TOUS, Departement.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'affichage des départements", e);
        }
    }

    public Departement trouverParNom(String nom) {
        try (Session session = sessionFactory().openSession()) {
            return session.createQuery(HQL_PAR_NOM, Departement.class)
                    .setParameter("nom", nom.toLowerCase())
                    .setMaxResults(1)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche du département", e);
        }
    }
}
