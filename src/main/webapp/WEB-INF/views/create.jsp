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
        <a class="navbar-brand" href="${pageContext.request.contextPath}/create">Blood Bank Manager</a>
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
        <div class="col-lg-6">
            <div class="card shadow-sm">
                <div class="card-header bg-danger text-white fw-semibold">
                    <c:choose>
                        <c:when test="${not empty donorForm.id}">Update Donor</c:when>
                        <c:otherwise>Create Donor</c:otherwise>
                    </c:choose>
                </div>
                <div class="card-body">
                    <form method="post" action="${pageContext.request.contextPath}/create">
                        <input type="hidden" name="formType" value="donor">
                        <input type="hidden" name="id" value="${donorForm.id}">
                        <div class="mb-3">
                            <label class="form-label">First Name*</label>
                            <input type="text" class="form-control" name="firstName" value="${donorForm.firstName}" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Last Name*</label>
                            <input type="text" class="form-control" name="lastName" value="${donorForm.lastName}" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">National ID (CIN)*</label>
                            <input type="text" class="form-control" name="cin" value="${donorForm.cin}" required>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Phone*</label>
                                <input type="tel" class="form-control" name="phone" value="${donorForm.phone}" required>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Date of Birth*</label>
                                <input type="date" class="form-control" name="dateOfBirth" value="${donorForm.dateOfBirth}" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Weight (kg)*</label>
                                <input type="number" min="0" class="form-control" name="weight" value="${donorForm.weight}" required>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Gender*</label>
                                <select class="form-select" name="gender" required>
                                    <option value="">Select</option>
                                    <option value="MALE" ${donorForm.gender == 'MALE' ? 'selected' : ''}>Male</option>
                                    <option value="FEMALE" ${donorForm.gender == 'FEMALE' ? 'selected' : ''}>Female</option>
                                </select>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Blood Group*</label>
                                <select class="form-select" name="bloodGroup" required>
                                    <option value="">Select</option>
                                    <c:forEach items="${bloodGroups}" var="group">
                                        <option value="${group}" ${donorForm.bloodGroup == group ? 'selected' : ''}>${group}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Medical Flags</label>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" name="medicalFlags" value="HEPATITIS"
                                       <c:if test="${donorForm.hasFlag('HEPATITIS')}">checked</c:if>>
                                <label class="form-check-label">Hepatitis B/C</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" name="medicalFlags" value="HIV"
                                       <c:if test="${donorForm.hasFlag('HIV')}">checked</c:if>>
                                <label class="form-check-label">HIV</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" name="medicalFlags" value="DIABETES"
                                       <c:if test="${donorForm.hasFlag('DIABETES')}">checked</c:if>>
                                <label class="form-check-label">Insulin-dependent Diabetes</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" name="medicalFlags" value="PREGNANCY"
                                       <c:if test="${donorForm.hasFlag('PREGNANCY')}">checked</c:if>>
                                <label class="form-check-label">Pregnancy</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" name="medicalFlags" value="BREASTFEEDING"
                                       <c:if test="${donorForm.hasFlag('BREASTFEEDING')}">checked</c:if>>
                                <label class="form-check-label">Breastfeeding</label>
                            </div>
                        </div>
                        <c:if test="${not empty donorErrors}">
                            <div class="alert alert-danger">
                                <ul class="mb-0">
                                    <c:forEach items="${donorErrors}" var="error">
                                        <li>${error}</li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </c:if>
                        <div class="d-grid mt-3">
                            <button type="submit" class="btn btn-danger">
                                <c:choose>
                                    <c:when test="${not empty donorForm.id}">Update Donor</c:when>
                                    <c:otherwise>Save Donor</c:otherwise>
                                </c:choose>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-lg-6">
            <div class="card shadow-sm">
                <div class="card-header bg-success text-white fw-semibold">
                    <c:choose>
                        <c:when test="${not empty receiverForm.id}">Update Receiver</c:when>
                        <c:otherwise>Create Receiver</c:otherwise>
                    </c:choose>
                </div>
                <div class="card-body">
                    <form method="post" action="${pageContext.request.contextPath}/create">
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
                                    <option value="OTHER" ${receiverForm.gender == 'OTHER' ? 'selected' : ''}>Other</option>
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
