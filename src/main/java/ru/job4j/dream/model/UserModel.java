package ru.job4j.dream.model;

public interface UserModel extends Model {
    String getEmail();

    void setEmail(String email);

    String getPassword();

    void setPassword(String password);
}
