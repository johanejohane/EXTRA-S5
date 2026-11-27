package com.app.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.context.ApplicationContext;

import com.app.helper.SessionKeys;
import com.app.helper.WebHelper;
import com.app.model.Utilisateur;
import com.app.service.UtilisateurService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import johane.annotation.Auth;
import johane.annotation.MonController;
import johane.annotation.UrlMapping;
import johane.util.ModAndView;
import johane.util.MySession;

@MonController
public class HomeController {

    /** Page d'accueil : charge l'utilisateur connecté à chaque visite (médecin ou patient). */
    @Auth
    @UrlMapping("/home")
    public ModAndView home(ApplicationContext ctx, MySession session,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        String email = (String) session.getAttribute(SessionKeys.USER);
        Optional<Utilisateur> utilisateur = ctx.getBean(UtilisateurService.class).charger(email);

        if (utilisateur.isEmpty()) { // compte supprimé pendant que la session était ouverte
            session.destroy();
            WebHelper.redirect(request, response, "/login");
            return null;
        }

        ModAndView mav = new ModAndView("home", null);
        mav.addValue("utilisateur", utilisateur.get());
        return mav;
    }
}
