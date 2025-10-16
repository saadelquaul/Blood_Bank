package org.blood_bank.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.blood_bank.service.ReceiverService;
import org.blood_bank.util.FlashScope;

import java.io.IOException;

public class ReceiverActionServlet extends HttpServlet {

    private ReceiverService receiverService;

    @Override
    public void init() {
        this.receiverService = new ReceiverService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");
        if (action == null || idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request");
            return;
        }
        Long receiverId = Long.valueOf(idParam);
        switch (action) {
            case "delete":
                receiverService.deleteReceiver(receiverId);
                FlashScope.setFlashMessage(request, "Receiver deleted successfully");
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
                return;
        }
        response.sendRedirect(request.getContextPath() + "/receivers");
    }
}

