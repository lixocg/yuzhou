package com.yuzhou.netty.demo.f06.thrift.impl;


import com.yuzhou.netty.demo.f06.thrift.Person;
import com.yuzhou.netty.demo.f06.thrift.PersonService;

public class PersonServiceImpl implements PersonService.Iface {
    @Override
    public Person getPersonByUsername(String username) {
        System.out.println("got param username " + username);

        Person person = new Person();
        person.setAge(20);
        person.setMarried(false);
        person.setUsername(username);

        return person;
    }

    @Override
    public void savePerson(Person person) {
        System.out.println("got person :");

        System.out.println(person.getUsername());
        System.out.println(person.getAge());
        System.out.println(person.married);
    }
}
