package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.Model;
import ru.job4j.dream.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PsqlPostStore implements Store {
    private static final Logger LOG = LoggerFactory.getLogger(PsqlPostStore.class.getName());

    private static final PsqlPostStore INST = new PsqlPostStore();

    private final PsqlConnection pool = PsqlConnection.instOf();

    public static PsqlPostStore instOf() {
        return INST;
    }

    @Override
    public Collection<? extends Model> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred in working with database", e);
        }
        return posts;
    }

    private Post create(Model model) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO post(name) VALUES (?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, model.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    model.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred in working with database", e);
        }
        return (Post) model;
    }

    private void update(Model model) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "UPDATE post SET name=(?) WHERE id=(?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, model.getName());
            ps.setInt(2, model.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error("Exception occurred in working with database", e);
        }
    }

    @Override
    public void save(Model model) {
        if (model.getId() == 0) {
            create(model);
        } else {
            update(model);
        }
    }

    @Override
    public Model findById(int id) {
        Post post = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM post WHERE id=(?)")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    post = new Post(id, rs.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred in working with database", e);
        }
        return post;
    }
}