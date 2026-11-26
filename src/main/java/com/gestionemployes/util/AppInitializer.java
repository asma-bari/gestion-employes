package com.gestionemployes.util;

import java.time.LocalDate;

import com.gestionemployes.dao.DepartementDAO;
import com.gestionemployes.dao.EmployeDAO;
import com.gestionemployes.dao.PosteDAO;
import com.gestionemployes.model.Departement;
import com.gestionemployes.model.Employe;
import com.gestionemployes.model.Poste;
import com.gestionemployes.service.AuthService;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppInitializer implements ServletContextListener {

    private static final String[][] DEPARTEMENTS = {
        {"Informatique", "Développement et maintenance des systèmes"},
        {"Ressources Humaines", "Gestion du personnel et recrutement"},
        {"Finance", "Gestion financière et comptabilité"},
        {"Marketing", "Communication et stratégie commerciale"},
        {"Administration", "Gestion administrative de l'entreprise"}
    };

    private static final String[][] POSTES = {
        {"Développeur Java", "Développement d'applications Java"},
        {"Développeur Web", "Développement web"},
        {"Administrateur systèmes", "Gestion des serveurs"},
        {"Chef de projet IT", "Coordination des projets informatiques"},
        {"Responsable RH", "Gestion des ressources humaines"},
        {"Chargé de recrutement", "Recrutement des employés"},
        {"Comptable", "Gestion de la comptabilité"},
        {"Responsable financier", "Gestion financière"},
        {"Responsable marketing", "Stratégie marketing"},
        {"Assistant administratif", "Gestion des tâches administratives"}
    };

    private static final String[] DEPARTEMENT_DU_POSTE = {
        "Informatique", "Informatique", "Informatique", "Informatique",
        "Ressources Humaines", "Ressources Humaines",
        "Finance", "Finance",
        "Marketing",
        "Administration"
    };

    private static final String[][] EMPLOYES = {
        {"EMP001", "Mohamed", "El Mansouri", "mohamed.elmansouri", "2019-09-02"},
        {"EMP002", "Fatima Zahra", "Benjelloun", "fatimazahra.benjelloun", "2020-01-13"},
        {"EMP003", "Anas", "El Amrani", "anas.elamrani", "2020-03-16"},
        {"EMP004", "Ikram", "Boulahya", "ikram.boulahya", "2020-06-01"},
        {"EMP005", "Hamza", "El Gharbi", "hamza.elgharbi", "2020-09-14"},
        {"EMP006", "Chaimae", "Ait Lahcen", "chaimae.aitlahcen", "2021-01-11"},
        {"EMP007", "Rayan", "Belkadi", "rayan.belkadi", "2021-02-22"},
        {"EMP008", "Mariam", "El Idrissi", "mariam.elidrissi", "2021-04-05"},
        {"EMP009", "Othmane", "Bennis", "othmane.bennis", "2021-06-21"},
        {"EMP010", "Salma", "Ouazzani", "salma.ouazzani", "2021-09-06"},
        {"EMP011", "Ayoub", "Chafai", "ayoub.chafai", "2021-11-15"},
        {"EMP012", "Kenza", "El Fadili", "kenza.elfadili", "2022-01-10"},
        {"EMP013", "Soufiane", "Lamrani", "soufiane.lamrani", "2022-03-07"},
        {"EMP014", "Doha", "Benomar", "doha.benomar", "2022-05-23"},
        {"EMP015", "Yassine", "El Khatib", "yassine.elkhatib", "2022-09-12"},
        {"EMP016", "Nisrine", "Berrichi", "nisrine.berrichi", "2023-01-09"},
        {"EMP017", "Mehdi", "Azzouzi", "mehdi.azzouzi", "2023-03-20"},
        {"EMP018", "Ghita", "El Malki", "ghita.elmalki", "2023-06-05"},
        {"EMP019", "Bilal", "Saadani", "bilal.saadani", "2023-09-18"},
        {"EMP020", "Sara", "El Khayat", "sara.elkhayat", "2024-01-08"}
    };

    @Override
    public void contextInitialized(ServletContextEvent event) {

        try {
            HibernateUtil.getSessionFactory();

            new AuthService().initialiserAdministrateur();
            insererDonneesDeDepart();

            event.getServletContext().log(
                    "Application initialisée. Connexion : admin / admin123");

        } catch (Throwable t) {
            event.getServletContext().log(
                    "ERREUR : impossible d'initialiser la base de données. "
                    + "Vérifiez que MySQL est démarré et que l'utilisateur / le mot de passe "
                    + "de hibernate.cfg.xml sont corrects.", t);
            throw new IllegalStateException("Initialisation impossible", t);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        HibernateUtil.shutdown();
    }

    private void insererDonneesDeDepart() {

        DepartementDAO departementDAO = new DepartementDAO();
        if (departementDAO.compter() == 0) {
            for (String[] d : DEPARTEMENTS) {
                departementDAO.ajouter(departement(d[0], d[1]));
            }
        }

        PosteDAO posteDAO = new PosteDAO();
        if (posteDAO.compter() == 0) {
            for (String[] p : POSTES) {
                posteDAO.ajouter(poste(p[0], p[1]));
            }
        }

        EmployeDAO employeDAO = new EmployeDAO();
        if (employeDAO.compter() == 0) {
            for (int i = 0; i < EMPLOYES.length; i++) {
                int indexPoste = i / 2;
                Departement departement = departementDAO.trouverParNom(DEPARTEMENT_DU_POSTE[indexPoste]);
                Poste poste = posteDAO.trouverParIntitule(POSTES[indexPoste][0]);
                if (departement != null && poste != null) {
                    employeDAO.ajouter(employe(EMPLOYES[i], departement, poste));
                }
            }
        }
    }

    private Departement departement(String nom, String description) {
        Departement d = new Departement();
        d.setNom(nom);
        d.setDescription(description);
        return d;
    }

    private Poste poste(String intitule, String description) {
        Poste p = new Poste();
        p.setIntitule(intitule);
        p.setDescription(description);
        return p;
    }

    private Employe employe(String[] donnees, Departement departement, Poste poste) {
        Employe e = new Employe();
        e.setMatricule(donnees[0]);
        e.setPrenom(donnees[1]);
        e.setNom(donnees[2]);
        e.setEmail(donnees[3] + "@gmail.com");
        e.setDateRecrutement(LocalDate.parse(donnees[4]));
        e.setDepartement(departement);
        e.setPoste(poste);
        return e;
    }
}
