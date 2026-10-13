/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hotel.hotelbookings.dao;

import com.hotel.hotelbookings.model.Guest;
import java.util.List;

/**
 *
 * @author sashasabeta
 */
public interface GuestDAO {

    List<Guest> findAll();

    Guest findById(String guestId);

    void add(Guest guest);

    void update(Guest guest);

    void delete(String guestId);

}
