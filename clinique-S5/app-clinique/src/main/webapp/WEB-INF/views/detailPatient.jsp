<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titre" value="Détails du patient" />
<%@ include file="fragments/header.jsp" %>

<section class="card card-narrow">
    <h1><c:out value="${patient.nom}" /></h1>

    <dl class="details">
        <dt>Nom</dt>
        <dd><c:out value="${patient.nom}" /></dd>
        <dt>Email</dt>
        <dd><c:out value="${patient.email}" /></dd>
        <dt>Inscrit le</dt>
        <dd><c:out value="${patient.dateCreationFormatee}" /></dd>
    </dl>

    <div class="actions">
        <a class="btn btn-primary" href="${base}/patients/modifier?id=${patient.id}">Modifier</a>
        <a class="btn" href="${base}/patients">Retour à la liste</a>
    </div>
</section>

<%@ include file="fragments/footer.jsp" %>
