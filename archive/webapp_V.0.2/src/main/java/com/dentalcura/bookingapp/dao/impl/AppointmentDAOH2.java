package com.dentalcura.bookingapp.dao.impl;

import com.dentalcura.bookingapp.dao.IDao;
import com.dentalcura.bookingapp.model.Address;
import com.dentalcura.bookingapp.model.Appointment;
import com.dentalcura.bookingapp.model.Dentist;
import com.dentalcura.bookingapp.model.Patient;
import com.dentalcura.bookingapp.service.AppointmentService;
import com.dentalcura.bookingapp.service.DentistService;
import com.dentalcura.bookingapp.service.PatientService;
import com.dentalcura.bookingapp.util.DB;
import com.dentalcura.bookingapp.util.SQLQueries;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository("appointmentDAOH2")
public class AppointmentDAOH2 implements IDao<Appointment>{

    @Autowired
    private DentistService dentistService;
    @Autowired
    private PatientService patientService;

    @Override
    public Appointment insert(Appointment appointment) {
        Connection connection;
        PreparedStatement preparedStatement;

        try {
            Class.forName(DB.DRIVER);
            connection = DriverManager.getConnection(DB.URL,DB.USR,DB.PWD);
            preparedStatement = connection.prepareStatement(SQLQueries.APPOINTMENT.getInsertCustom());

//            preparedStatement.setLong(1, appointment.id());
            preparedStatement.setString(1, appointment.date());
            preparedStatement.setLong(2, appointment.patient().id());
            preparedStatement.setLong(3, appointment.dentist().id());

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            log.info("New reg ADDED to table [" + appointment + "]");

        } catch (SQLException | ClassNotFoundException e) {
            log.error("Add new " + appointment +  " to table was not possible");
            log.error(String.valueOf(e));
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<Appointment> selectAll() {

        Connection connection;
        PreparedStatement preparedStatement;
        List<Appointment> appointments = new ArrayList<>();

        Patient patient;
        Dentist dentist;

        PatientService patientService = new PatientService();
        patientService.setPatientIDao(new PatientDAOH2());
        DentistService dentistService = new DentistService();
        dentistService.setDentistIDao(new DentistDAOH2());

        try {
            Class.forName(DB.DRIVER);
            connection = DriverManager.getConnection(DB.URL,DB.USR,DB.PWD);
            preparedStatement = connection.prepareStatement(SQLQueries.APPOINTMENT.getSelectAll());
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()){
                Long id = resultSet.getLong(1);
                String date = resultSet.getString(2);
                Long patient_id = resultSet.getLong(3);
                Long dentist_id = resultSet.getLong(4);

                patient = patientService.selectPatientByID(patient_id);
                dentist = dentistService.selectDentistByID(dentist_id);

                Appointment appointment = new Appointment(id, date, patient, dentist);
                appointments.add(appointment);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

            log.info("Reading data from DB...");

        } catch (SQLException | ClassNotFoundException e) {
            log.error("Read data from DB was not possible");
            log.error(String.valueOf(e));
            throw new RuntimeException(e);
        }

        log.info("Rendering data for all Appointments in DB...");
        appointments.forEach(appointment -> log.info("id [" + appointment.id() + "] " + appointment));

        return appointments;
    }

    @Override
    public Appointment selectByID(Long id) {
        Connection connection;
        PreparedStatement preparedStatement;

        Patient patient;
        Dentist dentist;
        Appointment appointment = null;

        PatientService patientService = new PatientService();
        patientService.setPatientIDao(new PatientDAOH2());
        DentistService dentistService = new DentistService();
        dentistService.setDentistIDao(new DentistDAOH2());

        log.info("Searching Appointment by ID in DB...");
        try {
            Class.forName(DB.DRIVER);
            connection = DriverManager.getConnection(DB.URL,DB.USR,DB.PWD);
            preparedStatement = connection.prepareStatement(SQLQueries.APPOINTMENT.getSelectById());
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                String date = resultSet.getString(2);
                Long patient_id = resultSet.getLong(3);
                Long dentist_id = resultSet.getLong(4);

                patient = patientService.selectPatientByID(patient_id);
                dentist = dentistService.selectDentistByID(dentist_id);

                appointment = new Appointment(id, date, patient, dentist);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();



        } catch (SQLException | ClassNotFoundException e) {
            log.error("Retrieve Appointment by ID from DB was not possible");
            log.error(String.valueOf(e));
            throw new RuntimeException(e);
        }

        log.info("Register id " + "[" + id + "] was found.");
        log.info(String.valueOf(appointment));

        return appointment;
    }

    @Override
    public Appointment updateByID(Appointment appointment) {
        Connection connection;
        PreparedStatement preparedStatement;

        try {
            Class.forName(DB.DRIVER);
            connection = DriverManager.getConnection(DB.URL,DB.USR,DB.PWD);
            preparedStatement = connection.prepareStatement(SQLQueries.APPOINTMENT.getUpdateById());

            preparedStatement.setLong(2, appointment.id());
            preparedStatement.setString(1, appointment.date());


            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            log.info("Appointment id  " + "[" + appointment.id() + "]" +" was UPDATED in table");

        } catch (SQLException | ClassNotFoundException e) {
            log.error("Updating Appointment id "  +  "[" + appointment.id() + "]" + " was not possible");
            log.error(String.valueOf(e));
            throw new RuntimeException(e);
        }

        log.info("UPDATED [" + appointment + "]");

        return appointment;
    }

    @Override
    public Appointment deleteByID(Long id) {
        Connection connection;
        PreparedStatement preparedStatement;

        try {
            Class.forName(DB.DRIVER);
            connection = DriverManager.getConnection(DB.URL,DB.USR,DB.PWD);
            preparedStatement = connection.prepareStatement(SQLQueries.APPOINTMENT.getDeleteById());

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            log.info("Appointment id  " + "[" + id + "]" +" was deleted from table");

        } catch (SQLException | ClassNotFoundException e) {
            log.error("Delete Appointment id "  +  "[" + id + "]" + " from table was not possible");
            log.error(String.valueOf(e));
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public String toString() {
        return "H2 relational database";
    }


}
