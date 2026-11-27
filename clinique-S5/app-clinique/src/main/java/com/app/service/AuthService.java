package com.app.service;

import java.util.Optional;
import org.springframework.stereotype.Service;

import com.app.model.Utilisateur;
import com.app.repository.UtilisateurRepository;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;


    public AuthService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    /** Vrai seulement si l'email existe ET que le mot de passe correspond. */
    public boolean auth(String email, String motDePasse) {
        if (email == null || email.isBlank() || motDePasse == null || motDePasse.isEmpty()) {
            return false;
        }
        Optional<Utilisateur> utilisateur = utilisateurRepository.findByEmail(email.trim().toLowerCase()); // check email
        if (utilisateur.isEmpty()) {
            return false;
        }
        return motDePasse.equals(utilisateur.get().getMotDePasse()); // check mdp
    }
}