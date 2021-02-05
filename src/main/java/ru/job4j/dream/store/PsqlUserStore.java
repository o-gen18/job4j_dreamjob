package ru.job4j.dream.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.Model;
import ru.job4j.dream.model.UserModel;
import ru.job4j.dream.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PsqlUserStore implements UserStore {
    private static final Logger LOG = LoggerFactory.getLogger(PsqlUserStore.class.getName());

    private static final PsqlUserStore INST = new PsqlUserStore();

    private final PsqlConnection pool = PsqlConnection.instOf();

    public static PsqlUserStore instOf() {
        return INST;
    }

    @Override
    public UserModel findByEmail(String email) {
        User user = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM users WHERE email=(?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getInt(1),
                            rs.getString("name"),
                            email,
                            rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred in working with database", e);
        }
        return user;
    }

    @Override
    public Collection<? extends Model> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM users ")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password")));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occurred in working with database", e);
        }
        return users;
    }

    @Override
    public boolean save(Model model) {
        User user = (User) model;
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO users (name, email, password) values (?, ?, ?)")) {
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getPassword());
                ps.execute();
                return true;
        } catch (Exception e) {
            LOG.error("Exception occurred in working with database", e);
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "DELETE FROM users WHERE id=(?)")) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            LOG.error("Exception occurred in working with database", e);
            return false;
        }
    }

    @Override
    public Model findById(int id) {
        User user = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM users WHERE id=(?)")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            id,
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception occurred in working with database", e);
        }
        return user;
    }
}
