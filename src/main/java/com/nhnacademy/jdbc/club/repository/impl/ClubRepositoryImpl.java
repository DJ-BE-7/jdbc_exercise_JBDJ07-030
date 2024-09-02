package com.nhnacademy.jdbc.club.repository.impl;

import com.nhnacademy.jdbc.club.domain.Club;
import com.nhnacademy.jdbc.club.repository.ClubRepository;
import com.nhnacademy.jdbc.util.DbUtils;

import java.sql.*;
import java.util.Optional;

public class ClubRepositoryImpl implements ClubRepository {

    @Override
    public Optional<Club> findByClubId(Connection connection, String clubId) {

        //todo#3 club 조회

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM jdbc_club WHERE club_id=?;");

        String query = queryBuilder.toString();

        try (PreparedStatement statement = connection.prepareStatement(query);
        ){
            statement.setString(1, clubId);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                Club account = new Club(
                        resultSet.getString("club_id"),
                        resultSet.getString("club_name"),
                        resultSet.getTimestamp("club_created_at").toLocalDateTime()
                );
                return Optional.of(account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();    }

    @Override
    public int save(Connection connection, Club club) {
        //todo#4 club 생성, executeUpdate() 결과를 반환
        String query = "INSERT INTO jdbc_club (club_id, club_name, club_created_at) VALUES (?, ?, ?);";

        try(PreparedStatement statement = connection.prepareStatement(query);){

            statement.setString(1, club.getClubId());
            statement.setString(2, club.getClubName());
            statement.setTimestamp(3, Timestamp.valueOf(club.getClubCreatedAt()));


            int row = statement.executeUpdate();
            return row;

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(Connection connection, Club club) {
        //todo#5 club 수정, clubName을 수정합니다. executeUpdate()결과를 반환

        String query = "UPDATE jdbc_club SET  club_name = ? WHERE club_id =? ";

        try(PreparedStatement statement = connection.prepareStatement(query);
        ){

            statement.setString(1, club.getClubName());
            statement.setString(2, club.getClubId());

            int row = statement.executeUpdate();
            return row;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }    }

    @Override
    public int deleteByClubId(Connection connection, String clubId) {
        //todo#6 club 삭제, executeUpdate()결과 반환

        String query = "DELETE FROM jdbc_club WHERE club_id =? ";


        try(PreparedStatement statement = connection.prepareStatement(query);){

            statement.setString(1, clubId);

            int row = statement.executeUpdate();
            return row;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countByClubId(Connection connection, String clubId) {
        //todo#7 clubId에 해당하는 club의 count를 반환

        int count=0;
        //todo#3 select count(*)를 이용해서 계좌의 개수를 count해서 반환
        String query = "SELECT COUNT(*) FROM jdbc_club WHERE club_id = ?;";

        try(PreparedStatement statement = connection.prepareStatement(query);){

            statement.setString(1, clubId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;    }
}
