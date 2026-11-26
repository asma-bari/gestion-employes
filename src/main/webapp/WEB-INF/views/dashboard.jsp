<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.util.Locale, java.util.Map, com.gestionemployes.model.Departement, com.gestionemployes.util.Html" %>
<%
    String ctx = request.getContextPath();
    long nbEmployes = (Long) request.getAttribute("nbEmployes");
    long nbDepartements = (Long) request.getAttribute("nbDepartements");
    long nbPostes = (Long) request.getAttribute("nbPostes");

    @SuppressWarnings("unchecked")
    List<Departement> departements = (List<Departement>) request.getAttribute("departements");
    @SuppressWarnings("unchecked")
    Map<Long, Long> effectifs = (Map<Long, Long>) request.getAttribute("effectifs");

    final String[] COULEURS = {"#7652B5", "#4FB3A9", "#F2A65A", "#E57A9A", "#5B8DEF", "#8BC34A", "#A084DC", "#B0A3C9"};
    final double RAYON = 70;
    final double CIRC = 2 * Math.PI * RAYON;

    long total = 0;
    for (Departement d : departements) {
        total += effectifs.getOrDefault(d.getIdDepartement(), 0L);
    }
    long nbTranches = 0;
    for (Departement d : departements) {
        if (effectifs.getOrDefault(d.getIdDepartement(), 0L) > 0) nbTranches++;
    }
%>
<jsp:include page="/WEB-INF/views/layout/header.jsp">
    <jsp:param name="titre" value="Tableau de bord"/>
    <jsp:param name="actif" value="dashboard"/>
</jsp:include>

<div class="topbar">
    <h1>Tableau de bord</h1>
</div>

<div class="stats">
    <a class="stat" href="<%= ctx %>/employes">
        <span class="stat-value"><%= nbEmployes %></span>
        <span class="stat-label">Employés</span>
    </a>
    <a class="stat" href="<%= ctx %>/departements">
        <span class="stat-value"><%= nbDepartements %></span>
        <span class="stat-label">Départements</span>
    </a>
    <a class="stat" href="<%= ctx %>/postes">
        <span class="stat-value"><%= nbPostes %></span>
        <span class="stat-label">Postes</span>
    </a>
</div>

<div class="card">
    <h2>Employés par département</h2>

    <% if (departements.isEmpty()) { %>
        <p class="empty">Aucun département enregistré.</p>
    <% } else { %>
    <div class="donut-wrap">

        <div class="donut">
            <svg viewBox="0 0 200 200" role="img" aria-label="Répartition des employés par département">
                <circle class="donut-bg" cx="100" cy="100" r="70"></circle>
                <g transform="rotate(-90 100 100)">
                <%
                    double decalage = 0;
                    int i = 0;
                    for (Departement d : departements) {
                        long n = effectifs.getOrDefault(d.getIdDepartement(), 0L);
                        String couleur = COULEURS[i % COULEURS.length];
                        i++;
                        if (n == 0 || total == 0) continue;
                        double part = (double) n / total * CIRC;
                        double trait = nbTranches > 1 ? Math.max(part - 2, 0.5) : part;
                %>
                    <circle class="donut-slice" cx="100" cy="100" r="70"
                            stroke="<%= couleur %>"
                            stroke-dasharray="<%= String.format(Locale.US, "%.2f %.2f", trait, CIRC - trait) %>"
                            stroke-dashoffset="<%= String.format(Locale.US, "%.2f", -decalage) %>">
                        <title><%= Html.e(d.getNom()) %> : <%= n %></title>
                    </circle>
                <%
                        decalage += part;
                    }
                %>
                </g>
            </svg>
            <div class="donut-center">
                <strong><%= total %></strong>
                <span>employé<%= total > 1 ? "s" : "" %></span>
            </div>
        </div>

        <ul class="legend">
            <% int k = 0;
               for (Departement d : departements) {
                   long n = effectifs.getOrDefault(d.getIdDepartement(), 0L);
                   long pct = total == 0 ? 0 : Math.round(n * 100.0 / total);
                   String couleur = COULEURS[k % COULEURS.length];
                   k++;
            %>
            <li>
                <span class="legend-dot" style="background: <%= couleur %>"></span>
                <a class="legend-name" href="<%= ctx %>/departements?action=details&id=<%= d.getIdDepartement() %>"><%= Html.e(d.getNom()) %></a>
                <span class="legend-count"><%= n %></span>
                <span class="legend-pct"><%= pct %>&nbsp;%</span>
            </li>
            <% } %>
        </ul>

    </div>
    <% } %>
</div>

<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
