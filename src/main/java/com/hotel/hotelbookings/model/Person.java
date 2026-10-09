/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.hotelbookings.model;

/**
 *
 * @author sashasabeta
 */
public abstract class Person {
    private String name;
    private String id;
    private String number;
    private String email;

    public Person(String name, String id, String number, String email) {
        this.name = name;
        this.id = id;
        this.number = number;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }
   
    public abstract String getRole();
    
    
}