package com.gestionemployes.dao;

import org.hibernate.Session;

import com.gestionemployes.model.Utilisateur;

public class UtilisateurDAO extends BaseDAO<Utilisateur> {

    public static final String HQL_PAR_LOGIN = "FROM Utilisateur u WHERE u.login = :login";

    public UtilisateurDAO() {
        super(Utilisateur.class);
    }

    public Utilisateur trouverParLogin(String login) {
        try (Session session = sessionFactory().openSession()) {
            return session.createQuery(HQL_PAR_LOGIN, Utilisateur.class)
                    .setParameter("login", login)
                    .setMaxResults(1)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche de l'utilisateur", e);
        }
    }
}
