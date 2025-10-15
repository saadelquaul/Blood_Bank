<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Donor List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/styles.css">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/create">Blood Bank Manager</a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/donors">Donors</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/receivers">Receivers</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="container">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="mb-0">Registered Donors</h2>
        <a class="btn btn-outline-light text-dark" href="${pageContext.request.contextPath}/create">Add New Donor</a>
    </div>
    <c:if test="${not empty flashMessage}">
        <div class="alert alert-info">${flashMessage}</div>
    </c:if>
    <div class="card shadow-sm">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-striped align-middle">
                    <thead class="table-dark">
                    <tr>
                        <th>#</th>
                        <th>Full Name</th>
                        <th>Blood Group</th>
                        <th>Age</th>
                        <th>Weight (kg)</th>
                        <th>Phone</th>
                        <th>Status</th>
                        <th>Associated Receiver</th>
                        <th>Last Donation</th>
                        <th class="text-end">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${donors}" var="donor" varStatus="loop">
                        <tr class="${donor.availabilityStatus == 'INELIGIBLE' ? 'table-warning' : ''}">
                            <td>${loop.index + 1}</td>
                            <td>
                                <div class="fw-semibold">${donor.firstName} ${donor.lastName}</div>
                                <small class="text-muted">${donor.cin}</small>
                            </td>
                            <td>${donor.bloodGroup}</td>
                            <td>${donor.age}</td>
                            <td>${donor.weight}</td>
                            <td>
                                <div>${donor.phone}</div>
                                <small class="text-muted">${donor.email}</small>
                            </td>
                            <td><span class="badge bg-${statusColors[donor.availabilityStatus]}">${donor.availabilityStatus}</span></td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty donor.currentReceiver}">
                                        <div>${donor.currentReceiver.firstName} ${donor.currentReceiver.lastName}</div>
                                        <small class="text-muted">${donor.currentReceiver.urgency}</small>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">None</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty donor.lastDonationDate}">
                                        ${donor.lastDonationDate}
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">Never</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-end">
                                <div class="btn-group" role="group">
                                    <a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/create?donorId=${donor.id}">Edit</a>
                                    <form class="d-inline" method="post" action="${pageContext.request.contextPath}/donors/action" onsubmit="return confirm('Delete this donor?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${donor.id}">
                                        <button type="submit" class="btn btn-sm btn-outline-danger">Delete</button>
                                    </form>
                                </div>
                                <c:if test="${donor.availabilityStatus == 'AVAILABLE'}">
                                    <form class="mt-2" method="post" action="${pageContext.request.contextPath}/assignments">
                                        <input type="hidden" name="donorId" value="${donor.id}">
                                        <input type="hidden" name="redirect" value="/donors">
                                        <div class="input-group input-group-sm">
                                            <select class="form-select" name="receiverId" required>
                                                <option value="">Assign to...</option>
                                                <c:forEach items="${compatibleReceivers[donor.id]}" var="receiver">
                                                    <option value="${receiver.id}">${receiver.firstName} ${receiver.lastName} (${receiver.urgency})</option>
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
                <c:if test="${empty donors}">
                    <div class="text-center text-muted py-4">No donors registered yet.</div>
                </c:if>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
