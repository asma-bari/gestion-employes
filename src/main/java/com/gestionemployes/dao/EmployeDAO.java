package com.gestionemployes.dao;

import com.gestionemployes.model.Employe;
import com.gestionemployes.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

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
                    "FROM Employe", Employe.class
            ).getResultList();

        } catch (Exception e) {

            throw new RuntimeException("Erreur lors de l'affichage des employés", e);
        }
    }

    public Employe rechercherParId(int id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(Employe.class, id);

        } catch (Exception e) {

            throw new RuntimeException("Erreur lors de la recherche de l'employé", e);
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

    public void supprimer(int id) {

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