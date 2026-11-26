package com.gestionemployes.dao;

import java.util.List;

import org.hibernate.Session;

import com.gestionemployes.model.Poste;

public class PosteDAO extends BaseDAO<Poste> {

    public static final String HQL_TOUS = "FROM Poste p ORDER BY p.intitule";
    public static final String HQL_PAR_INTITULE = "FROM Poste p WHERE lower(p.intitule) = :intitule";

    public PosteDAO() {
        super(Poste.class);
    }

    @Override
    public List<Poste> tous() {
        try (Session session = sessionFactory().openSession()) {
            return session.createQuery(HQL_TOUS, Poste.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'affichage des postes", e);
        }
    }

    public Poste trouverParIntitule(String intitule) {
        try (Session session = sessionFactory().openSession()) {
            return session.createQuery(HQL_PAR_INTITULE, Poste.class)
                    .setParameter("intitule", intitule.toLowerCase())
                    .setMaxResults(1)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche du poste", e);
        }
    }
}
