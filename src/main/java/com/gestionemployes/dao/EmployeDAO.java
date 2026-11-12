package com.gestionemployes.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gestionemployes.model.Employe;
import com.gestionemployes.util.HibernateUtil;

public class EmployeDAO {

    public void ajouter(Employe employe) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();
            session.persist(employe);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erreur lors de l'ajout de l'employé", e);
        }
    }

    public List<Employe> afficherTous() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM Employe e ORDER BY e.nom, e.prenom", Employe.class)
                    .getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'affichage des employés", e);
        }
    }

    public List<Employe> rechercher(String terme) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM Employe e "
                    + "WHERE lower(e.matricule) LIKE :t "
                    + "OR lower(e.nom) LIKE :t "
                    + "OR lower(e.prenom) LIKE :t "
                    + "ORDER BY e.nom, e.prenom", Employe.class)
                    .setParameter("t", "%" + terme.toLowerCase() + "%")
                    .getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche des employés", e);
        }
    }

    public Employe rechercherParId(Long id) {

        if (id == null) {
            return null;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(Employe.class, id);

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche de l'employé", e);
        }
    }

    public Employe rechercherParMatricule(String matricule) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM Employe WHERE matricule = :matricule", Employe.class)
                    .setParameter("matricule", matricule)
                    .uniqueResult();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche par matricule", e);
        }
    }

    public Employe rechercherParEmail(String email) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "FROM Employe WHERE email = :email", Employe.class)
                    .setParameter("email", email)
                    .uniqueResult();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche par e-mail", e);
        }
    }

    public void modifier(Employe employe) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();
            session.merge(employe);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erreur lors de la modification de l'employé", e);
        }
    }

    public void supprimer(Long id) {

        if (id == null) {
            return;
        }

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            Employe employe = session.get(Employe.class, id);
            if (employe != null) {
                session.remove(employe);
            }

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erreur lors de la suppression de l'employé", e);
        }
    }
}