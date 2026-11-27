package com.app.helper;

/** Clés de session et noms de rôles. Les clés DOIVENT être identiques à celles du web.xml. */
public final class SessionKeys {
    public static final String USER = "userEmail";  // auth.user-session-key
    public static final String ROLE = "userRole";   // auth.role-session-key

    public static final String ROLE_MEDECIN = "medecin";
    public static final String ROLE_PATIENT = "patient";

    private SessionKeys() {
    }
}
