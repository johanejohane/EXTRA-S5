package com.app.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.ApplicationContext;

import com.app.dto.PatientDto;
import com.app.helper.PatientHelper;
import com.app.helper.SessionKeys;
import com.app.helper.WebHelper;
import com.app.model.Patient;
import com.app.service.EmailDejaUtiliseException;
import com.app.service.PatientService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import johane.annotation.Auth;
import johane.annotation.MonController;
import johane.annotation.UrlMapping;
import johane.util.ModAndView;

/** CRUD des patients : réservé aux médecins. */
@MonController
public class PatientController {

    private static final Map<String, String> MESSAGES = Map.of(
            "cree", "Patient créé avec succès.",
            "modifie", "Patient modifié avec succès.",
            "supprime", "Patient supprimé avec succès.");

    // ---------- Liste ----------

    @Auth(roles = SessionKeys.ROLE_MEDECIN)
    @UrlMapping("/patients")
    public ModAndView lister(ApplicationContext ctx, String success) {
        ModAndView mav = new ModAndView("listePatient", null);
        mav.addValue("patients", ctx.getBean(PatientService.class).getAllPatients());
        String message = success == null ? null : MESSAGES.get(success); // on n'affiche jamais le paramètre brut
        if (message != null) {
            mav.addValue("message", message);
        }
        return mav;
    }

    // ---------- Create ----------

    @Auth(roles = SessionKeys.ROLE_MEDECIN)
    @UrlMapping("/patients/nouveau")
    public ModAndView nouveau() {
        return formulaire(new PatientDto(), true, new LinkedHashMap<>());
    }

    @Auth(roles = SessionKeys.ROLE_MEDECIN)
    @UrlMapping(value = "/patients/creer", method = "POST")
    public ModAndView creer(PatientDto dto, ApplicationContext ctx,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        PatientHelper.normaliser(dto);
        Map<String, String> erreurs = PatientHelper.valider(dto, true);

        if (erreurs.isEmpty()) {
            try {
                ctx.getBean(PatientService.class).creer(dto);
                WebHelper.redirect(request, response, "/patients?success=cree");
                return null;
            } catch (EmailDejaUtiliseException e) {
                erreurs.put("email", "Cet email est déjà utilisé.");
            }
        }
        return formulaire(dto, true, erreurs); // on réaffiche le formulaire avec les erreurs
    }

    // ---------- Update ----------

    @Auth(roles = SessionKeys.ROLE_MEDECIN)
    @UrlMapping("/patients/modifier")
    public ModAndView modifierForm(ApplicationContext ctx, int id, HttpServletResponse response)
            throws IOException {
        Optional<Patient> patient = ctx.getBean(PatientService.class).getPatient(id);
        if (patient.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Patient introuvable");
            return null;
        }
        return formulaire(PatientHelper.versDto(patient.get()), false, new LinkedHashMap<>());
    }

    @Auth(roles = SessionKeys.ROLE_MEDECIN)
    @UrlMapping(value = "/patients/modifier", method = "POST")
    public ModAndView modifier(PatientDto dto, ApplicationContext ctx,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (dto.getId() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Identifiant manquant");
            return null;
        }
        PatientHelper.normaliser(dto);
        Map<String, String> erreurs = PatientHelper.valider(dto, false);

        if (erreurs.isEmpty()) {
            try {
                ctx.getBean(PatientService.class).modifier(dto);
                WebHelper.redirect(request, response, "/patients?success=modifie");
                return null;
            } catch (EmailDejaUtiliseException e) {
                erreurs.put("email", "Cet email est déjà utilisé.");
            } catch (IllegalArgumentException e) { // patient introuvable
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Patient introuvable");
                return null;
            }
        }
        return formulaire(dto, false, erreurs);
    }

    // ---------- Delete (simple pour l'instant) ----------

    @Auth(roles = SessionKeys.ROLE_MEDECIN)
    @UrlMapping(value = "/patients/supprimer", method = "POST")
    public void supprimer(ApplicationContext ctx, int id,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        ctx.getBean(PatientService.class).supprimer(id);
        WebHelper.redirect(request, response, "/patients?success=supprime");
    }

    // ---------- Read ----------

    @Auth(roles = SessionKeys.ROLE_MEDECIN)
    @UrlMapping("/patients/detail")
    public ModAndView detail(ApplicationContext ctx, int id, HttpServletResponse response) throws IOException {
        Optional<Patient> patient = ctx.getBean(PatientService.class).getPatient(id);
        if (patient.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Patient introuvable");
            return null;
        }
        ModAndView mav = new ModAndView("detailPatient", null);
        mav.addValue("patient", patient.get());
        return mav;
    }

    // ---------- commun ----------

    private ModAndView formulaire(PatientDto dto, boolean creation, Map<String, String> erreurs) {
        ModAndView mav = new ModAndView("formPatient", null);
        mav.addValue("patient", dto);
        mav.addValue("creation", creation);
        mav.addValue("errors", erreurs);
        mav.addValue("action", creation ? "/patients/creer" : "/patients/modifier");
        return mav;
    }
}
