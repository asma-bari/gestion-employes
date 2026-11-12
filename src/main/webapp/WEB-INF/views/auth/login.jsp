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

        <h1>Connexion</h1>

        <% if (request.getAttribute("erreur") != null) { %>
            <div class="error-message">
                <%= request.getAttribute("erreur") %>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/login" method="post">

            <div class="form-group">
                <label for="login">Identifiant</label>
                <input type="text" id="login" name="login"
                       autocomplete="username" required>
            </div>

            <div class="form-group">
                <label for="password">Mot de passe</label>
                <input type="password" id="password" name="password"
                       autocomplete="current-password" required>
            </div>

            <button type="submit" class="login-button">Se connecter</button>

        </form>

    </div>
</div>

</body>
</html>