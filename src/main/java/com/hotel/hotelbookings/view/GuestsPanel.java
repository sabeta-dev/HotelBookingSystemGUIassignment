/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.hotelbookings.view;

import com.hotel.hotelbookings.controller.HotelController;
import com.hotel.hotelbookings.model.Guest;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sashasabeta
 */
public class GuestsPanel extends JPanel {

    private final HotelController controller;
    private DefaultTableModel tableModel;

    public GuestsPanel(HotelController controller) {

        this.controller = controller;

        setLayout(new BorderLayout());
        String[] coloumns = {"Guest ID", "Name", "Email", "Phone", "Loyalty points"};
        tableModel = new DefaultTableModel(coloumns, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollpane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton returnButton = new JButton("return to menu");
        returnButton.addActionListener(e -> SwingUtilities.getWindowAncestor(this).dispose());
        buttonPanel.add(returnButton);

        add(scrollpane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        refresh();
    }

    public void refresh() {
        tableModel.setRowCount(0);
        for (Guest guest : controller.getAllGuests()) {
            tableModel.addRow(new Object[]{guest.getId(), guest.getName(), guest.getEmail(), guest.getNumber(), guest.getLoyaltyPoints()});
        }

    }

}
