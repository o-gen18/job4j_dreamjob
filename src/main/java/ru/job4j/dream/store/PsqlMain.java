package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Model;
import ru.job4j.dream.model.Post;

public class PsqlMain {
    public static void main(String[] args) {
        Store postStore = PsqlPostStore.instOf();
        Store candidateStore = PsqlCandidateStore.instOf();
        postStore.save(new Post(0, "First Vacancy"));
        postStore.save(new Post(0, "Second Vacancy"));
        postStore.save(new Post(0, "Third Vacancy"));
        for (Model post : postStore.findAll()) {
            System.out.println(post.getId() + " " + post.getName());
        }
        postStore.save(new Post(1, "First Vacancy Updated"));
        postStore.save(new Post(2, "Second Vacancy Updated"));
        postStore.save(new Post(3, "Third Vacancy Updated"));
        for (Model post : postStore.findAll()) {
            System.out.println(post.getId() + " " + post.getName());
        }
        System.out.println(postStore.findById(1).getName());
        System.out.println(postStore.findById(2).getName());
        System.out.println(postStore.findById(3).getName());
        System.out.println();

        candidateStore.save(new Candidate(0, "First Candidate"));
        candidateStore.save(new Candidate(0, "Second Candidate"));
        candidateStore.save(new Candidate(0, "Third Candidate"));
        for (Model candidate : candidateStore.findAll()) {
            System.out.println(candidate.getId() + " " + candidate.getName());
        }
        candidateStore.save(new Candidate(1, "First Candidate Updated"));
        candidateStore.save(new Candidate(2, "Second Candidate Updated"));
        candidateStore.save(new Candidate(3, "Third Candidate Updated"));
        for (Model candidate : candidateStore.findAll()) {
            System.out.println(candidate.getId() + " " + candidate.getName());
        }
        System.out.println(candidateStore.findById(1).getName());
        System.out.println(candidateStore.findById(2).getName());
        System.out.println(candidateStore.findById(3).getName());
    }
}
