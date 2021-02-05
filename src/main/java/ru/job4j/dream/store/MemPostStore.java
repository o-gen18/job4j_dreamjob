package ru.job4j.dream.store;

import ru.job4j.dream.model.Model;
import ru.job4j.dream.model.Post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemPostStore implements Store {
    private static final MemPostStore INST = new MemPostStore();

    private static AtomicInteger postId = new AtomicInteger(4);

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private MemPostStore() {
        posts.put(1, new Post(1, "Junior Java Job"));
        posts.put(2, new Post(2, "Middle Java Job"));
        posts.put(3, new Post(3, "Senior Java Job"));
    }

    public static MemPostStore instOf() {
        return INST;
    }

    @Override
    public Collection<? extends Model> findAll() {
        return posts.values();
    }

    @Override
    public Model save(Model model) {
        if (model.getId() == 0) {
            model.setId(postId.incrementAndGet());
        }
        return posts.put(model.getId(), (Post) model);
    }

    @Override
    public boolean delete(int id) {
        return posts.remove(id) != null;
    }

    @Override
    public Model findById(int id) {
        return posts.get(id);
    }
}
