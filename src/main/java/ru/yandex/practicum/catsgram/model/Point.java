package ru.yandex.practicum.catsgram.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
class Point {
    int x;
    int y;
}
