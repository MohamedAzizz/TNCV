package com.tncv.user_service.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "diplomas")
public class Diploma {

    // ============================================================
    // ID
    // ============================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // ============================================================
    // USER
    // ============================================================

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        nullable = false
    )
    private User user;


    // ============================================================
    // DIPLOME
    // ============================================================

    @Column(
        name = "degree",
        nullable = false,
        length = 150
    )
    private String degree;


    // ============================================================
    // ETABLISSEMENT
    // ============================================================

    @Column(
        name = "institution",
        nullable = false,
        length = 200
    )
    private String institution;


    // ============================================================
    // SPECIALITE
    // ============================================================

    @Column(
        name = "field_of_study",
        length = 200
    )
    private String fieldOfStudy;


    // ============================================================
    // ANNEE DE DEBUT
    // ============================================================

    @Column(name = "start_year")
    private Integer startYear;


    // ============================================================
    // ANNEE DE FIN
    // ============================================================

    @Column(name = "end_year")
    private Integer endYear;


    // ============================================================
    // CONSTRUCTEUR
    // ============================================================

    public Diploma() {
    }


    // ============================================================
    // GETTERS / SETTERS
    // ============================================================

    public Long getId() {
        return id;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }


    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }


    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }


    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }


    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }
}