package clinic.persistence;

import clinic.entities.Patient;
import clinic.sql.SQLQueries;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAOH2 implements IDao<Patient>{

    private final static Logger LOGGER = Logger.getLogger(PatientDAOH2.class);
    private final static String DB_JDBC_DRIVER = "org.h2.Driver";
    private final static String DB_URL = "jdbc:h2:~/test";
    private final static String DB_USER = "sa";
    private final static String DB_PASSWORD = "";

    public void createTable(){
        Connection connection;
        Statement statement;

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            statement = connection.createStatement();

            statement.execute(SQLQueries.PATIENT.getCreateTable());

            statement.close();
            connection.close();
            LOGGER.info("PATIENT table was created in DB");
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error("Something went wrong... :( " + e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Patient insert(Patient patient) {
        Connection connection;
        PreparedStatement preparedStatement;

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            preparedStatement = connection.prepareStatement(SQLQueries.PATIENT.getInsertCustom());

            preparedStatement.setLong(1, patient.id());
            preparedStatement.setString(2, patient.name());
            preparedStatement.setString(3, patient.surname());
            preparedStatement.setString(4, patient.address());
            preparedStatement.setInt(5, patient.niNumber());
            preparedStatement.setString(6, patient.registrationDate());

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
            LOGGER.info("Data inserted in a table: " + patient.name() + " " + patient.surname());
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error("Something went wrong... :( " + e);
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<Patient> selectAll() {

        Connection connection;
        PreparedStatement preparedStatement;
        List<Patient> patients = new ArrayList<>();

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            preparedStatement = connection.prepareStatement(SQLQueries.PATIENT.getSelectAll());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Long id = resultSet.getLong(1);
                String name = resultSet.getString(2);
                String surname = resultSet.getString(3);
                String address = resultSet.getString(4);
                int niNumber = resultSet.getInt(5);
                String registrationDate = resultSet.getString(6);

                Patient patient = new Patient(id,name, surname, address, niNumber, registrationDate);
                patients.add(patient);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error("Something went wrong... :( " + e);
            throw new RuntimeException(e);
        }

        LOGGER.info("Retrieving data from the database... ");
        patients.forEach(LOGGER::info);
        return patients;
    }

    @Override
    public Patient selectByID(Long id) {
        Connection connection;
        PreparedStatement preparedStatement;
        Patient patient = null;

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            preparedStatement = connection.prepareStatement(SQLQueries.PATIENT.getSelectById());
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Long tabId = resultSet.getLong(1);
                String name = resultSet.getString(2);
                String surname = resultSet.getString(3);
                String address = resultSet.getString(4);
                int niNumber = resultSet.getInt(5);
                String regDate = resultSet.getString(6);

                patient = new Patient(tabId, name, surname, address, niNumber, regDate);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();


        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error("Something went wrong... :( " + e);
            throw new RuntimeException(e);
        }

        LOGGER.info("Selecting by id " + "(" + id + ") " + patient);
        return patient;
    }

    @Override
    public Patient updateByID(Patient patient) {
        Connection connection;
        PreparedStatement preparedStatement;

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            preparedStatement = connection.prepareStatement(SQLQueries.PATIENT.getUpdateById());

            preparedStatement.setLong(6, patient.id());

            preparedStatement.setString(1, patient.name());
            preparedStatement.setString(2, patient.surname());
            preparedStatement.setString(3, patient.address());
            preparedStatement.setInt(4, patient.niNumber());
            preparedStatement.setString(5, patient.registrationDate());


            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            LOGGER.info("Data UPDATED in table by id " + "(" + patient.id() + ")");
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error("Something went wrong... :( " + e);
            throw new RuntimeException(e);
        }

        return patient;
    }

    @Override
    public Patient deleteByID(Long id) {
        Connection connection;
        PreparedStatement preparedStatement;

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            preparedStatement = connection.prepareStatement(SQLQueries.PATIENT.getDeleteById());

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            LOGGER.info("Data DELETED from table by id " + "(" + id + ")");
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error("Something went wrong... :( " + e);
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public String toString() {
        return "H2 relational database";
    }
}
