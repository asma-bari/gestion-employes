package com.gestionemployes.dao;

import java.util.List;
import java.util.function.Consumer;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.gestionemployes.util.HibernateUtil;

public abstract class BaseDAO<T> {

    private final Class<T> type;

    protected BaseDAO(Class<T> type) {
        this.type = type;
    }

    protected SessionFactory sessionFactory() {
        return HibernateUtil.getSessionFactory();
    }

    public void ajouter(T objet) {
        dansTransaction(session -> session.persist(objet));
    }

    public void modifier(T objet) {
        dansTransaction(session -> session.merge(objet));
    }

    public void supprimer(Long id) {
        if (id == null) {
            return;
        }
        dansTransaction(session -> {
            T objet = session.find(type, id);
            if (objet != null) {
                session.remove(objet);
            }
        });
    }

    public T trouver(Long id) {
        if (id == null) {
            return null;
        }
        try (Session session = sessionFactory().openSession()) {
            return session.find(type, id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la lecture de " + type.getSimpleName(), e);
        }
    }

    public List<T> tous() {
        try (Session session = sessionFactory().openSession()) {
            return session.createQuery("FROM " + type.getSimpleName(), type).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la lecture de " + type.getSimpleName(), e);
        }
    }

    public long compter() {
        try (Session session = sessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT COUNT(x) FROM " + type.getSimpleName() + " x", Long.class)
                    .getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du comptage de " + type.getSimpleName(), e);
        }
    }

    protected void dansTransaction(Consumer<Session> travail) {
        Transaction transaction = null;

        try (Session session = sessionFactory().openSession()) {
            transaction = session.beginTransaction();
            travail.accept(session);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erreur de persistance (" + type.getSimpleName() + ")", e);
        }
    }
}
