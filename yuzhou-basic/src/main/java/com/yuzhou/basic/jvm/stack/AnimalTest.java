package com.yuzhou.basic.jvm.stack;

class Animal {
    public void eat() {
        System.out.println("eating....");
    }
}

interface Huntable {
    void hunt();
}

class Dog extends Animal implements Huntable {
    @Override
    public void eat() {
        System.out.println("dog eating");
    }

    @Override
    public void hunt() {
        System.out.println("not ok hunt....");
    }
}

class Cat extends Animal implements Huntable {
    @Override
    public void eat() {
        System.out.println("cat eating....");
    }

    @Override
    public void hunt() {
        System.out.println("cat ok hunt....");
    }
}

public class AnimalTest {
    public void eat(Animal a) {
        a.eat();
    }

    public void hunt(Huntable h) {
        h.hunt();
    }

    public static void main(String[] args) {

        AnimalTest a = new AnimalTest();
        a.eat(new Cat());
    }
}
