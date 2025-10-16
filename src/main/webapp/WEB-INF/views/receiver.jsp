<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Create Donor &amp; Receiver</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/receiver">Blood Bank Manager</a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/donors">Donors</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/receivers">Receivers</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="container">
    <c:if test="${not empty flashMessage}">
        <div class="alert alert-success">${flashMessage}</div>
    </c:if>
    <div class="row g-4">
        <div class="col-lg-6 mx-auto ">
            <div class="card shadow-sm">
                <div class="card-header bg-success text-white fw-semibold">
                    <c:choose>
                        <c:when test="${not empty receiverForm.id}">Update Receiver</c:when>
                        <c:otherwise>Create Receiver</c:otherwise>
                    </c:choose>
                </div>
                <div class="card-body">
                    <form method="post" action="${pageContext.request.contextPath}/receiver">
                        <input type="hidden" name="formType" value="receiver">
                        <input type="hidden" name="id" value="${receiverForm.id}">
                        <div class="mb-3">
                            <label class="form-label">First Name*</label>
                            <input type="text" class="form-control" name="firstName" value="${receiverForm.firstName}" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Last Name*</label>
                            <input type="text" class="form-control" name="lastName" value="${receiverForm.lastName}" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">National ID (CIN)*</label>
                            <input type="text" class="form-control" name="cin" value="${receiverForm.cin}" required>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Phone*</label>
                                <input type="tel" class="form-control" name="phone" value="${receiverForm.phone}" required>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Date of Birth*</label>
                                <input type="date" class="form-control" name="dateOfBirth" value="${receiverForm.dateOfBirth}" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Gender*</label>
                                <select class="form-select" name="gender" required>
                                    <option value="">Select</option>
                                    <option value="MALE" ${receiverForm.gender == 'MALE' ? 'selected' : ''}>Male</option>
                                    <option value="FEMALE" ${receiverForm.gender == 'FEMALE' ? 'selected' : ''}>Female</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Blood Group*</label>
                                <select class="form-select" name="bloodGroup" required>
                                    <option value="">Select</option>
                                    <c:forEach items="${bloodGroups}" var="group">
                                        <option value="${group}" ${receiverForm.bloodGroup == group ? 'selected' : ''}>${group}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Urgency*</label>
                                <select class="form-select" name="urgency" required>
                                    <c:forEach items="${urgencies}" var="urgency">
                                        <option value="${urgency}" ${receiverForm.urgency == urgency ? 'selected' : ''}>${urgency}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <c:if test="${not empty receiverErrors}">
                            <div class="alert alert-danger">
                                <ul class="mb-0">
                                    <c:forEach items="${receiverErrors}" var="error">
                                        <li>${error}</li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </c:if>
                        <div class="d-grid mt-3">
                            <button type="submit" class="btn btn-success">
                                <c:choose>
                                    <c:when test="${not empty receiverForm.id}">Update Receiver</c:when>
                                    <c:otherwise>Save Receiver</c:otherwise>
                                </c:choose>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
