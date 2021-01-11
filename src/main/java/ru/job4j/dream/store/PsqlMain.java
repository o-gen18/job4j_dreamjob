package ru.job4j.dream.store;

import ru.job4j.dream.model.Post;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.save(new Post(0, "First"));
        store.save(new Post(0, "Second"));
        store.save(new Post(0, "Third"));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }
        store.save(new Post(1, "First Updated"));
        store.save(new Post(2, "Second Updated"));
        store.save(new Post(3, "Third Updated"));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }
        System.out.println(store.findPostById(1).getName());
        System.out.println(store.findPostById(2).getName());
        System.out.println(store.findPostById(3).getName());
    }
}
