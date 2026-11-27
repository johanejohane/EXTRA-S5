<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titre" value="${creation ? 'Nouveau patient' : 'Modifier le patient'}" />
<%@ include file="fragments/header.jsp" %>

<section class="card card-narrow">
    <h1><c:out value="${titre}" /></h1>

    <form method="post" action="${base}${action}" class="form">
        <c:if test="${not creation}">
            <input type="hidden" name="id" value="${patient.id}">
        </c:if>

        <label for="nom">Nom</label>
        <input type="text" id="nom" name="nom" value="<c:out value='${patient.nom}' />" required>
        <c:if test="${not empty errors.nom}"><p class="field-error"><c:out value="${errors.nom}" /></p></c:if>

        <label for="email">Email</label>
        <input type="email" id="email" name="email" value="<c:out value='${patient.email}' />" required>
        <c:if test="${not empty errors.email}"><p class="field-error"><c:out value="${errors.email}" /></p></c:if>

        <label for="password">Mot de passe</label>
        <input type="password" id="password" name="password" autocomplete="new-password"
               <c:if test="${creation}">required</c:if>>
        <c:if test="${not creation}"><p class="hint">Laisser vide pour conserver le mot de passe actuel.</p></c:if>
        <c:if test="${not empty errors.password}"><p class="field-error"><c:out value="${errors.password}" /></p></c:if>

        <div class="actions">
            <button type="submit" class="btn btn-primary">Enregistrer</button>
            <a class="btn" href="${base}/patients">Annuler</a>
        </div>
    </form>
</section>

<%@ include file="fragments/footer.jsp" %>
