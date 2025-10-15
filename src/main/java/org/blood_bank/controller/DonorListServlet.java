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
import org.blood_bank.service.DonorService;
import org.blood_bank.service.ReceiverService;
import org.blood_bank.util.FlashScope;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DonorListServlet extends HttpServlet {

    private DonorService donorService;
    private ReceiverService receiverService;

    @Override
    public void init() {
        this.donorService = new DonorService();
        this.receiverService = new ReceiverService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Donor> donors = donorService.findALl();
        List<Receiver> receivers = receiverService.findAllSorted();

        Map<Long, List<Receiver>> compatibleReceivers = donors.stream()
                .filter(Donor::isAvailable)
                .collect(Collectors.toMap(Donor::getId, donor -> receivers.stream()
                        .filter(receiver -> receiver.getStatus() == ReceiverStatus.PENDING)
                        .filter(receiver -> donor.getBloodType().canDonateTo(receiver.getBloodGroup()))
                        .collect(Collectors.toList())));

        Map<String, String> statusColors = Map.of(
                DonorAvailabilityStatus.AVAILABLE.name(), "success",
                DonorAvailabilityStatus.NOT_AVAILABLE.name(), "warning",
                DonorAvailabilityStatus.NOT_ELIGIBLE.name(), "danger"
        );

        request.setAttribute("donors", donors);
        request.setAttribute("compatibleReceivers", compatibleReceivers);
        request.setAttribute("statusColors", statusColors);
        request.setAttribute("flashMessage", FlashScope.consumeFlashMessage(request));

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/donors.jsp");
        dispatcher.forward(request, response);
    }
}

