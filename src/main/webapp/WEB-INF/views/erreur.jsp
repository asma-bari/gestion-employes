<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Erreur | Gestion des employés</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/employes.css">
</head>
<body>
<div class="page narrow">
    <div class="card">
        <h1>Une erreur est survenue</h1>
        <p class="empty">La page demandée est introuvable ou une erreur interne s'est produite.</p>
        <div class="buttons center">
            <a class="btn" href="<%= request.getContextPath() %>/dashboard">Retour à l'accueil</a>
        </div>
    </div>
</div>
</body>
</html>
