package com.app.helper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.app.dto.PatientDto;
import com.app.model.Patient;
import com.app.model.Utilisateur;

public final class PatientHelper {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    public static final int MDP_MIN = 6;
    private static final int MDP_MAX = 100; // taille de la colonne utilisateur.mot_de_passe (VARCHAR(100))

    private PatientHelper() {
    }

    /** Nettoie les champs : espaces retirés, email en minuscules (le mot de passe n'est pas touché). */
    public static void normaliser(PatientDto dto) {
        if (dto.getNom() != null) {
            dto.setNom(dto.getNom().trim());
        }
        if (dto.getEmail() != null) {
            dto.setEmail(dto.getEmail().trim().toLowerCase());
        }
    }

    /**
     * Validation du formulaire. Renvoie champ -> message (map vide = formulaire valide).
     * creation = true : mot de passe obligatoire ; false : facultatif (vide = inchangé).
     */
    public static Map<String, String> valider(PatientDto dto, boolean creation) {
        Map<String, String> erreurs = new LinkedHashMap<>();

        String nom = dto.getNom();
        if (nom == null || nom.isBlank()) {
            erreurs.put("nom", "Le nom est obligatoire.");
        } else if (nom.length() > 100) {
            erreurs.put("nom", "Le nom ne doit pas dépasser 100 caractères.");
        }

        String email = dto.getEmail();
        if (email == null || email.isBlank()) {
            erreurs.put("email", "L'email est obligatoire.");
        } else if (email.length() > 150 || !EMAIL.matcher(email).matches()) {
            erreurs.put("email", "L'email n'est pas valide.");
        }

        String mdp = dto.getPassword();
        boolean mdpVide = mdp == null || mdp.isEmpty();
        if (mdpVide) {
            if (creation) {
                erreurs.put("password", "Le mot de passe est obligatoire.");
            }
        } else if (mdp.length() < MDP_MIN) {
            erreurs.put("password", "Le mot de passe doit contenir au moins " + MDP_MIN + " caractères.");
        } else if (mdp.length() > MDP_MAX) {
            erreurs.put("password", "Le mot de passe ne doit pas dépasser " + MDP_MAX + " caractères.");
        }
        return erreurs;
    }

    /** Remplit l'objet métier à partir du DTO (le mot de passe est géré par le service, pas ici). */
    public static void remplirUtilisateur(Utilisateur cible, PatientDto dto) {
        cible.setNom(dto.getNom());
        cible.setEmail(dto.getEmail());
    }

    /** Sens inverse : pré-remplit le formulaire de modification (sans mot de passe). */
    public static PatientDto versDto(Patient patient) {
        PatientDto dto = new PatientDto();
        dto.setId(patient.getId());
        dto.setNom(patient.getNom());
        dto.setEmail(patient.getEmail());
        return dto;
    }
}