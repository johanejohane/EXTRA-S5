<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titre" value="Connexion" />
<%@ include file="fragments/header.jsp" %>

<section class="card card-narrow">
    <h1>Connexion</h1>
    <p class="muted">Médecins et patients : connectez-vous avec votre email.</p>

    <c:if test="${not empty erreur}">
        <div class="alert alert-error"><c:out value="${erreur}" /></div>
    </c:if>

    <form method="post" action="${base}/login" class="form">
        <label for="email">Email</label>
        <input type="email" id="email" name="email" value="<c:out value='${email}' />" required autofocus>

        <label for="password">Mot de passe</label>
        <input type="password" id="password" name="password" required>

        <button type="submit" class="btn btn-primary">Se connecter</button>
    </form>
</section>

<%@ include file="fragments/footer.jsp" %>
