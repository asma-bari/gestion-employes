<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.gestionemployes.model.Poste, com.gestionemployes.util.Html" %>
<%
    Poste p = (Poste) request.getAttribute("poste");
    boolean edition = p != null && p.getIdPoste() != null;
    String erreur = (String) request.getAttribute("erreur");
    String ctx = request.getContextPath();
    String titrePage = edition ? "Modifier un poste" : "Ajouter un poste";
%>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="titre" value="<%= titrePage %>"/>
    <jsp:param name="actif" value="postes"/>
</jsp:include>

<div class="narrow-box">
<div class="card">

    <h1><%= edition ? "Modifier le poste" : "Nouveau poste" %></h1>

    <% if (erreur != null) { %>
        <div class="error-message"><%= Html.e(erreur) %></div>
    <% } %>

    <form action="<%= ctx %>/postes" method="post">

        <input type="hidden" name="action" value="enregistrer">
        <input type="hidden" name="idPoste"
               value="<%= edition ? p.getIdPoste() : "" %>">

        <div class="form-group">
            <label for="intitule">Intitulé *</label>
            <input type="text" id="intitule" name="intitule" maxlength="100"
                   value="<%= p != null ? Html.e(p.getIntitule()) : "" %>" required>
        </div>

        <div class="form-group">
            <label for="description">Description</label>
            <textarea id="description" name="description" rows="4" maxlength="500"><%= p != null ? Html.e(p.getDescription()) : "" %></textarea>
        </div>

        <div class="buttons">
            <button type="submit" class="btn">Enregistrer</button>
            <a class="btn btn-light" href="<%= ctx %>/postes">Annuler</a>
        </div>

    </form>

</div>
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
