<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, com.gestionemployes.model.*, com.gestionemployes.util.Html" %>
<%
    Poste p = (Poste) request.getAttribute("poste");
    @SuppressWarnings("unchecked")
    List<Employe> employes = (List<Employe>) request.getAttribute("employes");
    String ctx = request.getContextPath();
%>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="titre" value="Détails du poste"/>
    <jsp:param name="actif" value="postes"/>
</jsp:include>

<div class="topbar">
    <h1><%= Html.e(p.getIntitule()) %></h1>
    <div class="actions">
        <a class="btn" href="<%= ctx %>/postes?action=modifier&id=<%= p.getIdPoste() %>">Modifier</a>
        <a class="btn btn-light" href="<%= ctx %>/postes">Retour</a>
    </div>
</div>

<div class="card">
    <dl class="details">
        <dt>Description</dt>
        <dd><%= Html.ou(p.getDescription()) %></dd>

        <dt>Nombre d'employés</dt>
        <dd><%= employes.size() %></dd>
    </dl>
</div>

<div class="card section">
    <h2>Employés occupant ce poste</h2>

    <% if (employes.isEmpty()) { %>
        <p class="empty">Aucun employé n'occupe ce poste.</p>
    <% } else { %>
    <div class="table-wrap">
        <table>
            <thead>
                <tr>
                    <th>Matricule</th>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Département</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
            <% for (Employe e : employes) { %>
                <tr>
                    <td><%= Html.e(e.getMatricule()) %></td>
                    <td><%= Html.e(e.getNom()) %></td>
                    <td><%= Html.e(e.getPrenom()) %></td>
                    <td><%= e.getDepartement() != null ? Html.e(e.getDepartement().getNom()) : "—" %></td>
                    <td class="row-actions">
                        <a href="<%= ctx %>/employes?action=details&id=<%= e.getIdEmploye() %>">Détails</a>
                    </td>
                </tr>
            <% } %>
            </tbody>
        </table>
    </div>
    <% } %>
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
