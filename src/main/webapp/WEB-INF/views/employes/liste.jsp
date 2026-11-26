<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.time.format.DateTimeFormatter, com.gestionemployes.model.*, com.gestionemployes.util.Html" %>
<%!
    private static final DateTimeFormatter FR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
%>
<%
    @SuppressWarnings("unchecked")
    List<Employe> employes = (List<Employe>) request.getAttribute("employes");
    @SuppressWarnings("unchecked")
    List<Departement> departements = (List<Departement>) request.getAttribute("departements");
    @SuppressWarnings("unchecked")
    List<Poste> postes = (List<Poste>) request.getAttribute("postes");

    String q = (String) request.getAttribute("q");
    if (q == null) q = "";
    Long filtreDepartement = (Long) request.getAttribute("filtreDepartement");
    Long filtrePoste = (Long) request.getAttribute("filtrePoste");
    boolean filtre = !q.isEmpty() || filtreDepartement != null || filtrePoste != null;
    String ctx = request.getContextPath();
%>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="titre" value="Employés"/>
    <jsp:param name="actif" value="employes"/>
</jsp:include>

<div class="topbar">
    <h1>Employés</h1>
    <div class="actions">
        <a class="btn" href="<%= ctx %>/employes?action=nouveau">Ajouter un employé</a>
    </div>
</div>

<div class="card">

    <form class="toolbar" action="<%= ctx %>/employes" method="get">
        <input type="text" name="q" value="<%= Html.e(q) %>"
               placeholder="Rechercher par nom et prénom (ou matricule)">

        <select name="departement">
            <option value="">Tous les départements</option>
            <% for (Departement d : departements) { %>
                <option value="<%= d.getIdDepartement() %>"
                    <%= d.getIdDepartement().equals(filtreDepartement) ? "selected" : "" %>>
                    <%= Html.e(d.getNom()) %>
                </option>
            <% } %>
        </select>

        <select name="poste">
            <option value="">Tous les postes</option>
            <% for (Poste p : postes) { %>
                <option value="<%= p.getIdPoste() %>"
                    <%= p.getIdPoste().equals(filtrePoste) ? "selected" : "" %>>
                    <%= Html.e(p.getIntitule()) %>
                </option>
            <% } %>
        </select>

        <button type="submit" class="btn">Filtrer</button>
        <% if (filtre) { %>
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
                    <th>Département</th>
                    <th>Poste</th>
                    <th>Recrutement</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
            <% for (Employe e : employes) { %>
                <tr>
                    <td><%= Html.e(e.getMatricule()) %></td>
                    <td><%= Html.e(e.getNom()) %></td>
                    <td><%= Html.e(e.getPrenom()) %></td>
                    <td><%= Html.e(e.getEmail()) %></td>
                    <td><%= e.getDepartement() != null ? Html.e(e.getDepartement().getNom()) : "<span class=\"muted\">Non affecté</span>" %></td>
                    <td><%= e.getPoste() != null ? Html.e(e.getPoste().getIntitule()) : "<span class=\"muted\">Non affecté</span>" %></td>
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

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
