#!/usr/bin/env bash
# ============================================================
# Déploiement indépendant : application Clinique S5 -> Tomcat 10.1
# Base MySQL conservée : clinique
# Usage depuis le dossier clinique-S5 : ./deploy.sh [--init-db]
# ============================================================
set -Eeuo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
APP_NAME="clinique-s5"
WAR_NAME="${APP_NAME}.war"
TOMCAT_HOME="${TOMCAT_HOME:-/home/johane/tomcat}"
TOMCAT_WEBAPPS="$TOMCAT_HOME/webapps"
PROJECT_DIR="$SCRIPT_DIR/app-clinique"
FRAMEWORK_DIR="$SCRIPT_DIR/mini-framework-johane"
INIT_DB=false

for arg in "$@"; do
  case "$arg" in
    --init-db) INIT_DB=true ;;
    *) echo "Option inconnue : $arg" >&2; exit 1 ;;
  esac
done

GREEN='\033[0;32m'; YELLOW='\033[1;33m'; RED='\033[0;31m'; NC='\033[0m'
ok(){ echo -e "${GREEN}✔ $*${NC}"; }
warn(){ echo -e "${YELLOW}⚠ $*${NC}"; }
fail(){ echo -e "${RED}✘ $*${NC}" >&2; exit 1; }

WAR_FILE="$PROJECT_DIR/target/$WAR_NAME"
DEST_WAR="$TOMCAT_WEBAPPS/$WAR_NAME"
EXPLODED_APP="$TOMCAT_WEBAPPS/$APP_NAME"
WORK_CACHE="$TOMCAT_HOME/work/Catalina/localhost/$APP_NAME"

# Vérifications
[[ -f "$PROJECT_DIR/pom.xml" ]] || fail "pom.xml introuvable : $PROJECT_DIR"
[[ -f "$FRAMEWORK_DIR/pom.xml" ]] || fail "pom.xml du framework introuvable : $FRAMEWORK_DIR"
[[ -d "$TOMCAT_WEBAPPS" ]] || fail "Dossier webapps introuvable : $TOMCAT_WEBAPPS"
command -v mvn >/dev/null 2>&1 || fail "Maven est introuvable"

# Tomcat 10.1 doit être lancé et utiliser le port configuré (8080 par défaut).
echo "Application : $APP_NAME"
echo "Base MySQL  : clinique (nom conservé)"
echo "Tomcat      : $TOMCAT_HOME"
echo

# Installer le framework partagé
mvn -f "$FRAMEWORK_DIR/pom.xml" clean install -DskipTests

# Construire le WAR distinct
mvn -f "$PROJECT_DIR/pom.xml" clean package -DskipTests
[[ -f "$WAR_FILE" ]] || fail "WAR non généré : $WAR_FILE"

# Initialisation optionnelle de la base EXISTANTE 'clinique'
if [[ "$INIT_DB" == true ]]; then
  SCHEMA="$PROJECT_DIR/sql/schema.sql"
  [[ -f "$SCHEMA" ]] || fail "Fichier SQL introuvable : $SCHEMA"
  if command -v mysql >/dev/null 2>&1; then MYSQL_BIN="$(command -v mysql)"
  elif [[ -x /opt/lampp/bin/mysql ]]; then MYSQL_BIN="/opt/lampp/bin/mysql"
  else fail "Client mysql introuvable (mysql ou /opt/lampp/bin/mysql)"; fi
  DB_USER="${DB_USER:-root}"
  if [[ -n "${DB_PASS:-}" ]]; then
    "$MYSQL_BIN" -u "$DB_USER" -p"$DB_PASS" < "$SCHEMA"
  else
    "$MYSQL_BIN" -u "$DB_USER" -p < "$SCHEMA"
  fi
  ok "Script SQL exécuté sur la base clinique"
fi

# Remplacer uniquement l'application clinique-s5 ; ne toucher à aucun autre WAR.
rm -f -- "$DEST_WAR"
[[ ! -d "$EXPLODED_APP" ]] || rm -rf -- "$EXPLODED_APP"
[[ ! -d "$WORK_CACHE" ]] || rm -rf -- "$WORK_CACHE"
install -m 0644 "$WAR_FILE" "$DEST_WAR"
ok "WAR déployé : $DEST_WAR"

echo
echo "URL : http://localhost:8080/$APP_NAME/"
echo "Base utilisée : clinique"
echo "Seule l'application $APP_NAME a été remplacée."
