package com.app.dto;

/** Utilisé pour créer ET modifier un patient (id = id de la table patient, null à la création). */
public class PatientDto {
    private Integer id;
    private String nom;
    private String email;
    private String password;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
