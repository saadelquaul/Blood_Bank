package org.blood_bank.entity;

import org.blood_bank.entity.enums.BloodGroup;
import org.blood_bank.entity.enums.Gender;
import org.blood_bank.entity.enums.ReceiverStatus;
import org.blood_bank.entity.enums.ReceiverUrgency;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "receivers")
public class Receiver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true, length = 32)
    private String cin;

    @Column(nullable = false, length = 32)
    private String phone;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private BloodGroup bloodGroup;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private ReceiverUrgency urgency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private ReceiverStatus status = ReceiverStatus.PENDING;

    @Column(nullable = false)
    private Integer requiredUnits;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Donation> donations = new HashSet<>();

    public ReceiverUrgency getUrgency() {
        return urgency;
    }

    public void setUrgency(ReceiverUrgency urgency) {
        this.urgency = urgency;
        if (urgency != null) {
            this.requiredUnits = urgency.getRequiredUnits();
        }
    }

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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public ReceiverStatus getStatus() {
        return status;
    }

    public void setStatus(ReceiverStatus status) {
        this.status = status;
    }

    public Integer getRequiredUnits() {
        return requiredUnits;
    }

    public void setRequiredUnits(Integer requiredUnits) {
        this.requiredUnits = requiredUnits;
    }

    public Set<Donation> getDonations() {
        return donations;
    }

    public void setDonations(Set<Donation> donations) {
        this.donations = donations;
    }

    public int getAssignedUnits() {
        return donations != null ? donations.size() : 0;
    }

    public int getRemainingUnits() {
        if (requiredUnits == null) {
            return 0;
        }
        return Math.max(requiredUnits - getAssignedUnits(), 0);
    }

    public void refreshStatus() {
        if (getAssignedUnits() >= requiredUnits) {
            status = ReceiverStatus.SATISFIED;
        } else {
            status = ReceiverStatus.PENDING;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receiver receiver = (Receiver) o;
        return Objects.equals(id, receiver.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }



}
