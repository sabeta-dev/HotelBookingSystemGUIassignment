/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hotel.hotelbookings.view;

import com.hotel.hotelbookings.controller.HotelController;
import com.hotel.hotelbookings.exception.BookingException;
import com.hotel.hotelbookings.exception.ValidationException;
import com.hotel.hotelbookings.model.Booking;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sashasabeta
 */
public class BookingPanel extends JPanel {

    private final HotelController controller;
    private DefaultTableModel tableModel;

    public BookingPanel(HotelController controller) {

        this.controller = controller;

        setLayout(new BorderLayout());
        String[] coloumns = {"Booking ID", "Check-in", "Check-out", "Status", "Guest ID", "Room"};
        tableModel = new DefaultTableModel(coloumns, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollpane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton returnButton = new JButton("return to menu");
        returnButton.addActionListener(e -> SwingUtilities.getWindowAncestor(this).dispose());
        buttonPanel.add(returnButton);

        JButton deleteButton = new JButton("Delete Booking");
        deleteButton.addActionListener(e -> {

            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select booking first.");
                return;
            }
            try {
                String bookingId = (String) table.getValueAt(selectedRow, 0);
                controller.cancelBooking(bookingId);
                refresh();

            } catch (ValidationException | BookingException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Cannot cancel", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(deleteButton);

        JButton addButton = new JButton("Add Booking");

        addButton.addActionListener(e
                -> {
            JTextField guestField = new JTextField(15);
            JTextField roomField = new JTextField(15);
            JTextField checkInField = new JTextField("yyyy-MM-dd ", 15);
            JTextField checkOutField = new JTextField("yyyy-MM-dd", 15);
            JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
            form.add(new JLabel("Guest ID:"));
            form.add(guestField);
            form.add(new JLabel("Room number:"));
            form.add(roomField);
            form.add(new JLabel("Check-in:"));
            form.add(checkInField);
            form.add(new JLabel("Check-out:"));
            form.add(checkOutField);

            int result = JOptionPane.showConfirmDialog(this, form, "New booking", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int roomNumber = Integer.parseInt(roomField.getText().trim());
                    controller.createBooking(guestField.getText().trim(), roomNumber,
                            checkInField.getText().trim(), checkOutField.getText().trim());
                    refresh();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Room number must be a number.", "Invalid input", JOptionPane.ERROR_MESSAGE);
                } catch (ValidationException | BookingException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Cannot create booking", JOptionPane.ERROR_MESSAGE);
                }
            }

        }
        );
        buttonPanel.add(addButton);

        add(scrollpane, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.SOUTH);
        JButton updateButton = new JButton("Update booking");
        updateButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select booking first.");
                return;
            }
            String bookingId = tableModel.getValueAt(selectedRow, 0).toString();
            String oldCheckIn = tableModel.getValueAt(selectedRow, 1).toString();
            String oldCheckOut = tableModel.getValueAt(selectedRow, 2).toString();
            String oldStatus = tableModel.getValueAt(selectedRow, 3).toString();

            JTextField checkinField = new JTextField(oldCheckIn, 15);
            JTextField checkoutField = new JTextField(oldCheckOut, 15);
            JComboBox<String> statusBox = new JComboBox<>(new String[]{"Confirmed", "Checked-in", "Checked-out"});
            statusBox.setSelectedItem(oldStatus);

            JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
            form.add(new JLabel("Check-in:"));
            form.add(checkinField);
            form.add(new JLabel("Check-out:"));
            form.add(checkoutField);
            form.add(new JLabel("Status:"));
            form.add(statusBox);

            int result = JOptionPane.showConfirmDialog(this, form, "Update booking " + bookingId, JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String newCheckIn = checkinField.getText().trim();
                    String newCheckOut = checkoutField.getText().trim();
                    String newStatus = (String) statusBox.getSelectedItem();
                    if (!newCheckIn.equals(oldCheckIn) || !newCheckOut.equals(oldCheckOut)) {
                        controller.updateBooking(bookingId, newCheckIn, newCheckOut);
                    }
                    if (!newStatus.equals(oldStatus)) {
                        controller.updateBookingStatus(bookingId, newStatus);
                    }
                    refresh();
                } catch (ValidationException | BookingException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Cannot update booking", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
                buttonPanel.add(updateButton);

        refresh();
    }

    

    public void refresh() {
        tableModel.setRowCount(0);
        for (Booking booking : controller.getAllBookings()) {
            tableModel.addRow(new Object[]{booking.getBookingId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getStatus(),
                booking.getGuest().getId(),
                booking.getRoom().getRoomNumber()
            });

        }

    }
}

