package ru.job4j.dream.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

public class PsqlCandidateStore implements Store {
    private static final Logger LOG = LoggerFactory.getLogger(PsqlPostStore.class.getName());

    private static final Store INST = new PsqlCandidateStore();

    private final PsqlConnection pool = PsqlConnection.instOf();

    public static Store instOf() {
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
                                    it.getString("photoId"),
                                    it.getString("cityId")));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred in working with database", e);
        }
        return candidates;
    }

    private Candidate create(Candidate candidate) {
        String photoId = candidate.getPhotoId();
        if (!(photoId == null)) {
            try (Connection cn = pool.getConnection();
                 PreparedStatement ps =  cn.prepareStatement(
                         "INSERT INTO candidate(name, photoId, cityId) VALUES (?, ?, ?)",
                         PreparedStatement.RETURN_GENERATED_KEYS)
            ) {
                ps.setString(1, candidate.getName());
                ps.setString(2, photoId);
                ps.setString(3, candidate.getCityId());
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
                         "INSERT INTO candidate(name, cityId) VALUES (?, ?)",
                         PreparedStatement.RETURN_GENERATED_KEYS)
            ) {
                ps.setString(1, candidate.getName());
                ps.setString(2, candidate.getCityId());
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

    private Candidate update(Candidate candidate) {
        String photoId = candidate.getPhotoId();
        if (!(photoId == null)) {
            try (Connection cn = pool.getConnection();
                 PreparedStatement ps =  cn.prepareStatement(
                         "UPDATE candidate SET (name, photoId, cityId)=(?, ?, ?) WHERE id=(?)")
            ) {
                ps.setString(1, candidate.getName());
                ps.setString(2, photoId);
                ps.setString(3, candidate.getCityId());
                ps.setInt(4, candidate.getId());
                ps.execute();
            } catch (Exception e) {
                LOG.error("Exception occurred in working with database", e);
            }
        } else {
            try (Connection cn = pool.getConnection();
                 PreparedStatement ps =  cn.prepareStatement(
                         "UPDATE candidate SET (name, cityId)=(?, ?) WHERE id=(?)")
            ) {
                ps.setString(1, candidate.getName());
                ps.setString(2, candidate.getCityId());
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
        Candidate candidate = (Candidate) model;
        saveCity(candidate.getCityId());
        if (model.getId() == 0) {
            return create(candidate);
        } else {
            return update(candidate);
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
                            rs.getString("photoId"),
                            rs.getString("cityId"));
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
        if (photoId.equals("null")) {
            return false;
        }
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

    public boolean saveCity(String cityId) {
        try (Connection cn = pool.getConnection();
           PreparedStatement ps = cn.prepareStatement(
                "INSERT INTO city(name) values(?)")) {
            ps.setString(1, cityId);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            LOG.error("Exception occurred in working with database", e);
            return false;
        }
    }

    public String getCities() {
        StringJoiner cities = new StringJoiner(",", "[", "]");
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT name FROM city"
             )) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cities.add("\"" + rs.getString(1) + "\"");
            }
            return cities.toString();
        } catch (Exception e) {
            LOG.error("Exception occurred in working with database", e);
            return cities.toString();
        }
    }

//    public static void main(String[] args) {
//        System.out.println(new PsqlCandidateStore().getCities());
//    }
}