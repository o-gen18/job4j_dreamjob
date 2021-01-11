package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.util.Collection;

public interface Store {
    Collection<Post> findAllPosts();
    Collection<Candidate> findAllCandidates();
    void save(Post post);
    Post findPostById(int id);
}
