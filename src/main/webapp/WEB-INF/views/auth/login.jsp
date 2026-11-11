<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Connexion | Gestion des employés</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/login.css">
</head>

<body>

<div class="login-container">

    <div class="login-card">

        <div class="logo">
            <div class="logo-icon">👥</div>
            <h1>Gestion Employés</h1>
            <p>Votre espace d'administration</p>
        </div>

        <div class="welcome">
            <h2>Bienvenue !</h2>
            <p>Connectez-vous pour accéder à votre espace.</p>
        </div>

        <% if (request.getAttribute("erreur") != null) { %>
            <div class="error-message">
                <%= request.getAttribute("erreur") %>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/login"
              method="post">

            <div class="form-group">
                <label for="email">Adresse e-mail</label>

                <input
                    type="email"
                    id="email"
                    name="email"
                    placeholder="admin@gmail.com"
                    autocomplete="username"
                    required>
            </div>

            <div class="form-group">
                <label for="password">Mot de passe</label>

                <div class="password-container">
                    <input
                        type="password"
                        id="password"
                        name="password"
                        placeholder="Entrez votre mot de passe"
                        autocomplete="current-password"
                        required>

                    <button type="button"
                            class="toggle-password"
                            id="togglePassword"
                            aria-label="Afficher le mot de passe">
                        👁
                    </button>
                </div>
            </div>

            <div class="form-options">
                <label class="remember">
                    <input type="checkbox" name="remember">
                    Se souvenir de moi
                </label>

                <a href="#" class="forgot">
                    Mot de passe oublié ?
                </a>
            </div>

            <button type="submit" class="login-button">
                Se connecter
            </button>

        </form>

        <div class="footer">
            <p>© 2026 Gestion Employés</p>
            <p>Tous droits réservés.</p>
        </div>

    </div>

</div>

<script src="${pageContext.request.contextPath}/assets/js/login.js"></script>

</body>
</html>
