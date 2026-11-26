<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.time.LocalDate, com.gestionemployes.model.*, com.gestionemployes.util.Html" %>
<%
    Employe e = (Employe) request.getAttribute("employe");
    boolean edition = e != null && e.getIdEmploye() != null;
    String erreur = (String) request.getAttribute("erreur");
    String ctx = request.getContextPath();

    @SuppressWarnings("unchecked")
    List<Departement> departements = (List<Departement>) request.getAttribute("departements");
    @SuppressWarnings("unchecked")
    List<Poste> postes = (List<Poste>) request.getAttribute("postes");
    Long idDepartement = (Long) request.getAttribute("idDepartement");
    Long idPoste = (Long) request.getAttribute("idPoste");
    String titrePage = edition ? "Modifier un employé" : "Ajouter un employé";
%>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="titre" value="<%= titrePage %>"/>
    <jsp:param name="actif" value="employes"/>
</jsp:include>

<div class="narrow-box">
<div class="card">

    <h1><%= edition ? "Modifier l'employé" : "Nouvel employé" %></h1>

    <% if (erreur != null) { %>
        <div class="error-message"><%= Html.e(erreur) %></div>
    <% } %>

    <% if (departements.isEmpty() || postes.isEmpty()) { %>
        <div class="error-message">
            Il faut d'abord créer au moins un
            <a href="<%= ctx %>/departements?action=nouveau">département</a> et un
            <a href="<%= ctx %>/postes?action=nouveau">poste</a> pour pouvoir affecter un employé.
        </div>
    <% } %>

    <form action="<%= ctx %>/employes" method="post">

        <input type="hidden" name="action" value="enregistrer">
        <input type="hidden" name="idEmploye"
               value="<%= edition ? e.getIdEmploye() : "" %>">

        <div class="form-group">
            <label for="matricule">Matricule *</label>
            <input type="text" id="matricule" name="matricule" maxlength="30"
                   value="<%= e != null ? Html.e(e.getMatricule()) : "" %>" required>
        </div>

        <div class="form-group">
            <label for="nom">Nom *</label>
            <input type="text" id="nom" name="nom" maxlength="100"
                   value="<%= e != null ? Html.e(e.getNom()) : "" %>" required>
        </div>

        <div class="form-group">
            <label for="prenom">Prénom *</label>
            <input type="text" id="prenom" name="prenom" maxlength="100"
                   value="<%= e != null ? Html.e(e.getPrenom()) : "" %>" required>
        </div>

        <div class="form-group">
            <label for="email">E-mail *</label>
            <input type="email" id="email" name="email" maxlength="150"
                   value="<%= e != null ? Html.e(e.getEmail()) : "" %>" required>
        </div>

        <div class="form-group">
            <label for="telephone">Téléphone</label>
            <input type="text" id="telephone" name="telephone" maxlength="20"
                   value="<%= e != null ? Html.e(e.getTelephone()) : "" %>">
        </div>

        <div class="form-group">
            <label for="adresse">Adresse</label>
            <input type="text" id="adresse" name="adresse" maxlength="255"
                   value="<%= e != null ? Html.e(e.getAdresse()) : "" %>">
        </div>

        <div class="form-group">
            <label for="dateRecrutement">Date de recrutement *</label>
            <input type="date" id="dateRecrutement" name="dateRecrutement"
                   max="<%= LocalDate.now() %>"
                   value="<%= e != null && e.getDateRecrutement() != null ? e.getDateRecrutement() : "" %>"
                   required>
        </div>

        <div class="form-group">
            <label for="departement">Département *</label>
            <select id="departement" name="departement" required>
                <option value="">— Choisir un département —</option>
                <% for (Departement d : departements) { %>
                    <option value="<%= d.getIdDepartement() %>"
                        <%= d.getIdDepartement().equals(idDepartement) ? "selected" : "" %>>
                        <%= Html.e(d.getNom()) %>
                    </option>
                <% } %>
            </select>
        </div>

        <div class="form-group">
            <label for="poste">Poste *</label>
            <select id="poste" name="poste" required>
                <option value="">— Choisir un poste —</option>
                <% for (Poste p : postes) { %>
                    <option value="<%= p.getIdPoste() %>"
                        <%= p.getIdPoste().equals(idPoste) ? "selected" : "" %>>
                        <%= Html.e(p.getIntitule()) %>
                    </option>
                <% } %>
            </select>
        </div>

        <div class="buttons">
            <button type="submit" class="btn">Enregistrer</button>
            <a class="btn btn-light" href="<%= ctx %>/employes">Annuler</a>
        </div>

    </form>

</div>
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
