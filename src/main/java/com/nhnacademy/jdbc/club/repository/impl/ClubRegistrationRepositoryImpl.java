package com.nhnacademy.jdbc.club.repository.impl;

import com.nhnacademy.jdbc.club.domain.ClubStudent;
import com.nhnacademy.jdbc.club.repository.ClubRegistrationRepository;
import com.nhnacademy.jdbc.student.domain.Student;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;

@Slf4j
public class ClubRegistrationRepositoryImpl implements ClubRegistrationRepository {

    @Override
    public int save(Connection connection, String studentId, String clubId) {
        //todo#11 - 핵생 -> 클럽 등록, executeUpdate() 결과를 반환

        String query = "INSERT INTO jdbc_club_registrations (student_id, club_id) VALUES (?, ?);";

        try(PreparedStatement statement = connection.prepareStatement(query);){

            statement.setString(1, studentId);
            statement.setString(2, clubId);

            int row = statement.executeUpdate();
            return row;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }    }

    @Override
    public int deleteByStudentIdAndClubId(Connection connection, String studentId, String clubId) {
        //todo#12 - 핵생 -> 클럽 탈퇴, executeUpdate() 결과를 반환
        String query = "DELETE FROM jdbc_club_registrations WHERE student_id =?; ";

        try(PreparedStatement statement = connection.prepareStatement(query);){

            statement.setString(1, studentId);

            int row = statement.executeUpdate();
            return row;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }      }

    @Override
    public List<ClubStudent> findClubStudentsByStudentId(Connection connection, String studentId) {
        //todo#13 - 핵생 -> 클럽 등록, executeUpdate() 결과를 반환
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select a.id as student_id, a.name as student_name, c.club_id, c.club_name from jdbc_students")
                .append(" a inner join jdbc_club_registrations b on a.id=b.student_id inner join jdbc_club c on b.club_id=c.club_id where a.id=?");

        String query = queryBuilder.toString();
        List<ClubStudent> clubStudents = new ArrayList<>();

        ResultSet rs = null;
        try (PreparedStatement statement = connection.prepareStatement(query);
        ){
            statement.setString(1, studentId);
             rs = statement.executeQuery();

           while(rs.next()){
               clubStudents.add(
                       new ClubStudent(
                               rs.getString("student_id"),
                               rs.getString("student_name"),
                               rs.getString("club_id"),
                               rs.getString("club_name")
                       )
               );
           }
           return clubStudents;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (Objects.nonNull(rs)) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<ClubStudent> findClubStudents(Connection connection) {
        //todo#21 - join
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select a.id as student_id, a.name as student_name, ")
                .append("c.club_id, c.club_name from jdbc_students a inner ")
                .append("join jdbc_club_registrations b on a.id=b.student_id inner join jdbc_club c on b.club_id=c.club_id order by a.id asc, b.club_id asc");

        String query = queryBuilder.toString();
        List<ClubStudent> clubStudents = new ArrayList<>();

        ResultSet rs = null;
        try (PreparedStatement statement = connection.prepareStatement(query);
        ){
             rs = statement.executeQuery();

            while(rs.next()){
                clubStudents.add(
                        new ClubStudent(
                                rs.getString("student_id"),
                                rs.getString("student_name"),
                                rs.getString("club_id"),
                                rs.getString("club_name")
                        )
                );
            }
            return clubStudents;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (Objects.nonNull(rs)) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<ClubStudent> findClubStudents_left_join(Connection connection) {
        //todo#22 - left join
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("select   a.id as student_id,  a.name as student_name, ")
                .append(" c.club_id,  c.club_name from jdbc_students a  left join jdbc_club_registrations ")
                .append("b on a.id=b.student_id left join jdbc_club c on b.club_id=c.club_id order by a.id asc, b.club_id asc;");
        String query = queryBuilder.toString();

        try{
            return getClubStudentListByQuery(connection, query);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<ClubStudent> findClubStudents_right_join(Connection connection) {
        //todo#23 - right join
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("select a.id as student_id, a.name as student_name, ")
                .append("c.club_id, c.club_name from jdbc_students a right join jdbc_club_registrations")
                .append(" b on a.id=b.student_id right join jdbc_club c on b.club_id=c.club_id order by c.club_id asc,a.id asc");
        String query = queryBuilder.toString();

        try{
            return getClubStudentListByQuery(connection, query);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<ClubStudent> findClubStudents_full_join(Connection connection) {
        //todo#24 - full join = left join union right join
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select   a.id as student_id,  a.name as student_name, ")
                .append(" c.club_id,  c.club_name from jdbc_students a  left join jdbc_club_registrations ")
                .append("b on a.id=b.student_id left join jdbc_club c on b.club_id=c.club_id ")
                .append(System.lineSeparator()).append("union").append(System.lineSeparator())
                .append("select a.id as student_id, a.name as student_name, ")
                .append("c.club_id, c.club_name from jdbc_students a right join jdbc_club_registrations")
                .append(" b on a.id=b.student_id right join jdbc_club c on b.club_id=c.club_id;");
        String query = queryBuilder.toString();
        try{
            return getClubStudentListByQuery(connection, query);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<ClubStudent> findClubStudents_left_excluding_join(Connection connection) {
        //todo#25 - left excluding join

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select   a.id as student_id,  a.name as student_name,")
                .append("  c.club_id,  c.club_name from jdbc_students a  left join jdbc_club_registrations b ")
                .append("on a.id=b.student_id left join jdbc_club c on b.club_id=c.club_id where c.club_id is null order by a.id asc");
        String query = queryBuilder.toString();
        try{
            return getClubStudentListByQuery(connection, query);
        }catch (Exception e){
            throw e;
        }    }

    @Override
    public List<ClubStudent> findClubStudents_right_excluding_join(Connection connection) {
        //todo#26 - right excluding join

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select   a.id as student_id,  a.name as student_name,  ")
                .append("c.club_id,  c.club_name from jdbc_students a  right join jdbc_club_registrations b ")
                .append("on a.id=b.student_id right join jdbc_club c on b.club_id=c.club_id where a.id is null order by b.club_id asc ");
        String query = queryBuilder.toString();
        try{
            return getClubStudentListByQuery(connection, query);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<ClubStudent> findClubStudents_outher_excluding_join(Connection connection) {
        //todo#27 - outher_excluding_join = left excluding join union right excluding join
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select   a.id as student_id,  a.name as student_name,")
                .append("  c.club_id,  c.club_name from jdbc_students a  left join jdbc_club_registrations b ")
                .append("on a.id=b.student_id left join jdbc_club c on b.club_id=c.club_id where c.club_id is null")
                .append(System.lineSeparator()).append("union").append(System.lineSeparator())
                .append("select   a.id as student_id,  a.name as student_name,  ")
                .append("c.club_id,  c.club_name from jdbc_students a  right join jdbc_club_registrations b ")
                .append("on a.id=b.student_id right join jdbc_club c on b.club_id=c.club_id where a.id is null;");
        String query = queryBuilder.toString();
        try{
            return getClubStudentListByQuery(connection, query);
        }catch (Exception e){
            throw e;
        }

    }
    public List<ClubStudent> getClubStudentListByQuery(Connection connection, String query){

        List<ClubStudent> clubStudents = new ArrayList<>();
        ResultSet rs = null;
        try (PreparedStatement statement = connection.prepareStatement(query);
        ){
             rs = statement.executeQuery();

            while(rs.next()){
                clubStudents.add(
                        new ClubStudent(
                                rs.getString("student_id"),
                                rs.getString("student_name"),
                                rs.getString("club_id"),
                                rs.getString("club_name")
                        )
                );
            }
            return clubStudents;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (Objects.nonNull(rs)) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}