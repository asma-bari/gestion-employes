# Gestion des employés, des départements et des postes

Projet Architecture JEE — ENSA Agadir (4ème année DLA).
Application web **Servlets / JSP / Hibernate (JPA) / MySQL** en architecture **MVC**.

## Prérequis

| Outil | Version |
|-------|---------|
| JDK | 17 ou plus |
| Maven | 3.8+ |
| Apache Tomcat | **10.1 ou plus** (Jakarta EE 10, Servlet 6.0 — Tomcat 9 ne convient pas) |
| MySQL | 8.x, démarré sur `localhost:3306` |

## Lancer l'application

1. Démarrer MySQL.
2. Configurer l'accès à MySQL :
   - Copier `src/main/resources/hibernate.cfg.xml.example` en `src/main/resources/hibernate.cfg.xml`
     (dossier `resources`, avec un seul « s »).
   - Dans ce nouveau fichier, remplacer `VOTRE_UTILISATEUR_MYSQL` et `VOTRE_MOT_DE_PASSE_MYSQL`
     par votre utilisateur et votre mot de passe MySQL.
   - Si vous n'avez pas encore d'utilisateur dédié, vous pouvez en créer un depuis MySQL
     (connecté en `root`), en remplaçant le mot de passe :
     ```sql
     CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'VOTRE_MOT_DE_PASSE';
     GRANT ALL PRIVILEGES ON gestion_employes.* TO 'admin'@'localhost';
     FLUSH PRIVILEGES;
     ```
   - La base `gestion_employes` et toutes les tables sont **créées automatiquement**
     au premier démarrage (`hibernate.hbm2ddl.auto=update`).
3. Construire le WAR :
   ```
   mvn clean package
   ```
4. Déployer `target/gestion-employes.war` dans le dossier `webapps` de Tomcat
   (ou lancer le projet depuis VS Code / Eclipse / IntelliJ avec un serveur Tomcat 10.1).
5. Ouvrir : <http://localhost:8080/gestion-employes/>

## Connexion

| Identifiant | Mot de passe |
|-------------|--------------|
| `admin` | `admin123` |

Le compte est créé au démarrage (table `utilisateurs`, mot de passe haché avec PBKDF2-HMAC-SHA256).
Trois départements et trois postes d'exemple sont aussi créés si les tables sont vides.

## Structure du code (MVC)

```
src/main/java/com/gestionemployes/
├── model/       Employe, Departement, Poste, Utilisateur (entités JPA/Hibernate)
├── dao/         BaseDAO, EmployeDAO, DepartementDAO, PosteDAO, UtilisateurDAO
├── service/     EmployeService, DepartementService, PosteService, AuthService (règles de gestion)
├── controller/  Servlets : Login, Logout, Dashboard, Employe, Departement, Poste
├── filter/      AuthFilter (pages protégées), EncodingFilter (UTF-8)
└── util/        HibernateUtil, PasswordUtil, Html (échappement XSS), AppInitializer
src/main/webapp/WEB-INF/views/   Pages JSP (auth, dashboard, employes, departements, postes)
```

## Fonctionnalités (cahier des charges)

- Authentification, déconnexion, protection des pages côté serveur, session.
- CRUD des employés, départements et postes, avec confirmation avant suppression (RG12).
- Affectation d'un employé à un département et à un poste via listes déroulantes.
- Recherche par matricule / nom / prénom et filtres par département et par poste.
- Détails d'un département et d'un poste avec la liste de leurs employés.
- Validation : champs obligatoires, format e-mail/téléphone, doublons (matricule, e-mail,
  nom de département, intitulé de poste), existence du département et du poste.
- Règles RG8 / RG9 : impossible de supprimer un département contenant des employés
  ou un poste attribué.
- Tableau de bord : nombre d'employés, de départements, de postes, effectif par département.

## Remarque sur la base de données

Si la table `employes` existe déjà d'une version précédente, Hibernate y ajoute les colonnes
`id_departement` et `id_poste`. Les anciens employés apparaissent « Non affecté » jusqu'à
modification. Pour repartir de zéro : `DROP DATABASE gestion_employes;` puis relancer l'application.

## Git

`hibernate.cfg.xml` est ignoré par Git (il contient le mot de passe MySQL). `hibernate.cfg.xml.example`
sert de modèle : chaque membre du binôme le copie en `hibernate.cfg.xml` et renseigne son propre
utilisateur et mot de passe MySQL. Ne jamais commiter de mot de passe.