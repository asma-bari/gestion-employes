<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.util.Map, com.gestionemployes.model.Departement, com.gestionemployes.util.Html" %>
<%
    @SuppressWarnings("unchecked")
    List<Departement> departements = (List<Departement>) request.getAttribute("departements");
    @SuppressWarnings("unchecked")
    Map<Long, Long> effectifs = (Map<Long, Long>) request.getAttribute("effectifs");
    String ctx = request.getContextPath();
%>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="titre" value="Départements"/>
    <jsp:param name="actif" value="departements"/>
</jsp:include>

<div class="topbar">
    <h1>Départements</h1>
    <div class="actions">
        <a class="btn" href="<%= ctx %>/departements?action=nouveau">Ajouter un département</a>
    </div>
</div>

<div class="card">

    <% if (departements.isEmpty()) { %>
        <p class="empty">Aucun département enregistré.</p>
    <% } else { %>
    <div class="table-wrap">
        <table>
            <thead>
                <tr>
                    <th>Nom</th>
                    <th>Description</th>
                    <th>Employés</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
            <% for (Departement d : departements) { %>
                <tr>
                    <td><%= Html.e(d.getNom()) %></td>
                    <td class="wrap"><%= Html.ou(d.getDescription()) %></td>
                    <td><%= effectifs.getOrDefault(d.getIdDepartement(), 0L) %></td>
                    <td class="row-actions">
                        <a href="<%= ctx %>/departements?action=details&id=<%= d.getIdDepartement() %>">Détails</a>
                        <a href="<%= ctx %>/departements?action=modifier&id=<%= d.getIdDepartement() %>">Modifier</a>
                        <form action="<%= ctx %>/departements" method="post"
                              onsubmit="return confirm('Supprimer ce département ?');">
                            <input type="hidden" name="action" value="supprimer">
                            <input type="hidden" name="id" value="<%= d.getIdDepartement() %>">
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

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
