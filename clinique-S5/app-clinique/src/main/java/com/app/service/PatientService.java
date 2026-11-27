package com.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.PatientDto;
import com.app.helper.PatientHelper;
import com.app.helper.SessionKeys;
import com.app.model.Patient;
import com.app.model.Utilisateur;
import com.app.repository.PatientRepository;
import com.app.repository.UtilisateurRepository;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UtilisateurRepository utilisateurRepository;

    public PatientService(PatientRepository patientRepository, UtilisateurRepository utilisateurRepository)
             {
        this.patientRepository = patientRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Transactional(readOnly = true)
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Patient> getPatient(int id) {
        return patientRepository.findById(id);
    }

    /** Crée l'utilisateur (rôle patient, mot de passe hashé) puis sa fiche patient, dans une seule transaction. */
    @Transactional
    public int creer(PatientDto dto) {
        PatientHelper.normaliser(dto);
        if (utilisateurRepository.emailExiste(dto.getEmail(), null)) {
            throw new EmailDejaUtiliseException(dto.getEmail());
        }
        Utilisateur utilisateur = new Utilisateur();
        PatientHelper.remplirUtilisateur(utilisateur, dto);
        utilisateur.setMotDePasse(dto.getPassword());
        try {
            int utilisateurId = utilisateurRepository.insert(utilisateur, SessionKeys.ROLE_PATIENT);
            return patientRepository.insert(utilisateurId);
        } catch (DuplicateKeyException e) { // deux créations simultanées avec le même email
            throw new EmailDejaUtiliseException(dto.getEmail());
        }
    }

    /** Mot de passe vide dans le dto = on garde l'ancien. */
    @Transactional
    public void modifier(PatientDto dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException("Patient sans identifiant");
        }
        PatientHelper.normaliser(dto);
        Patient patient = patientRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Patient introuvable : " + dto.getId()));
        if (utilisateurRepository.emailExiste(dto.getEmail(), patient.getUtilisateurId())) {
            throw new EmailDejaUtiliseException(dto.getEmail());
        }
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(patient.getUtilisateurId());
        PatientHelper.remplirUtilisateur(utilisateur, dto);
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            utilisateur.setMotDePasse(dto.getPassword());
        }
        try {
            utilisateurRepository.update(utilisateur);
        } catch (DuplicateKeyException e) {
            throw new EmailDejaUtiliseException(dto.getEmail());
        }
    }

    /** Suppression simple pour l'instant (pas encore de dossiers médicaux liés). */
    @Transactional
    public boolean supprimer(int id) {
        return patientRepository.deleteById(id);
    }
}
