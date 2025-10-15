package org.blood_bank.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.blood_bank.service.AssignmentService;
import org.blood_bank.util.FlashScope;

import java.io.IOException;

public class AssignmentServlet extends HttpServlet {

    private AssignmentService assignmentService;

    @Override
    public void init() {
        this.assignmentService = new AssignmentService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String donorIdParam = request.getParameter("donorId");
        String receiverIdParam = request.getParameter("receiverId");
        if (donorIdParam == null || receiverIdParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing donorId or receiverId");
            return;
        }
        Long donorId = Long.valueOf(donorIdParam);
        Long receiverId = Long.valueOf(receiverIdParam);

        AssignmentService.AssignmentResult result = assignmentService.assign(donorId, receiverId);
        if (result.isSuccess()) {
            FlashScope.setFlashMessage(request, "Donor assigned successfully");
        } else {
            FlashScope.setFlashMessage(request, result.getMessage());
        }

        String redirect = request.getParameter("redirect");
        if (redirect == null || redirect.isBlank()) {
            redirect = "/donors";
        }
        response.sendRedirect(request.getContextPath() + redirect);
    }
}

