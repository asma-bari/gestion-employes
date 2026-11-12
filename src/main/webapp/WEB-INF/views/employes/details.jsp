<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.time.format.DateTimeFormatter, com.gestionemployes.model.Employe" %>
<%!
    private static final DateTimeFormatter FR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static String v(String s) {
        return (s == null || s.isEmpty()) ? "—" : s;
    }
%>
<%
    Employe e = (Employe) request.getAttribute("employe");
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Détails de l'employé</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/css/employes.css">
</head>
<body>

<div class="page narrow">

    <div class="card">

        <h1><%= e.getPrenom() %> <%= e.getNom() %></h1>

        <dl class="details">
            <dt>Matricule</dt>
            <dd><%= e.getMatricule() %></dd>

            <dt>E-mail</dt>
            <dd><%= e.getEmail() %></dd>

            <dt>Téléphone</dt>
            <dd><%= v(e.getTelephone()) %></dd>

            <dt>Adresse</dt>
            <dd><%= v(e.getAdresse()) %></dd>

            <dt>Date de recrutement</dt>
            <dd><%= e.getDateRecrutement() != null ? e.getDateRecrutement().format(FR) : "—" %></dd>
        </dl>

        <div class="buttons">
            <a class="btn" href="<%= ctx %>/employes?action=modifier&id=<%= e.getIdEmploye() %>">Modifier</a>
            <a class="btn btn-light" href="<%= ctx %>/employes">Retour</a>
        </div>

    </div>

</div>

</body>
</html>