package ru.job4j.dream.store;

import ru.job4j.dream.model.Model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PsqlPostStoreStub implements Store {
    private final Map<Integer, Model> store = new HashMap<>();
    private int id = 0;

    @Override
    public Collection<? extends Model> findAll() {
        return store.values();
    }

    @Override
    public Model save(Model model) {
        model.setId(id++);
        this.store.put(model.getId(), model);
        return model;
    }

    @Override
    public boolean delete(int id) {
        return store.remove(id) != null;
    }

    @Override
    public Model findById(int id) {
        return store.get(id);
    }
}
