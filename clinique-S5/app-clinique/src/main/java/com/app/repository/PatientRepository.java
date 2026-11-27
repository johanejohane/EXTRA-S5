package com.app.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.app.model.Patient;

@Repository
public class PatientRepository {

    private static final String SELECT =
            "SELECT p.id, p.utilisateur_id, u.nom, u.email, p.date_creation "
                    + "FROM patient p JOIN utilisateur u ON u.id = p.utilisateur_id ";

    private static final RowMapper<Patient> MAPPER = (rs, i) -> {
        Patient p = new Patient();
        p.setId(rs.getInt("id"));
        p.setUtilisateurId(rs.getInt("utilisateur_id"));
        p.setNom(rs.getString("nom"));
        p.setEmail(rs.getString("email"));
        p.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        return p;
    };

    private final JdbcTemplate jdbc;

    public PatientRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Patient> findAll() {
        return jdbc.query(SELECT + "ORDER BY u.nom", MAPPER);
    }

    public Optional<Patient> findById(int id) {
        return jdbc.query(SELECT + "WHERE p.id = ?", MAPPER, id).stream().findFirst();
    }

    public int insert(int utilisateurId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO patient (utilisateur_id) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, utilisateurId);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    /** Supprime l'utilisateur lié : la ligne patient part avec (ON DELETE CASCADE). */
    public boolean deleteById(int id) {
        int n = jdbc.update(
                "DELETE FROM utilisateur WHERE id = (SELECT utilisateur_id FROM patient WHERE id = ?)", id);
        return n > 0;
    }
}
