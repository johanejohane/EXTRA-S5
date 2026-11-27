<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="base" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${titre}" /> · Clinique</title>
    <link rel="stylesheet" href="${base}/assets/style.css">
</head>
<body>
<header class="topbar">
    <div class="topbar-inner">
        <c:choose>
            <c:when test="${not empty sessionScope.userEmail}">
                <a class="brand" href="${base}/home">Clinique</a>
                <nav class="nav">
                    <a href="${base}/home">Mes informations</a>
                    <c:if test="${sessionScope.userRole == 'medecin'}">
                        <a href="${base}/patients">Patients</a>
                    </c:if>
                    <form method="post" action="${base}/logout" class="inline">
                        <button type="submit" class="link-btn">Déconnexion</button>
                    </form>
                </nav>
            </c:when>
            <c:otherwise>
                <span class="brand">Clinique</span>
            </c:otherwise>
        </c:choose>
    </div>
</header>
<main class="container">
