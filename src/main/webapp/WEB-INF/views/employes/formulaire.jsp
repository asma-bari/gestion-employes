<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.time.LocalDate, com.gestionemployes.model.Employe" %>
<%!
    private static String v(String s) {
        return s == null ? "" : s;
    }
%>
<%
    Employe e = (Employe) request.getAttribute("employe");
    boolean edition = e != null && e.getIdEmploye() != null;
    String erreur = (String) request.getAttribute("erreur");
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><%= edition ? "Modifier" : "Ajouter" %> un employé</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/css/employes.css">
</head>
<body>

<div class="page narrow">

    <div class="card">

        <h1><%= edition ? "Modifier l'employé" : "Nouvel employé" %></h1>

        <% if (erreur != null) { %>
            <div class="error-message"><%= erreur %></div>
        <% } %>

        <form action="<%= ctx %>/employes" method="post">

            <input type="hidden" name="action" value="enregistrer">
            <input type="hidden" name="idEmploye"
                   value="<%= edition ? e.getIdEmploye() : "" %>">

            <div class="form-group">
                <label for="matricule">Matricule *</label>
                <input type="text" id="matricule" name="matricule" maxlength="30"
                       value="<%= e != null ? v(e.getMatricule()) : "" %>" required>
            </div>

            <div class="form-group">
                <label for="nom">Nom *</label>
                <input type="text" id="nom" name="nom" maxlength="100"
                       value="<%= e != null ? v(e.getNom()) : "" %>" required>
            </div>

            <div class="form-group">
                <label for="prenom">Prénom *</label>
                <input type="text" id="prenom" name="prenom" maxlength="100"
                       value="<%= e != null ? v(e.getPrenom()) : "" %>" required>
            </div>

            <div class="form-group">
                <label for="email">E-mail *</label>
                <input type="email" id="email" name="email" maxlength="150"
                       value="<%= e != null ? v(e.getEmail()) : "" %>" required>
            </div>

            <div class="form-group">
                <label for="telephone">Téléphone</label>
                <input type="text" id="telephone" name="telephone" maxlength="20"
                       value="<%= e != null ? v(e.getTelephone()) : "" %>">
            </div>

            <div class="form-group">
                <label for="adresse">Adresse</label>
                <input type="text" id="adresse" name="adresse" maxlength="255"
                       value="<%= e != null ? v(e.getAdresse()) : "" %>">
            </div>

            <div class="form-group">
                <label for="dateRecrutement">Date de recrutement *</label>
                <input type="date" id="dateRecrutement" name="dateRecrutement"
                       max="<%= LocalDate.now() %>"
                       value="<%= e != null && e.getDateRecrutement() != null ? e.getDateRecrutement() : "" %>"
                       required>
            </div>

            <div class="buttons">
                <button type="submit" class="btn">Enregistrer</button>
                <a class="btn btn-light" href="<%= ctx %>/employes">Annuler</a>
            </div>

        </form>

    </div>

</div>

</body>
</html>