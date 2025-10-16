package org.blood_bank.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.blood_bank.entity.Donor;
import org.blood_bank.entity.Receiver;
import org.blood_bank.entity.enums.DonorAvailabilityStatus;
import org.blood_bank.entity.enums.ReceiverStatus;
import org.blood_bank.entity.enums.ReceiverUrgency;
import org.blood_bank.service.DonorService;
import org.blood_bank.service.ReceiverService;
import org.blood_bank.util.FlashScope;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReceiverListServlet extends HttpServlet {

    private ReceiverService receiverService;
    private DonorService donorService;

    @Override
    public void init() {
        this.receiverService = new ReceiverService();
        this.donorService = new DonorService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Receiver> receivers = receiverService.findAllSorted();
        List<Donor> donors = donorService.findALl();

        Map<Long, List<Donor>> compatibleDonors = receivers.stream()
                .collect(Collectors.toMap(Receiver::getId, receiver -> donors.stream()
                        .filter(donor -> donor.getAvailabilityStatus() == DonorAvailabilityStatus.AVAILABLE)
                        .filter(donor -> donor.getBloodType().canDonateTo(receiver.getBloodGroup()))
                        .collect(Collectors.toList())));

        Map<String, String> statusColors = Map.of(
                ReceiverStatus.SATISFIED.name(), "success",
                ReceiverStatus.PENDING.name(), "warning"
        );
        Map<String, String> urgencyColors = Map.of(
                ReceiverUrgency.CRITICAL.name(), "danger",
                ReceiverUrgency.URGENT.name(), "warning",
                ReceiverUrgency.NORMAL.name(), "success"
        );

        request.setAttribute("receivers", receivers);
        request.setAttribute("compatibleDonors", compatibleDonors);
        request.setAttribute("statusColors", statusColors);
        request.setAttribute("urgencyColors", urgencyColors);
        request.setAttribute("flashMessage", FlashScope.consumeFlashMessage(request));

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/receivers.jsp");
        dispatcher.forward(request, response);
    }
}

