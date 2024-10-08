package it.unibo.myvet.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import it.unibo.myvet.model.Appointment;
import it.unibo.myvet.controller.AppointmentListController;
import it.unibo.myvet.model.Animal;
import it.unibo.myvet.model.AppointmentState;
import it.unibo.myvet.model.Breed;
import it.unibo.myvet.model.Service;
import it.unibo.myvet.model.Sex;
import it.unibo.myvet.model.Specialization;
import it.unibo.myvet.model.Species;
import it.unibo.myvet.model.User;
import it.unibo.myvet.model.Vet;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AppointmentListView extends JPanel {

        private JTable appointmentTable;
        private DefaultTableModel tableModel;
        private AppointmentListController appointmentListController;
        private List<Appointment> appointments;

        public AppointmentListView(AppointmentListController appointmentListController) {
                this.appointmentListController = appointmentListController;
                this.appointments = this.appointmentListController.getAppointments();

                this.createView();
        }

        private void createView() {
                setLayout(new BorderLayout());

                // Nomi delle colonne per la tabella
                String[] columnNames = { "Animal Name", "Owner Name", "Vet Name", "Date & Time", "Service", "Status" };

                // Crea il modello della tabella
                tableModel = new DefaultTableModel(columnNames, 0) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                return false; // Rendi tutte le celle non editabili
                        }
                };

                appointmentTable = new JTable(tableModel);

                // Popola la tabella con i dati degli appuntamenti
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                for (Appointment appointment : appointments) {
                        String animalName = appointment.getAnimal().getName();
                        String ownerName = appointment.getAnimal().getOwner().getFirstName() + " "
                                        + appointment.getAnimal().getOwner().getLastName();
                        String vetName = appointment.getVet().getFirstName() + " " + appointment.getVet().getLastName();
                        String statusName = appointment.getStatus().getStateName();

                        Object[] rowData = {
                                        animalName,
                                        ownerName,
                                        vetName,
                                        appointment.getDateTime().format(formatter),
                                        appointment.getService().getName(),
                                        statusName
                        };
                        tableModel.addRow(rowData);
                }

                // Aggiungi la tabella a un pannello di scorrimento
                JScrollPane scrollPane = new JScrollPane(appointmentTable);
                add(scrollPane, BorderLayout.CENTER);

                // Aggiungi il MouseListener per rilevare i clic sulla tabella
                appointmentTable.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                                if (e.getClickCount() == 2) { // Verifica se è stato fatto un doppio clic
                                        int row = appointmentTable.getSelectedRow();
                                        if (row != -1) {
                                                // Ottieni l'appuntamento corrispondente alla riga selezionata
                                                Appointment selectedAppointment = appointments.get(row);

                                                // Apri la finestra di dettagli dell'appuntamento
                                                new AppointmentDetailView(selectedAppointment);
                                        }
                                }
                        }
                });
        }

        // Metodo main temporaneo per testare la visualizzazione
        public static void main(String[] args) {
                // Creazione di dati fittizi per testare la view

                // Creazione di alcuni utenti (proprietari)
                User owner1 = new User("CF001", "password123", "John", "Doe", "123456789", 1);
                User owner2 = new User("CF002", "password456", "Jane", "Smith", "987654321", 2);

                // Creazione di alcune specie
                Species dogSpecies = new Species(1, "Dog");
                Species catSpecies = new Species(2, "Cat");

                // Creazione di alcune razze
                Breed breed1 = new Breed(1, "Labrador", dogSpecies);
                Breed breed2 = new Breed(2, "Siamese", catSpecies);

                // Creazione di alcuni animali
                Animal animal1 = new Animal(1, "Rex", LocalDate.of(2015, 5, 20), Sex.M, owner1, breed1);
                Animal animal2 = new Animal(2, "Whiskers", LocalDate.of(2018, 3, 15), Sex.M, owner2, breed2);

                Specialization specialization = new Specialization("pisellone");

                // Creazione di alcuni veterinari
                Vet vet1 = new Vet("CFV001", "vetpassword1", "Dr.", "Jones", "111222333", 1, specialization);
                Vet vet2 = new Vet("CFV002", "vetpassword2", "Dr.", "Smith", "444555666", 2, specialization);

                // Creazione di alcuni stati degli appuntamenti
                AppointmentState state1 = new AppointmentState(1, "Completed");
                AppointmentState state2 = new AppointmentState(2, "Pending");

                Service service = new Service("General Checkup");

                Appointment appointment1 = new Appointment(1, animal1, vet1, LocalDateTime.of(2024, 8, 19, 10, 30),
                                service, 10,
                                state1);
                Appointment appointment2 = new Appointment(2, animal2, vet2, LocalDateTime.of(2024, 8, 20, 15, 00),
                                service, 10,
                                state2);
                // Creazione di alcuni appuntamenti
                List<Appointment> appointments = new ArrayList<>();

                appointments.add(appointment1);
                appointments.add(appointment2);

                // Creazione del JFrame e aggiunta del pannello
                JFrame frame = new JFrame("Appointments");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setSize(1000, 400);
                frame.setLocationRelativeTo(null);

                AppointmentListView appointmentListView = new AppointmentListView(
                                new AppointmentListController(appointments));
                frame.add(appointmentListView);

                frame.setVisible(true);
        }
}