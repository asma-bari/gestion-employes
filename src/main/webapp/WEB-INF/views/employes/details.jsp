<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.time.format.DateTimeFormatter, com.gestionemployes.model.Employe, com.gestionemployes.util.Html" %>
<%!
    private static final DateTimeFormatter FR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
%>
<%
    Employe e = (Employe) request.getAttribute("employe");
    String ctx = request.getContextPath();
%>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="titre" value="Détails de l'employé"/>
    <jsp:param name="actif" value="employes"/>
</jsp:include>

<div class="narrow-box">
<div class="card">

    <h1><%= Html.e(e.getPrenom()) %> <%= Html.e(e.getNom()) %></h1>

    <dl class="details">
        <dt>Matricule</dt>
        <dd><%= Html.e(e.getMatricule()) %></dd>

        <dt>E-mail</dt>
        <dd><%= Html.e(e.getEmail()) %></dd>

        <dt>Téléphone</dt>
        <dd><%= Html.ou(e.getTelephone()) %></dd>

        <dt>Adresse</dt>
        <dd><%= Html.ou(e.getAdresse()) %></dd>

        <dt>Date de recrutement</dt>
        <dd><%= e.getDateRecrutement() != null ? e.getDateRecrutement().format(FR) : "—" %></dd>

        <dt>Département</dt>
        <dd>
            <% if (e.getDepartement() != null) { %>
                <a href="<%= ctx %>/departements?action=details&id=<%= e.getDepartement().getIdDepartement() %>"><%= Html.e(e.getDepartement().getNom()) %></a>
            <% } else { %>—<% } %>
        </dd>

        <dt>Poste</dt>
        <dd>
            <% if (e.getPoste() != null) { %>
                <a href="<%= ctx %>/postes?action=details&id=<%= e.getPoste().getIdPoste() %>"><%= Html.e(e.getPoste().getIntitule()) %></a>
            <% } else { %>—<% } %>
        </dd>
    </dl>

    <div class="buttons">
        <a class="btn" href="<%= ctx %>/employes?action=modifier&id=<%= e.getIdEmploye() %>">Modifier</a>
        <a class="btn btn-light" href="<%= ctx %>/employes">Retour</a>
    </div>

</div>
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
