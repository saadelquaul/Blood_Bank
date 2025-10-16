package org.blood_bank.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.blood_bank.controller.dto.DonorFormData;
import org.blood_bank.controller.dto.ReceiverFormData;
import org.blood_bank.entity.Donor;
import org.blood_bank.entity.Receiver;
import org.blood_bank.entity.enums.BloodGroup;

import org.blood_bank.entity.enums.ReceiverUrgency;
import org.blood_bank.service.DonorService;
import org.blood_bank.service.ReceiverService;
import org.blood_bank.util.FlashScope;


import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CreatePageServlet extends HttpServlet {


    private DonorService donorService;
    private ReceiverService receiverService;

    @Override
    public void init() {
        this.donorService = new DonorService();
        this.receiverService = new ReceiverService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        prepareReferenceData(request);
        request.setAttribute("flashMessage", FlashScope.consumeFlashMessage(request));

        String servletPath = request.getServletPath();

        if (servletPath.equals("/donor")) {
            DonorFormData donorForm = resolveDonorForm(request.getParameter("donorId"));
            request.setAttribute("donorForm", donorForm);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/donor.jsp");
            dispatcher.forward(request, response);
        } else if ( servletPath.equals("/receiver")) {
            ReceiverFormData receiverForm = resolveReceiverForm(request.getParameter("receiverId"));
            request.setAttribute("receiverForm", receiverForm);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/receiver.jsp");
            dispatcher.forward(request, response);
        } else {
            forwardToView(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String formType = request.getParameter("formType");
        if ("donor".equalsIgnoreCase(formType)) {
            handleDonorSubmission(request, response);
        } else if ("receiver".equalsIgnoreCase(formType)) {
            handleReceiverSubmission(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported form type");
        }
    }

    private void handleDonorSubmission(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DonorFormData donorForm = populateDonorForm(request);
        Donor donor = donorForm.toDonor();

        if (donor.getId() != null) {
        donorService.findById(donor.getId()).ifPresent(existing -> {
            donor.setCurrentReceiver(existing.getCurrentReceiver());
            donor.setDonations(existing.getDonations());
        });
        }

        List<String> errors = donorService.validateDonor(donor);
        donorService.findByCin(donor.getCin()).ifPresent(existing -> {
            if (donor.getId() == null || !existing.getId().equals(donor.getId())) {
                errors.add("Another donor already uses this CIN");
            }
        });

        if (!errors.isEmpty()) {
            prepareReferenceData(request);
            request.setAttribute("donorErrors", errors);
            request.setAttribute("donorForm", donorForm);
            request.setAttribute("receiverForm", resolveReceiverForm(request.getParameter("receiverId")));
            forwardToView(request, response);
            return;
        }

        donorService.saveDonor(donor);
        FlashScope.setFlashMessage(request, donorForm.getId() == null ? "Donor created successfully" : "Donor updated successfully");
        response.sendRedirect(request.getContextPath() + "/donors");
    }

    private void handleReceiverSubmission(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReceiverFormData receiverForm = populateReceiverForm(request);
        Receiver receiver = receiverForm.toReceiver();

        if (receiver.getId() != null) {
            receiverService.findById(receiver.getId())
                    .ifPresent(existing -> receiver.setDonations(existing.getDonations()));
        }
        List<String> errors = receiverService.validateReceiver(receiver);
        receiverService.findByCin(receiver.getCin()).ifPresent(existing -> {
            if (receiver.getId() == null || !existing.getId().equals(receiver.getId())) {
                errors.add("Another receiver already uses this CIN");
            }
        });

        if (!errors.isEmpty()) {
            prepareReferenceData(request);
            request.setAttribute("receiverErrors", errors);
            request.setAttribute("receiverForm", receiverForm);
            request.setAttribute("donorForm", resolveDonorForm(request.getParameter("donorId")));
            forwardToView(request, response);
            return;
        }

        receiverService.saveReceiver(receiver);
        FlashScope.setFlashMessage(request, receiverForm.getId() == null ? "Receiver created successfully" : "Receiver updated successfully");
        response.sendRedirect(request.getContextPath() + "/receivers");
    }

    private DonorFormData populateDonorForm(HttpServletRequest request) {
        DonorFormData form = new DonorFormData();
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isBlank()) {
            form.setId(Long.valueOf(idParam));
        }
        form.setFirstName(request.getParameter("firstName"));
        form.setLastName(request.getParameter("lastName"));
        form.setCin(request.getParameter("cin"));
        form.setPhone(request.getParameter("phone"));
        form.setDateOfBirth(request.getParameter("dateOfBirth"));
        form.setGender(request.getParameter("gender"));
        form.setBloodGroup(request.getParameter("bloodGroup"));
        String weightParam = request.getParameter("weight");
        if (weightParam != null && !weightParam.isBlank()) {
            form.setWeight(Double.valueOf(weightParam));
        }
        String[] flags = request.getParameterValues("medicalFlags");
        if (flags != null) {
            Set<String> flagSet = Arrays.stream(flags).collect(Collectors.toSet());
            form.setMedicalFlags(flagSet);
        } else {
            form.setMedicalFlags(new HashSet<>());
        }
        return form;
    }

    private ReceiverFormData populateReceiverForm(HttpServletRequest request) {
        ReceiverFormData form = new ReceiverFormData();
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isBlank()) {

            form.setId(Long.valueOf(idParam));
        }
        form.setFirstName(request.getParameter("firstName"));
        form.setLastName(request.getParameter("lastName"));
        form.setCin(request.getParameter("cin"));
        form.setPhone(request.getParameter("phone"));
        form.setDateOfBirth(request.getParameter("dateOfBirth"));
        form.setGender(request.getParameter("gender"));
        form.setBloodGroup(request.getParameter("bloodGroup"));
        form.setUrgency(request.getParameter("urgency"));
        return form;
    }

    private DonorFormData resolveDonorForm(String donorIdParam) {
        if (donorIdParam != null && !donorIdParam.isBlank()) {
            try {
                Long donorId = Long.valueOf(donorIdParam);
                return donorService.findById(donorId)
                        .map(DonorFormData::fromDonor)
                        .orElseGet(DonorFormData::new);
            } catch (NumberFormatException ignored) {
                return new DonorFormData();
            }
        }
        return new DonorFormData();
    }

    private ReceiverFormData resolveReceiverForm(String receiverIdParam) {
        if (receiverIdParam != null && !receiverIdParam.isBlank()) {
            try {
                Long receiverId = Long.valueOf(receiverIdParam);
                return receiverService.findById(receiverId)
                        .map(ReceiverFormData::fromReceiver)
                        .orElseGet(ReceiverFormData::new);
            } catch (NumberFormatException ignored) {
                return new ReceiverFormData();
            }
        }
        return new ReceiverFormData();
    }

    private void prepareReferenceData(HttpServletRequest request) {
        List<String> bloodGroups = Arrays.stream(BloodGroup.values())
                .map(BloodGroup::getLabel)
                .collect(Collectors.toList());
        request.setAttribute("bloodGroups", bloodGroups);
        request.setAttribute("urgencies", Arrays.stream(ReceiverUrgency.values())
                .map(Enum::name)
                .collect(Collectors.toList()));
    }

    private void forwardToView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/create.jsp");
        dispatcher.forward(request, response);
    }
}

