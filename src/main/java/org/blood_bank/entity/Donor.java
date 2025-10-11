package org.blood_bank.entity;


import org.blood_bank.entity.converters.MedicalFlagsConverter;
import org.blood_bank.entity.enums.BloodGroup;
import org.blood_bank.entity.enums.DonorAvailabilityStatus;
import org.blood_bank.entity.enums.Gender;
import org.blood_bank.entity.enums.MedicalFlags;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "donors")
public class Donor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, name = "first_name")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Column(nullable = false)
    private String cin;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, name ="date_Of_Birth")
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private Double weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "blood_Type")
    private BloodGroup bloodType;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name ="availability_Status")
    private DonorAvailabilityStatus availabilityStatus =
            DonorAvailabilityStatus.AVAILABLE;

    @Column(name = "medical_flags", length = 255)
    @Convert(converter = MedicalFlagsConverter.class)
    private Set<MedicalFlags> medicalFlags = new HashSet<>();


    private LocalDate lastDonationDate;

    @ManyToOne
    @JoinColumn(name = "current_receiver_id")
    private Receiver currentReceiver;

    @OneToMany (mappedBy = "donor")
    private Set<Donation> donations = new HashSet<>();

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

    public String getCIN() {
        return cin;
    }

    public void setCIN(String CIN) {
        this.cin = CIN;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDateOfBirth () {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender)
    {
        this.gender  = gender;
    }

    public BloodGroup getBloodType() {
        return bloodType;
    }

    public void setBloodType(BloodGroup bloodType) {
        this.bloodType = bloodType;
    }

    public DonorAvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(DonorAvailabilityStatus availabilityStatus){
        this.availabilityStatus = availabilityStatus;
    }

    public Set<MedicalFlags> getMedicalFlags() {
        return medicalFlags;
    }

    public void setMedicalFlags(Set<MedicalFlags> medicalFlags)
    {
        this.medicalFlags = medicalFlags;
    }

    public LocalDate getLastDonationDate() {
        return lastDonationDate;
    }

    public void setLastDonationDate(LocalDate lastDonationDate) {
        this.lastDonationDate = lastDonationDate;
    }

    public Receiver getCurrentReceiver() {
        return currentReceiver;
    }

    public void setCurrentReceiver(Receiver currentReceiver)
    {
        this.currentReceiver = currentReceiver;
    }

    public Set<Donation> getDonations() {
        return donations;
    }


    public void setDonations(Set<Donation> donations) {
        this.donations = donations;
    }

    public int getAge() {
        if (dateOfBirth == null) {
            return 0;
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public boolean isAvailable() {
        return availabilityStatus == DonorAvailabilityStatus.AVAILABLE;
    }

    public boolean isEligible() {
        return availabilityStatus != DonorAvailabilityStatus.NOT_ELIGIBLE;
    }

    @Override
    public boolean equals(Object o) {

        if(o == null || getClass() != o.getClass()) return false;
        if(this == null) return true;
        Donor donor = (Donor) o;
        return Objects.equals(id, donor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
