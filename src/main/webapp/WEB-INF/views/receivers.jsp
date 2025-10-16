<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Receiver List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/styles.css">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/receiver">Blood Bank Manager</a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/donors">Donors</a></li>
                <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/receivers">Receivers</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="container">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="mb-0">Registered Receivers</h2>
        <a class="btn  btn-outline-light text-dark" href="${pageContext.request.contextPath}/receiver">Add New Receiver</a>
    </div>
    <c:if test="${not empty flashMessage}">
        <div class="alert alert-info">${flashMessage}</div>
    </c:if>
    <div class="card shadow-sm">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead class="table-dark">
                    <tr>
                        <th>#</th>
                        <th>Full Name</th>
                        <th>Blood Group</th>
                        <th>Urgency</th>
                        <th>Status</th>
                        <th>Needed Units</th>
                        <th>Assigned Donors</th>
                        <th class="text-end">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${receivers}" var="receiver" varStatus="loop">

                        <tr class="urgency-${receiver.urgency}">
                            <td>${loop.index + 1}</td>
                            <td>
                                <div class="fw-semibold">${receiver.firstName} ${receiver.lastName}</div>
                                <small class="text-muted">${receiver.cin}</small>
                            </td>
                            <td>${receiver.bloodGroup}</td>
                            <td><span style="color:black;" class="badge bg-${receiver.urgency == 'CRITICAL' ? 'danger' : receiver.urgency == 'URGENT' ? 'warning' : 'success'}">${receiver.urgency}</span></td>
                            <td><span style="color:black;" class="badge bg-${receiver.status == 'SATISFIED' ? 'success' : 'warning'}">${receiver.status}</span></td>
                            <td>${receiver.requiredUnits}</td>
                            <td>
                                <c:if test="${not empty receiver.donations}">
                                    <ul class="list-unstyled mb-0">
                                        <c:forEach items="${receiver.donations}" var="donation">
                                            <li>${donation.donor.firstName} ${donation.donor.lastName} <small class="text-muted">(${donation.donor.bloodType})</small></li>
                                        </c:forEach>
                                    </ul>
                                </c:if>
                                <c:if test="${empty receiver.donations}">
                                    <span class="text-muted">None</span>
                                </c:if>
                            </td>
                            <td class="text-end">
                                <div class="btn-group" role="group">
                                    <a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/create?receiverId=${receiver.id}">Edit</a>
                                    <form class="d-inline" method="post" action="${pageContext.request.contextPath}/receivers/action" onsubmit="return confirm('Delete this receiver?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${receiver.id}">
                                        <button type="submit" class="btn btn-sm btn-outline-danger">Delete</button>
                                    </form>
                                </div>
                                <c:if test="${receiver.status != 'SATISFIED'}">
                                    <form class="mt-2" method="post" action="${pageContext.request.contextPath}/assignments">
                                        <input type="hidden" name="receiverId" value="${receiver.id}">
                                        <input type="hidden" name="redirect" value="/receivers">
                                        <div class="input-group input-group-sm">
                                            <select class="form-select" name="donorId" required>
                                                <option value="">Assign donor...</option>
                                                <c:forEach items="${compatibleDonors[receiver.id]}" var="donor">
                                                    <option value="${donor.id}">${donor.firstName} ${donor.lastName} (${donor.bloodType})</option>
                                                </c:forEach>
                                            </select>
                                            <button class="btn btn-success" type="submit">Assign</button>
                                        </div>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <c:if test="${empty receivers}">
                    <div class="text-center text-muted py-4">No receivers registered yet.</div>
                </c:if>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
