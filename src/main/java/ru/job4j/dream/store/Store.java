package ru.job4j.dream.store;

import ru.job4j.dream.model.Post;

import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private static final Store INST = new Store();

    private Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private Store() {
        posts.put(1, new Post(1, "Junior Java Job",
                "Simple job for beginners in java language.",
                Calendar.getInstance().getTime().toString()));

        posts.put(2, new Post(2, "Middle Java Job",
                "Work for experienced java developers.",
                Calendar.getInstance().getTime().toString()));

        posts.put(3, new Post(3, "Senior Java Job",
                "Projects for advanced java programmers",
                Calendar.getInstance().getTime().toString()));
    }

    public static Store instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }
}
