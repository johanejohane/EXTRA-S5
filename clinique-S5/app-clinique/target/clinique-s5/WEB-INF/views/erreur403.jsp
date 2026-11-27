<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titre" value="Accès refusé" />
<%@ include file="fragments/header.jsp" %>

<section class="card card-narrow">
    <h1>Accès refusé</h1>
    <p class="muted">Votre rôle ne vous permet pas d'accéder à cette page.</p>
    <a class="btn btn-primary" href="${base}/home">Retour à l'accueil</a>
</section>

<%@ include file="fragments/footer.jsp" %>
