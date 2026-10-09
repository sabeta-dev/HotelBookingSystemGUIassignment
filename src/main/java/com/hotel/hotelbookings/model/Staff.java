/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.hotelbookings.model;

/**
 *
 * @author sashasabeta
 */
public class Staff extends Person {

    String role;
    public Staff(String name, String id, String number, String email, String role) {
        super(name, id, number, email);
        this.role = role;
    }
    @Override
    public String getRole()
    {
        return role;
    }
    
    
    public String contactInformation()
    {
        return getName()+": "+getNumber()+", "+getEmail()+ ", Role:"+getRole();
    }
}
