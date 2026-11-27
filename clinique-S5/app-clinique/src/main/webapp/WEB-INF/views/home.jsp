<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titre" value="Accueil" />
<%@ include file="fragments/header.jsp" %>

<section class="card">
    <h1>Bonjour, <c:out value="${utilisateur.nom}" /></h1>

    <c:choose>
        <c:when test="${utilisateur.role == 'medecin'}">
            <p class="muted">Connecté en tant que médecin.</p>
        </c:when>
        <c:otherwise>
            <p class="muted">Voici vos informations personnelles.</p>
        </c:otherwise>
    </c:choose>

    <dl class="details">
        <dt>Nom</dt>
        <dd><c:out value="${utilisateur.nom}" /></dd>
        <dt>Email</dt>
        <dd><c:out value="${utilisateur.email}" /></dd>
        <c:if test="${utilisateur.role == 'medecin'}">
            <dt>Rôle</dt>
            <dd>Médecin</dd>
        </c:if>
    </dl>

    <c:if test="${utilisateur.role == 'medecin'}">
        <a class="btn btn-primary" href="${base}/patients">Gérer les patients</a>
    </c:if>
</section>

<%@ include file="fragments/footer.jsp" %>
