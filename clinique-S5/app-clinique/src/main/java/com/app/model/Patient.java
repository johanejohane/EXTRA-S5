package com.app.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Fiche patient (table patient) jointe à l'utilisateur correspondant. */
public class Patient {
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Integer id;
    private Integer utilisateurId;
    private String nom;
    private String email;
    private LocalDateTime dateCreation;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(Integer utilisateurId) { this.utilisateurId = utilisateurId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    /** Pour les JSP (JSTL fmt ne sait pas formater java.time). */
    public String getDateCreationFormatee() {
        return dateCreation == null ? "" : dateCreation.format(FORMAT);
    }
}
