/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hotel.hotelbookings.dao;
import java.util.List;
import com.hotel.hotelbookings.model.Room; 
       
/**
 *
 * @author sashasabeta
 */
public interface RoomDAO {
    List<Room> findAll();
    List<Room> findAvailable();
    Room findByRoomNumber(int roomNumber);
    void add(Room room);
    void update(Room room);
    void delete(int roomNumber);
    
   
    
}
