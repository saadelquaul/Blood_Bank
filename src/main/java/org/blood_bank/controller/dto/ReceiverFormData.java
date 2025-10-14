package org.blood_bank.controller.dto;

import org.blood_bank.entity.Receiver;
import org.blood_bank.entity.enums.BloodGroup;
import org.blood_bank.entity.enums.Gender;
import org.blood_bank.entity.enums.ReceiverUrgency;

import java.time.LocalDate;

public class ReceiverFormData {


    private Long id;
    private String firstName;
    private String lastName;
    private String cin;
    private String phone;
    private String dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String urgency;

    public static ReceiverFormData fromReceiver(Receiver receiver) {
        ReceiverFormData data = new ReceiverFormData();
        data.setId(receiver.getId());
        data.setFirstName(receiver.getFirstName());
        data.setLastName(receiver.getLastName());
        data.setCin(receiver.getCin());
        data.setPhone(receiver.getPhone());
        data.setDateOfBirth(receiver.getDateOfBirth() != null ? receiver.getDateOfBirth().toString() : null);
        data.setGender(receiver.getGender() != null ? receiver.getGender().name() : null);
        data.setBloodGroup(receiver.getBloodGroup() != null ? receiver.getBloodGroup().getLabel() : null);
        data.setUrgency(receiver.getUrgency() != null ? receiver.getUrgency().name() : null);
        return data;
    }

    public Receiver toReceiver() {
        Receiver receiver = new Receiver();
        receiver.setId(id);
        receiver.setFirstName(firstName);
        receiver.setLastName(lastName);
        receiver.setCin(cin);
        receiver.setPhone(phone);
        if (dateOfBirth != null && !dateOfBirth.isBlank()) {
            receiver.setDateOfBirth(LocalDate.parse(dateOfBirth));
        }
        if (gender != null && !gender.isBlank()) {
            receiver.setGender(Gender.valueOf(gender));
        }
        if (bloodGroup != null && !bloodGroup.isBlank()) {
            receiver.setBloodGroup(BloodGroup.fromLabel(bloodGroup));
        }
        if (urgency != null && !urgency.isBlank()) {
            receiver.setUrgency(ReceiverUrgency.valueOf(urgency));
            receiver.setRequiredUnits(ReceiverUrgency.valueOf(urgency).getRequiredUnits());
        }
        return receiver;
    }

    // Getters and setters omitted for brevity

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

}
