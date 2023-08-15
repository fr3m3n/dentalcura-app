package com.dentalcura.bookingapp.dao.impl;

import com.dentalcura.bookingapp.dao.IDao;
import com.dentalcura.bookingapp.model.Dentist;
import com.dentalcura.bookingapp.sql.SQLQueries;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DentistDAOH2 implements IDao<Dentist>{
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

            statement.execute(SQLQueries.DENTIST.getCreateTable());

            statement.close();
            connection.close();
            log.info("DENTIST table was created in DB");
        } catch (SQLException | ClassNotFoundException e) {
            log.error("Something went wrong... " + e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Dentist insert(Dentist dentist) {
        Connection connection;
        PreparedStatement preparedStatement;

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            preparedStatement = connection.prepareStatement(SQLQueries.DENTIST.getInsertCustom());

            preparedStatement.setLong(1, dentist.id());
            preparedStatement.setString(2, dentist.name());
            preparedStatement.setString(3, dentist.surname());
            preparedStatement.setInt(4, dentist.licenseNumber());

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            log.info("Data inserted in a table: " + dentist.name() + " " + dentist.surname());
        } catch (SQLException | ClassNotFoundException e) {
            log.error("Something went wrong... :( " + e);
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<Dentist> selectAll() {

        Connection connection;
        PreparedStatement preparedStatement;
        List<Dentist> dentists = new ArrayList<>();

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            preparedStatement = connection.prepareStatement(SQLQueries.DENTIST.getSelectAll());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Long id = resultSet.getLong(1);
                String name = resultSet.getString(2);
                String surname = resultSet.getString(3);
                int licenseNumber = resultSet.getInt(4);

                Dentist dentist = new Dentist(id,name, surname,licenseNumber);
                dentists.add(dentist);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            log.error("Something went wrong... :( " + e);
            throw new RuntimeException(e);
        }

        log.info("Retrieving data from the database... ");
        dentists.forEach(dentist -> log.info("Dentist: " + dentist));
        return dentists;
    }

    @Override
    public Dentist selectByID(Long id) {
        Connection connection;
        PreparedStatement preparedStatement;
        Dentist dentist = null;

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            preparedStatement = connection.prepareStatement(SQLQueries.DENTIST.getSelectById());
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Long tabId = resultSet.getLong(1);
                String name = resultSet.getString(2);
                String surname = resultSet.getString(3);
                int licenseNumber = resultSet.getInt(4);
                dentist = new Dentist(tabId, name, surname,licenseNumber);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();


        } catch (SQLException | ClassNotFoundException e) {
            log.error("Something went wrong... :( " + e);
            throw new RuntimeException(e);
        }

        log.info("Selecting by id " + "(" + id + ") " + dentist);
        return dentist;
    }

    @Override
    public Dentist updateByID(Dentist dentist) {
        Connection connection;
        PreparedStatement preparedStatement;

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            preparedStatement = connection.prepareStatement(SQLQueries.DENTIST.getUpdateById());

            preparedStatement.setLong(4, dentist.id());

            preparedStatement.setString(1, dentist.name());
            preparedStatement.setString(2, dentist.surname());
            preparedStatement.setInt(3, dentist.licenseNumber());


            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            log.info("Data UPDATED in table by id " + "(" + dentist.id() + ")");
        } catch (SQLException | ClassNotFoundException e) {
            log.error("Something went wrong... :( " + e);
            throw new RuntimeException(e);
        }

        return dentist;
    }

    @Override
    public Dentist deleteByID(Long id) {
        Connection connection;
        PreparedStatement preparedStatement;

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            preparedStatement = connection.prepareStatement(SQLQueries.DENTIST.getDeleteById());

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            log.info("Data DELETED from table by id " + "(" + id + ")");
        } catch (SQLException | ClassNotFoundException e) {
            log.error("Something went wrong... :( " + e);
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public String toString() {
        return "H2 relational database";
    }


}
