package com.app.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.context.ApplicationContext;

import com.app.dto.LoginDto;
import com.app.helper.SessionKeys;
import com.app.helper.WebHelper;
import com.app.model.Utilisateur;
import com.app.service.AuthService;
import com.app.service.UtilisateurService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import johane.annotation.MonController;
import johane.annotation.UrlMapping;
import johane.util.ModAndView;
import johane.util.MySession;

@MonController
public class AuthController {

    /** "/" : redirige vers la page de login (ou vers /home si déjà connecté). */
    @UrlMapping("/")
    public void racine(MySession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        boolean connecte = session.getAttribute(SessionKeys.USER) != null;
        WebHelper.redirect(request, response, connecte ? "/home" : "/login");
    }

    @UrlMapping("/login")
    public ModAndView afficherLogin(MySession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (session.getAttribute(SessionKeys.USER) != null) {
            WebHelper.redirect(request, response, "/home");
            return null;
        }
        return new ModAndView("login", null);
    }

    @UrlMapping(value = "/login", method = "POST")
    public ModAndView login(LoginDto dto, ApplicationContext ctx, MySession session,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        AuthService authService = ctx.getBean(AuthService.class);

        if (!authService.auth(dto.getEmail(), dto.getPassword())) {
            ModAndView mav = new ModAndView("login", null);
            mav.addValue("erreur", "Email ou mot de passe incorrect.");
            mav.addValue("email", dto.getEmail());
            return mav;
        }

        // auth() est vrai : on retrouve l'utilisateur pour connaître son rôle
        Optional<Utilisateur> utilisateur = ctx.getBean(UtilisateurService.class).charger(dto.getEmail());
        if (utilisateur.isEmpty()) {
            ModAndView mav = new ModAndView("login", null);
            mav.addValue("erreur", "Compte introuvable.");
            return mav;
        }

        session.setAttribute(SessionKeys.USER, utilisateur.get().getEmail());
        session.setAttribute(SessionKeys.ROLE, utilisateur.get().getRole());

        WebHelper.redirect(request, response, "/home");
        return null;
    }

    @UrlMapping(value = "/logout", method = "POST")
    public void logout(MySession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        session.destroy(); // le framework vide la vraie HttpSession à la fin de la requête
        WebHelper.redirect(request, response, "/login");
    }
}
