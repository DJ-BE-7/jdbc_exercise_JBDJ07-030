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

        String query = "INSERT INTO jdbc_students (id, name, gender, age, created_at) VALUES (?, ?, ?, ?, ?)";


        try(Connection connection =  DbUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);){

            statement.setString(1, student.getId());
            statement.setString(2, student.getName());
            statement.setString(3, student.getGender().toString()); // assuming gender is of enum type GENDER {M, F}
            statement.setInt(4, student.getAge());
            statement.setTimestamp(5, student.getCreatedAt());

            int row = statement.executeUpdate();
            return row;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public Optional<Student> findById(String id){
        //todo#2 student 조회

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM jdbc_students WHERE id=?");

        String query = queryBuilder.toString();


        try (Connection connection = DbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ){
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

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
        String query = "UPDATE jdbc_students SET id = ?, name = ?, gender = ?, age = ? WHERE id =? ";

        try(Connection connection =  DbUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);){

            statement.setString(1, student.getId());
            statement.setString(2, student.getName());
            statement.setString(3, student.getGender().toString());
            statement.setInt(4, student.getAge());
            statement.setString(5, student.getId());

            int row = statement.executeUpdate();
            return row;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteById(String id){
       //todo#4 student 삭제

        String query = "DELETE FROM jdbc_students WHERE id =? ";


        try(Connection connection =  DbUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);){

            statement.setString(1, id);

            int row = statement.executeUpdate();
            return row;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
