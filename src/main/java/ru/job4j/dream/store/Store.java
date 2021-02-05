package ru.job4j.dream.store;

import ru.job4j.dream.model.Model;

import java.util.Collection;

public interface Store {
    Collection<? extends Model> findAll();

    Model save(Model model);

    boolean delete(int id);

    Model findById(int id);
}