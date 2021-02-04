package ru.job4j.dream.store;

import ru.job4j.dream.model.UserModel;

public interface UserStore extends Store {
    UserModel findByEmail(String email);
}
