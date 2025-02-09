package ru.yandex.practicum.catsgram.model;

public class Practicum {
    public static void main(String[] args) {

        final Point point1 = Point.builder().x(11).y(22).build();
        System.out.println(point1);

        // используем метод toBuilder на уже созданном объекте
        // point1, чтобы на его основе создать новый объект
        // с другим значением в поле y
        final Point point2 = point1.toBuilder().y(3).build();
        System.out.println(point2);
    }
}