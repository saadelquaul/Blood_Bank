package org.blood_bank.controller.dto;

import org.blood_bank.entity.Donor;
import org.blood_bank.entity.enums.BloodGroup;
import org.blood_bank.entity.enums.Gender;
import org.blood_bank.entity.enums.MedicalFlags;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DonorFormData {


    private Long id;
    private String firstName;
    private String lastName;
    private String cin;
    private String phone;
    private String dateOfBirth;
    private Double weight;
    private String gender;
    private String bloodGroup;
    private Set<String> medicalFlags = new HashSet<>();

    public static DonorFormData fromDonor(Donor donor) {
        DonorFormData data = new DonorFormData();
        data.setId(donor.getId());
        data.setFirstName(donor.getFirstName());
        data.setLastName(donor.getLastName());
        data.setCin(donor.getCin());
        data.setPhone(donor.getPhone());
        data.setDateOfBirth(donor.getDateOfBirth() != null ? donor.getDateOfBirth().toString() : null);
        data.setWeight(donor.getWeight());
        data.setGender(donor.getGender() != null ? donor.getGender().name() : null);
        data.setBloodGroup(donor.getBloodType() != null ? donor.getBloodType().getLabel() : null);
        data.setMedicalFlags(donor.getMedicalFlags().stream().map(Enum::name).collect(Collectors.toSet()));
        return data;
    }

    public Donor toDonor() {
        Donor donor = new Donor();
        donor.setId(id);
        donor.setFirstName(firstName);
        donor.setLastName(lastName);
        donor.setCIN(cin);
        donor.setPhone(phone);
        donor.setWeight(weight);
        if (dateOfBirth != null && !dateOfBirth.isBlank()) {
            donor.setDateOfBirth(LocalDate.parse(dateOfBirth));
        }
        if (gender != null && !gender.isBlank()) {
            donor.setGender(Gender.valueOf(gender));
        }
        if (bloodGroup != null && !bloodGroup.isBlank()) {
            donor.setBloodType(BloodGroup.fromLabel(bloodGroup));
        }
        if (medicalFlags != null) {
            donor.setMedicalFlags(medicalFlags.stream().map(MedicalFlags::valueOf).collect(Collectors.toSet()));
        }
        return donor;
    }

    public boolean hasFlag(String flag) {
        return medicalFlags != null && medicalFlags.contains(flag);
    }

    // Getters and setters

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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
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

    public Set<String> getMedicalFlags() {
        return medicalFlags;
    }

    public void setMedicalFlags(Set<String> medicalFlags) {
        this.medicalFlags = medicalFlags;
    }

}
