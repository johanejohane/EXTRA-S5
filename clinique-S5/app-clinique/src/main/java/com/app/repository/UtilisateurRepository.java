package com.app.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.app.model.Utilisateur;

@Repository
public class UtilisateurRepository {

    private static final String SELECT =
            "SELECT u.id, u.nom, u.email, u.mot_de_passe, r.nom AS role "
                    + "FROM utilisateur u JOIN role r ON r.id = u.role_id ";

    private static final RowMapper<Utilisateur> MAPPER = (rs, i) -> {
        Utilisateur u = new Utilisateur();
        u.setId(rs.getInt("id"));
        u.setNom(rs.getString("nom"));
        u.setEmail(rs.getString("email"));
        u.setMotDePasse(rs.getString("mot_de_passe"));
        u.setRole(rs.getString("role"));
        return u;
    };

    private final JdbcTemplate jdbc;

    public UtilisateurRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Utilisateur> findByEmail(String email) {
        return jdbc.query(SELECT + "WHERE u.email = ?", MAPPER, email).stream().findFirst();
    }

    /** exceptId : on ignore cet utilisateur (cas de la modification), null à la création. */
    public boolean emailExiste(String email, Integer exceptId) {
        Integer n;
        if (exceptId == null) {
            n = jdbc.queryForObject("SELECT COUNT(*) FROM utilisateur WHERE email = ?", Integer.class, email);
        } else {
            n = jdbc.queryForObject("SELECT COUNT(*) FROM utilisateur WHERE email = ? AND id <> ?",
                    Integer.class, email, exceptId);
        }
        return n != null && n > 0;
    }

    /** Insère l'utilisateur avec le rôle donné (par son nom) et renvoie son id. */
    public int insert(Utilisateur u, String roleNom) {
        String sql = "INSERT INTO utilisateur (nom, email, mot_de_passe, role_id) "
                + "VALUES (?, ?, ?, (SELECT id FROM role WHERE nom = ?))";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, u.getNom());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getMotDePasse());
            ps.setString(4, roleNom);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    /** Si motDePasse est null, l'ancien hash est conservé. */
    public void update(Utilisateur u) {
        if (u.getMotDePasse() == null) {
            jdbc.update("UPDATE utilisateur SET nom = ?, email = ? WHERE id = ?",
                    u.getNom(), u.getEmail(), u.getId());
        } else {
            jdbc.update("UPDATE utilisateur SET nom = ?, email = ?, mot_de_passe = ? WHERE id = ?",
                    u.getNom(), u.getEmail(), u.getMotDePasse(), u.getId());
        }
    }
}
