package com.app.helper;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public final class WebHelper {

    private WebHelper() {
    }

    /**
     * Redirection HTTP (le ModAndView du framework fait un forward, pas un redirect).
     * Le controller doit ensuite retourner null (ou être void) pour que le FrontController s'arrête.
     */
    public static void redirect(HttpServletRequest request, HttpServletResponse response, String path)
            throws IOException {
        response.sendRedirect(request.getContextPath() + path);
    }
}
