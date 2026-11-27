<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titre" value="Connexion requise" />
<%@ include file="fragments/header.jsp" %>

<section class="card card-narrow">
    <h1>Connexion requise</h1>
    <p class="muted">Vous devez être connecté pour accéder à cette page.</p>
    <a class="btn btn-primary" href="${base}/login">Se connecter</a>
</section>

<%@ include file="fragments/footer.jsp" %>
