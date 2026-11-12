<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.time.format.DateTimeFormatter, com.gestionemployes.model.Employe" %>
<%!
    private static final DateTimeFormatter FR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
%>
<%
    @SuppressWarnings("unchecked")
    List<Employe> employes = (List<Employe>) request.getAttribute("employes");
    String q = (String) request.getAttribute("q");
    if (q == null) q = "";
    String message = (String) request.getAttribute("message");
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Employés</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/css/employes.css">
</head>
<body>

<div class="page">

    <div class="topbar">
        <h1>Employés</h1>
        <div class="actions">
            <a class="btn" href="<%= ctx %>/employes?action=nouveau">Ajouter un employé</a>
            <a class="btn btn-light" href="<%= ctx %>/logout">Déconnexion</a>
        </div>
    </div>

    <% if (message != null) { %>
        <div class="alert-success"><%= message %></div>
    <% } %>

    <div class="card">

        <form class="toolbar" action="<%= ctx %>/employes" method="get">
            <input type="text" name="q" value="<%= q %>"
                   placeholder="Rechercher par matricule, nom ou prénom">
            <button type="submit" class="btn">Rechercher</button>
            <% if (!q.isEmpty()) { %>
                <a class="btn btn-light" href="<%= ctx %>/employes">Réinitialiser</a>
            <% } %>
        </form>

        <% if (employes == null || employes.isEmpty()) { %>
            <p class="empty">Aucun employé trouvé.</p>
        <% } else { %>
        <div class="table-wrap">
            <table>
                <thead>
                    <tr>
                        <th>Matricule</th>
                        <th>Nom</th>
                        <th>Prénom</th>
                        <th>E-mail</th>
                        <th>Recrutement</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                <% for (Employe e : employes) { %>
                    <tr>
                        <td><%= e.getMatricule() %></td>
                        <td><%= e.getNom() %></td>
                        <td><%= e.getPrenom() %></td>
                        <td><%= e.getEmail() %></td>
                        <td><%= e.getDateRecrutement() != null ? e.getDateRecrutement().format(FR) : "" %></td>
                        <td class="row-actions">
                            <a href="<%= ctx %>/employes?action=details&id=<%= e.getIdEmploye() %>">Détails</a>
                            <a href="<%= ctx %>/employes?action=modifier&id=<%= e.getIdEmploye() %>">Modifier</a>
                            <form action="<%= ctx %>/employes" method="post"
                                  onsubmit="return confirm('Supprimer cet employé ?');">
                                <input type="hidden" name="action" value="supprimer">
                                <input type="hidden" name="id" value="<%= e.getIdEmploye() %>">
                                <button type="submit" class="link-danger">Supprimer</button>
                            </form>
                        </td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
        <% } %>

    </div>

</div>

</body>
</html>