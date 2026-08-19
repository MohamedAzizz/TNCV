package com.tncv.user_service.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "users",
    indexes = {
        @Index(
            name = "idx_user_keycloak_id",
            columnList = "keycloak_id"
        ),
        @Index(
            name = "idx_user_email",
            columnList = "email"
        )
    }
)
public class User {

    // ============================================================
    // ID
    // ============================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // ============================================================
    // KEYCLOAK
    // ============================================================

    @Column(
        name = "keycloak_id",
        nullable = false,
        unique = true,
        length = 100
    )
    private String keycloakId;


    // ============================================================
    // INFORMATIONS PERSONNELLES
    // ============================================================

    @Column(
        name = "first_name",
        nullable = false,
        length = 100
    )
    private String firstName;


    @Column(
        name = "last_name",
        nullable = false,
        length = 100
    )
    private String lastName;


    @Column(
        name = "email",
        nullable = false,
        unique = true,
        length = 150
    )
    private String email;


    @Column(
        name = "phone",
        length = 30
    )
    private String phone;


    @Column(
        name = "date_of_birth"
    )
    private LocalDate dateOfBirth;


    // ============================================================
    // PHOTO DE PROFIL
    // ============================================================

    @Column(
        name = "profile_photo",
        length = 500
    )
    private String profilePhoto;


    // ============================================================
    // ADRESSE
    // ============================================================

    @Column(
        name = "address",
        length = 255
    )
    private String address;


    @Column(
        name = "country",
        length = 100
    )
    private String country;


    // ============================================================
    // DIPLOMAS
    // ============================================================

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER
    )
    private List<Diploma> diplomas = new ArrayList<>();


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public User() {
    }


    // ============================================================
    // GETTERS / SETTERS
    // ============================================================

    public Long getId() {
        return id;
    }


    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
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


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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


    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public List<Diploma> getDiplomas() {
        return diplomas;
    }

    public void setDiplomas(List<Diploma> diplomas) {
        this.diplomas = diplomas;
    }


    // ============================================================
    // ADD DIPLOMA
    // ============================================================

    public void addDiploma(Diploma diploma) {

        diplomas.add(diploma);

        diploma.setUser(this);
    }


    // ============================================================
    // REMOVE DIPLOMA
    // ============================================================

    public void removeDiploma(Diploma diploma) {

        diplomas.remove(diploma);

        diploma.setUser(null);
    }
}