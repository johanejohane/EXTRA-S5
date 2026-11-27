package com.app.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.model.Utilisateur;
import com.app.repository.UtilisateurRepository;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    /** Charge l'utilisateur (avec son rôle) à partir de son email. */
    public Optional<Utilisateur> charger(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        return utilisateurRepository.findByEmail(email.trim().toLowerCase());
    }
}
