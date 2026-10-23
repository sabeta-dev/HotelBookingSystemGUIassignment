/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.hotelbookings.view;

import com.hotel.hotelbookings.controller.HotelController;
import com.hotel.hotelbookings.dao.DatabaseManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author sashasabeta
 */
public class MainFrame extends JFrame {

    private final HotelController controller;

    public MainFrame(HotelController controller) {
        super("Hotel Booking System");
        this.controller = controller;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override

            public void windowClosing(WindowEvent e) {
                DatabaseManager.getInstance().closeConnection();
            }
        });

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int frameHeight = screenHeight / 2;
        int frameWidth = screenWidth / 2;
        setSize(frameWidth, frameHeight);
        

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Hotel Booking System", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(20f));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel menu = new JPanel(new GridLayout(3, 1, 10, 10));
        menu.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));

        JButton roomsButton = new JButton("Rooms");
        roomsButton.addActionListener(e -> openWindow("Rooms", new RoomsPanel(controller)));
        menu.add(roomsButton);

        JButton guestsButton = new JButton("Guests");
        menu.add(guestsButton);

        JButton bookingsButton = new JButton("Bookings");
        menu.add(bookingsButton);

        add(menu, BorderLayout.CENTER);
        menu.setBackground(Color.LIGHT_GRAY);
    }

    private void openWindow(String title, JPanel panel) {
        JFrame window = new JFrame(title);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int frameHeight = screenHeight / 2;
        int frameWidth = screenWidth / 2;
        window.add(panel);
        window.setSize(frameWidth, frameHeight);
        window.setLocationRelativeTo(this);
        window.setVisible(true);
    }
}
