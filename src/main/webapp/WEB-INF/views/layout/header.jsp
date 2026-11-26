<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.gestionemployes.util.Html" %>
<%
    String ctx = request.getContextPath();
    String titre = request.getParameter("titre");
    String actif = request.getParameter("actif");
    if (actif == null) actif = "";

    String messageSucces = (String) session.getAttribute("flash_ok");
    String messageErreur = (String) session.getAttribute("flash_err");
    session.removeAttribute("flash_ok");
    session.removeAttribute("flash_err");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><%= Html.e(titre) %> | Gestion des employés</title>
    <link rel="stylesheet" href="<%= ctx %>/assets/css/employes.css">
</head>
<body>

<nav class="navbar">
    <div class="navbar-inner">
        <a class="brand" href="<%= ctx %>/dashboard">Gestion des employés</a>
        <div class="nav-links">
            <a href="<%= ctx %>/dashboard" class="<%= "dashboard".equals(actif) ? "active" : "" %>">Accueil</a>
            <a href="<%= ctx %>/employes" class="<%= "employes".equals(actif) ? "active" : "" %>">Employés</a>
            <a href="<%= ctx %>/departements" class="<%= "departements".equals(actif) ? "active" : "" %>">Départements</a>
            <a href="<%= ctx %>/postes" class="<%= "postes".equals(actif) ? "active" : "" %>">Postes</a>
        </div>
        <a class="logout" href="<%= ctx %>/logout">Déconnexion</a>
    </div>
</nav>

<div class="page">

<% if (!"dashboard".equals(actif)) { %>
    <nav class="breadcrumb" aria-label="Fil d'Ariane">
        <a href="<%= ctx %>/dashboard">&#8962; Accueil</a>
        <span class="sep">&rsaquo;</span>
        <span><%= Html.e(titre) %></span>
    </nav>
<% } %>

<% if (messageSucces != null) { %>
    <div class="alert-success"><%= Html.e(messageSucces) %></div>
<% } %>
<% if (messageErreur != null) { %>
    <div class="error-message"><%= Html.e(messageErreur) %></div>
<% } %>
