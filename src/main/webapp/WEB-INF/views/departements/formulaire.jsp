<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.gestionemployes.model.Departement, com.gestionemployes.util.Html" %>
<%
    Departement d = (Departement) request.getAttribute("departement");
    boolean edition = d != null && d.getIdDepartement() != null;
    String erreur = (String) request.getAttribute("erreur");
    String ctx = request.getContextPath();
    String titrePage = edition ? "Modifier un département" : "Ajouter un département";
%>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="titre" value="<%= titrePage %>"/>
    <jsp:param name="actif" value="departements"/>
</jsp:include>

<div class="narrow-box">
<div class="card">

    <h1><%= edition ? "Modifier le département" : "Nouveau département" %></h1>

    <% if (erreur != null) { %>
        <div class="error-message"><%= Html.e(erreur) %></div>
    <% } %>

    <form action="<%= ctx %>/departements" method="post">

        <input type="hidden" name="action" value="enregistrer">
        <input type="hidden" name="idDepartement"
               value="<%= edition ? d.getIdDepartement() : "" %>">

        <div class="form-group">
            <label for="nom">Nom *</label>
            <input type="text" id="nom" name="nom" maxlength="100"
                   value="<%= d != null ? Html.e(d.getNom()) : "" %>" required>
        </div>

        <div class="form-group">
            <label for="description">Description</label>
            <textarea id="description" name="description" rows="4" maxlength="500"><%= d != null ? Html.e(d.getDescription()) : "" %></textarea>
        </div>

        <div class="buttons">
            <button type="submit" class="btn">Enregistrer</button>
            <a class="btn btn-light" href="<%= ctx %>/departements">Annuler</a>
        </div>

    </form>

</div>
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
