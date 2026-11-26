package com.gestionemployes.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.gestionemployes.model.Employe;

public class EmployeDAO extends BaseDAO<Employe> {

    public static final String HQL_PAR_MATRICULE = "FROM Employe e WHERE e.matricule = :matricule";
    public static final String HQL_PAR_EMAIL = "FROM Employe e WHERE e.email = :email";

    public static final String HQL_COMPTER_PAR_DEPARTEMENT =
            "SELECT COUNT(e) FROM Employe e JOIN e.departement d WHERE d.idDepartement = :id";
    public static final String HQL_COMPTER_PAR_POSTE =
            "SELECT COUNT(e) FROM Employe e JOIN e.poste p WHERE p.idPoste = :id";

    public static final String HQL_EFFECTIFS_DEPARTEMENTS =
            "SELECT d.idDepartement, COUNT(e) FROM Employe e JOIN e.departement d GROUP BY d.idDepartement";
    public static final String HQL_EFFECTIFS_POSTES =
            "SELECT p.idPoste, COUNT(e) FROM Employe e JOIN e.poste p GROUP BY p.idPoste";

    public EmployeDAO() {
        super(Employe.class);
    }

    public static String construireRecherche(int nbMots, boolean avecDepartement, boolean avecPoste) {

        List<String> conditions = new ArrayList<>();

        for (int i = 0; i < nbMots; i++) {
            conditions.add("(lower(e.matricule) LIKE :mot" + i
                    + " OR lower(e.nom) LIKE :mot" + i
                    + " OR lower(e.prenom) LIKE :mot" + i + ")");
        }
        if (avecDepartement) {
            conditions.add("d.idDepartement = :departement");
        }
        if (avecPoste) {
            conditions.add("p.idPoste = :poste");
        }

        StringBuilder hql = new StringBuilder(
                "SELECT e FROM Employe e LEFT JOIN FETCH e.departement d LEFT JOIN FETCH e.poste p");

        if (!conditions.isEmpty()) {
            hql.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        hql.append(" ORDER BY e.nom, e.prenom");
        return hql.toString();
    }

    public List<Employe> rechercher(String terme, Long idDepartement, Long idPoste) {

        String[] mots = (terme == null || terme.isBlank())
                ? new String[0]
                : terme.trim().toLowerCase().split("\\s+");

        try (Session session = sessionFactory().openSession()) {

            var requete = session.createQuery(
                    construireRecherche(mots.length, idDepartement != null, idPoste != null),
                    Employe.class);

            for (int i = 0; i < mots.length; i++) {
                requete.setParameter("mot" + i, "%" + mots[i] + "%");
            }
            if (idDepartement != null) {
                requete.setParameter("departement", idDepartement);
            }
            if (idPoste != null) {
                requete.setParameter("poste", idPoste);
            }

            return requete.getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche des employés", e);
        }
    }

    @Override
    public List<Employe> tous() {
        return rechercher(null, null, null);
    }

    public Employe trouverParMatricule(String matricule) {
        try (Session session = sessionFactory().openSession()) {
            return session.createQuery(HQL_PAR_MATRICULE, Employe.class)
                    .setParameter("matricule", matricule)
                    .setMaxResults(1)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche par matricule", e);
        }
    }

    public Employe trouverParEmail(String email) {
        try (Session session = sessionFactory().openSession()) {
            return session.createQuery(HQL_PAR_EMAIL, Employe.class)
                    .setParameter("email", email)
                    .setMaxResults(1)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche par e-mail", e);
        }
    }

    public long compterParDepartement(Long idDepartement) {
        return compterPar(HQL_COMPTER_PAR_DEPARTEMENT, idDepartement);
    }

    public long compterParPoste(Long idPoste) {
        return compterPar(HQL_COMPTER_PAR_POSTE, idPoste);
    }

    private long compterPar(String hql, Long id) {
        try (Session session = sessionFactory().openSession()) {
            return session.createQuery(hql, Long.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du comptage des employés", e);
        }
    }

    public Map<Long, Long> effectifsParDepartement() {
        return effectifs(HQL_EFFECTIFS_DEPARTEMENTS);
    }

    public Map<Long, Long> effectifsParPoste() {
        return effectifs(HQL_EFFECTIFS_POSTES);
    }

    private Map<Long, Long> effectifs(String hql) {
        try (Session session = sessionFactory().openSession()) {

            Map<Long, Long> resultat = new HashMap<>();

            for (Object[] ligne : session.createQuery(hql, Object[].class).getResultList()) {
                resultat.put((Long) ligne[0], (Long) ligne[1]);
            }
            return resultat;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du calcul des effectifs", e);
        }
    }
}
