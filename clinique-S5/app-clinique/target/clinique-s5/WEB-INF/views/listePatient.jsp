<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titre" value="Patients" />
<%@ include file="fragments/header.jsp" %>

<section class="card">
    <div class="card-head">
        <h1>Patients</h1>
        <a class="btn btn-primary" href="${base}/patients/nouveau">+ Ajouter un patient</a>
    </div>

    <c:if test="${not empty message}">
        <div class="alert alert-success"><c:out value="${message}" /></div>
    </c:if>

    <c:choose>
        <c:when test="${empty patients}">
            <p class="muted">Aucun patient pour le moment.</p>
        </c:when>
        <c:otherwise>
            <div class="table-wrap">
                <table>
                    <thead>
                        <tr>
                            <th>Nom</th>
                            <th>Email</th>
                            <th>Inscrit le</th>
                            <th class="col-actions">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${patients}">
                            <tr>
                                <td><c:out value="${p.nom}" /></td>
                                <td><c:out value="${p.email}" /></td>
                                <td><c:out value="${p.dateCreationFormatee}" /></td>
                                <td class="col-actions">
                                    <a class="btn btn-small" href="${base}/patients/detail?id=${p.id}">Détails</a>
                                    <a class="btn btn-small" href="${base}/patients/modifier?id=${p.id}">Modifier</a>
                                    <form method="post" action="${base}/patients/supprimer" class="inline"
                                          onsubmit="return confirm('Supprimer ce patient ?');">
                                        <input type="hidden" name="id" value="${p.id}">
                                        <button type="submit" class="btn btn-small btn-danger">Supprimer</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</section>

<%@ include file="fragments/footer.jsp" %>
