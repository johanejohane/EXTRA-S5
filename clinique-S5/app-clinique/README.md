# app-clinique — Sprint 1 (médecins / patients)

Application web construite avec **mini-framework-johane** (@MonController, @UrlMapping, @Auth, ModAndView, MySession, Binder).

## Lancer

1. Installer le framework dans le dépôt Maven local :
   `cd mini-framework-johane && mvn clean install`
2. Base MySQL `clinique` (nom conservé) :
   `mysql -u root -p < sql/schema.sql`
   (identifiants à adapter dans `src/main/resources/db.properties`)
3. Construire le WAR : `mvn clean package` → `target/clinique-s5.war`
4. Déployer sur **Tomcat 10.1** (Java 17+) puis ouvrir `http://localhost:8080/clinique-s5/`

## Comptes de test

| Rôle    | Email                 | Mot de passe |
|---------|-----------------------|--------------|
| Médecin | medecin@clinique.mg   | medecin123   |
| Patient | patient@clinique.mg   | patient123   |

## Routes

| Méthode | URL                  | Rôle     | Rôle de la route                    |
|---------|----------------------|----------|-------------------------------------|
| GET     | `/`                  | —        | redirige vers `/login` ou `/home`   |
| GET/POST| `/login`             | —        | formulaire / authentification       |
| POST    | `/logout`            | connecté | vide la session                     |
| GET     | `/home`              | connecté | informations de l'utilisateur       |
| GET     | `/patients`          | médecin  | liste                               |
| GET     | `/patients/nouveau`  | médecin  | formulaire de création              |
| POST    | `/patients/creer`    | médecin  | Create                              |
| GET     | `/patients/detail?id=` | médecin | Read                               |
| GET/POST| `/patients/modifier` | médecin  | formulaire / Update                 |
| POST    | `/patients/supprimer`| médecin  | Delete                              |


## Déploiement indépendant sur Tomcat 10.1

Depuis le dossier `clinique-S5` :

```bash
chmod +x deploy.sh
./deploy.sh
```

Pour exécuter le script SQL sur la base existante `clinique` : `./deploy.sh --init-db`.
Le WAR et le contexte sont nommés `clinique-s5` ; le script ne remplace que cette application dans Tomcat.
