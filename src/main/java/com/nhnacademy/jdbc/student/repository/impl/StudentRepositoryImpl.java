package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;
import java.sql.*;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
public class StudentRepositoryImpl implements StudentRepository {

    @Override
    public int save(Connection connection, Student student){
        //todo#2 학생등록
        String query = "INSERT INTO jdbc_students (id, name, gender, age, created_at) VALUES (?, ?, ?, ?, ?)";

        try(PreparedStatement statement = connection.prepareStatement(query);){

            statement.setString(1, student.getId());
            statement.setString(2, student.getName());
            statement.setString(3, student.getGender().toString()); // assuming gender is of enum type GENDER {M, F}
            statement.setInt(4, student.getAge());
            statement.setTimestamp(5, Timestamp.valueOf(student.getCreatedAt().truncatedTo(ChronoUnit.MINUTES)));

            int row = statement.executeUpdate();
            return row;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Student> findById(Connection connection,String id){
        //todo#3 학생조회

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM jdbc_students WHERE id=?");

        String query = queryBuilder.toString();

        try (PreparedStatement statement = connection.prepareStatement(query);
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

        return Optional.empty();    }

    @Override
    public int update(Connection connection,Student student){
        //todo#4 학생수정
        String query = "UPDATE jdbc_students SET id = ?, name = ?, gender = ?, age = ? WHERE id =? ";

        try(PreparedStatement statement = connection.prepareStatement(query);){

            statement.setString(1, student.getId());
            statement.setString(2, student.getName());
            statement.setString(3, student.getGender().toString());
            statement.setInt(4, student.getAge());
            statement.setString(5, student.getId());

            int row = statement.executeUpdate();
            return row;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }    }

    @Override
    public int deleteById(Connection connection,String id){
        //todo#5 학생삭제
        String query = "DELETE FROM jdbc_students WHERE id =? ";

        try(PreparedStatement statement = connection.prepareStatement(query);){

            statement.setString(1, id);

            int row = statement.executeUpdate();
            return row;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}