package ru.job4j.dream.store;

import ru.job4j.dream.model.Model;

import java.util.Collection;

public interface Store {
    Collection<? extends Model> findAll();

    void save(Model model);

    Model findById(int id);
}
