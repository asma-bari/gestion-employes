<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.util.Map, com.gestionemployes.model.Poste, com.gestionemployes.util.Html" %>
<%
    @SuppressWarnings("unchecked")
    List<Poste> postes = (List<Poste>) request.getAttribute("postes");
    @SuppressWarnings("unchecked")
    Map<Long, Long> effectifs = (Map<Long, Long>) request.getAttribute("effectifs");
    String ctx = request.getContextPath();
%>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="titre" value="Postes"/>
    <jsp:param name="actif" value="postes"/>
</jsp:include>

<div class="topbar">
    <h1>Postes</h1>
    <div class="actions">
        <a class="btn" href="<%= ctx %>/postes?action=nouveau">Ajouter un poste</a>
    </div>
</div>

<div class="card">

    <% if (postes.isEmpty()) { %>
        <p class="empty">Aucun poste enregistré.</p>
    <% } else { %>
    <div class="table-wrap">
        <table>
            <thead>
                <tr>
                    <th>Intitulé</th>
                    <th>Description</th>
                    <th>Employés</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
            <% for (Poste p : postes) { %>
                <tr>
                    <td><%= Html.e(p.getIntitule()) %></td>
                    <td class="wrap"><%= Html.ou(p.getDescription()) %></td>
                    <td><%= effectifs.getOrDefault(p.getIdPoste(), 0L) %></td>
                    <td class="row-actions">
                        <a href="<%= ctx %>/postes?action=details&id=<%= p.getIdPoste() %>">Détails</a>
                        <a href="<%= ctx %>/postes?action=modifier&id=<%= p.getIdPoste() %>">Modifier</a>
                        <form action="<%= ctx %>/postes" method="post"
                              onsubmit="return confirm('Supprimer ce poste ?');">
                            <input type="hidden" name="action" value="supprimer">
                            <input type="hidden" name="id" value="<%= p.getIdPoste() %>">
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
