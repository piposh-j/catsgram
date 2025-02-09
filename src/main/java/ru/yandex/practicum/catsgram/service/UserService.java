package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.*;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }


    public User create(@RequestBody User user) {
        // проверяем выполнение необходимых условий
        if (user.getEmail() == null || user.getUsername().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }


        if (emailAlreadyExist(user).isPresent()) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        // формируем дополнительные данные
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        // сохраняем новую публикацию в памяти приложения
        users.put(user.getId(), user);
        return user;
    }


    public User update(@RequestBody User newUser) {
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (newUser.getEmail() != null || !newUser.getEmail().isBlank()) {
                oldUser.setEmail(newUser.getEmail());
            }

            if (newUser.getPassword() != null && !newUser.getPassword().isBlank()) {
                oldUser.setPassword(newUser.getPassword());
            }

            if (newUser.getUsername() != null && !newUser.getUsername().isBlank()) {
                oldUser.setUsername(newUser.getUsername());
            }
            Optional<User> result = emailAlreadyExist(newUser);
            if (result.isPresent() && !Objects.equals(result.get().getId(), newUser.getId())) {
                throw new DuplicatedDataException("Этот имейл уже используется");
            }

            return oldUser;
        }
        throw new NotFoundException("Пост с id = " + newUser.getId() + " не найден");
    }

    private Optional<User> emailAlreadyExist(User user) {
        return users.values()
                .stream()
                .filter(it -> it.getEmail().equals(user.getEmail()))
                .findFirst();
    }

        public Optional<User> findById(@PathVariable long userId) {
        return users.values().stream()
                .filter(x -> x.getId() == userId)
                .findFirst();
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
