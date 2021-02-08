package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Model;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemCandidateStore implements Store {
    private static final Store INST = new MemCandidateStore();

    private static AtomicInteger candidateId = new AtomicInteger(4);

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private MemCandidateStore() {
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
    }

    public static Store instOf() {
        return INST;
    }

    @Override
    public Collection<? extends Model> findAll() {
        return candidates.values();
    }

    @Override
    public Model save(Model model) {
        if (model.getId() == 0) {
            model.setId(candidateId.incrementAndGet());
        }
        return candidates.put(model.getId(), (Candidate) model);
    }

    @Override
    public boolean delete(int id) {
        return candidates.remove(id) != null;
    }

    @Override
    public Model findById(int id) {
        return candidates.get(id);
    }
}
