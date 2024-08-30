package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Optional;

@Slf4j
public class StatementStudentRepository implements StudentRepository {

    @Override
    public int save(Student student){
        //todo#1 insert student

        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("INSERT INTO jdbc_students (id, name, gender, age, created_at) VALUES (\"")
                .append(student.getId()).append("\", \"")
                .append(student.getName()).append("\", \"")
                .append(student.getGender().toString()).append("\", \"")
                .append(student.getAge()).append("\", \"")
                .append(student.getCreatedAt()).append("\");");

        String query = queryBuilder.toString();

        try(Connection connection =  DbUtils.getConnection();
            Statement statement = connection.createStatement();
            ){

            int row = statement.executeUpdate(query);
            return row;

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Student> findById(String id){
        //todo#2 student 조회

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM jdbc_students WHERE id=\"").append(id).append("\"");

        String query = queryBuilder.toString();

        try (Connection connection = DbUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ){

            if(resultSet.next()){

                Student student = new Student(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        Student.GENDER.valueOf(resultSet.getString("gender")),
                        resultSet.getInt("age")
                );
                return Optional.of(student);
            }
        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public int update(Student student){
        //todo#3 student 수정, name <- 수정합니다.

        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("UPDATE jdbc_students SET id =\"")
                .append(student.getId()).append("\", name = \"")
                .append(student.getName()).append("\", gender = \"")
                .append(student.getGender().toString()).append("\", age = \"")
                .append(student.getAge()).append("\" WHERE id = \"")
                .append(student.getId()).append("\";");
        String query = queryBuilder.toString();

        try(Connection connection =  DbUtils.getConnection();
            Statement statement = connection.createStatement();
            ){

            int row = statement.executeUpdate(query);
            return row;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteById(String id){
        //todo#4 student 삭제

        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("DELETE FROM jdbc_students WHERE id =\"").append(id).append("\"");
        String query = queryBuilder.toString();

        try(Connection connection =  DbUtils.getConnection();
            Statement statement = connection.createStatement();
            ){

            int row = statement.executeUpdate(query);
            return row;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
