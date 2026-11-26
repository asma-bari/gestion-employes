package com.gestionemployes.service;

import com.gestionemployes.dao.UtilisateurDAO;
import com.gestionemployes.model.Utilisateur;
import com.gestionemployes.util.PasswordUtil;

public class AuthService {

    public static final String LOGIN_PAR_DEFAUT = "admin";
    public static final String MOT_DE_PASSE_PAR_DEFAUT = "admin123";
    public static final String ROLE_ADMIN = "ADMIN";

    private final UtilisateurDAO dao = new UtilisateurDAO();

    public Utilisateur authentifier(String login, String motDePasse) {

        if (login == null || login.isBlank() || motDePasse == null) {
            return null;
        }

        Utilisateur utilisateur = dao.trouverParLogin(login.trim());

        if (utilisateur != null
                && PasswordUtil.verifier(motDePasse, utilisateur.getMotDePasseHash())) {
            return utilisateur;
        }
        return null;
    }

    public void initialiserAdministrateur() {
        if (dao.compter() == 0) {
            dao.ajouter(new Utilisateur(
                    LOGIN_PAR_DEFAUT,
                    PasswordUtil.hacher(MOT_DE_PASSE_PAR_DEFAUT),
                    ROLE_ADMIN));
        }
    }
}
