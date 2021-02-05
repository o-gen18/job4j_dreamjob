package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Model;
import ru.job4j.dream.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PsqlCandidateStore implements Store {
    private static final Logger LOG = LoggerFactory.getLogger(PsqlPostStore.class.getName());

    private static final PsqlCandidateStore INST = new PsqlCandidateStore();

    private final PsqlConnection pool = PsqlConnection.instOf();

    public static PsqlCandidateStore instOf() {
        return INST;
    }

    @Override
    public Collection<? extends Model> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(
                                    it.getInt("id"),
                                    it.getString("name"),
                                    it.getString("photoId")));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred in working with database", e);
        }
        return candidates;
    }

    private Candidate create(Model model) {
        Candidate candidate = (Candidate) model;
        String photoId = candidate.getPhotoId();
        if (!(photoId == null)) {
            try (Connection cn = pool.getConnection();
                 PreparedStatement ps =  cn.prepareStatement(
                         "INSERT INTO candidate(name, photoId) VALUES (?, ?)",
                         PreparedStatement.RETURN_GENERATED_KEYS)
            ) {
                ps.setString(1, candidate.getName());
                ps.setString(2, photoId);
                ps.execute();
                try (ResultSet id = ps.getGeneratedKeys()) {
                    if (id.next()) {
                        candidate.setId(id.getInt(1));
                    }
                }
            } catch (Exception e) {
                LOG.error("Exception occurred in working with database", e);
            }
        } else {
            try (Connection cn = pool.getConnection();
                 PreparedStatement ps =  cn.prepareStatement(
                         "INSERT INTO candidate(name) VALUES (?)",
                         PreparedStatement.RETURN_GENERATED_KEYS)
            ) {
                ps.setString(1, candidate.getName());
                ps.execute();
                try (ResultSet id = ps.getGeneratedKeys()) {
                    if (id.next()) {
                        candidate.setId(id.getInt(1));
                    }
                }
            } catch (Exception e) {
                LOG.error("Exception occurred in working with database", e);
            }
        }
        return candidate;
    }

    private Candidate update(Model model) {
        Candidate candidate = (Candidate) model;
        String photoId = candidate.getPhotoId();
        if (!(photoId == null)) {
            try (Connection cn = pool.getConnection();
                 PreparedStatement ps =  cn.prepareStatement(
                         "UPDATE candidate SET (name, photoId)=(?, ?) WHERE id=(?)")
            ) {
                ps.setString(1, candidate.getName());
                ps.setString(2, photoId);
                ps.setInt(3, candidate.getId());
                ps.execute();
            } catch (Exception e) {
                LOG.error("Exception occurred in working with database", e);
            }
        } else {
            try (Connection cn = pool.getConnection();
                 PreparedStatement ps =  cn.prepareStatement(
                         "UPDATE candidate SET (name)=(?) WHERE id=(?)")
            ) {
                ps.setString(1, candidate.getName());
                ps.setInt(2, candidate.getId());
                ps.execute();
            } catch (Exception e) {
                LOG.error("Exception occurred in working with database", e);
            }
        }
        return candidate;
    }

    @Override
    public Model save(Model model) {
        if (model.getId() == 0) {
            return create(model);
        } else {
            return update(model);
        }
    }

    @Override
    public boolean delete(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "DELETE FROM candidate WHERE id=(?)")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            LOG.error("Exception occurred in working with database", e);
            return false;
        }
    }

    @Override
    public Model findById(int id) {
        Candidate candidate = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM candidate WHERE id=(?)")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    candidate = new Candidate(id,
                            rs.getString("name"),
                            rs.getString("photoId"));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred in working with database", e);
        }
        return candidate;
    }

    public String savePhoto(String nameOfPhoto) {
        String photoId = "";
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO photo(name) VALUES (?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, nameOfPhoto);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    photoId = String.valueOf(id.getInt(1)) + "_" + nameOfPhoto;
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred in working with database", e);
        }
        return photoId;
    }

    public boolean deletePhoto(String photoId) {
        int photoIdInt = Integer.parseInt(photoId.substring(0, photoId.indexOf("_")));
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "DELETE FROM photo WHERE id=(?)")) {
            ps.setInt(1, photoIdInt);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            LOG.error("Exception occurred in working with database", e);
            return false;
        }
    }
}