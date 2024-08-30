package com.nhnacademy.jdbc.user.repository.impl;

import com.nhnacademy.jdbc.user.domain.User;
import com.nhnacademy.jdbc.user.repository.UserRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Slf4j
public class StatementUserRepository implements UserRepository {

    @Override
    public Optional<User> findByUserIdAndUserPassword(String userId, String userPassword) {
        //todo#1 아이디, 비밀번호가 일치하는 User 조회

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM jdbc_users WHERE user_id = \'")
                .append(userId).append("\' and user_password = \'")
                .append(userPassword).append("\'");
        String query = queryBuilder.toString();

        try(Connection connection = DbUtils.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        ){
            if(resultSet.next()){
                User user = new User(
                        resultSet.getString("user_id"),
                        resultSet.getString("user_name"),
                        resultSet.getString("user_password")
                );
                return Optional.of(user);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> findById(String userId) {
        //#todo#2-아이디로 User 조회
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM jdbc_users WHERE user_id = \'")
                .append(userId).append("\';");
        String query = queryBuilder.toString();

        try(Connection connection = DbUtils.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
        ){
            if(resultSet.next()){
                User user = new User(
                        resultSet.getString("user_id"),
                        resultSet.getString("user_name"),
                        resultSet.getString("user_password")
                );
                return Optional.of(user);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public int save(User user) {
        //todo#3- User 저장
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO jdbc_users VALUES (\'")
                .append(user.getUserId()).append("\', \'")
                .append(user.getUserName()).append("\', \'")
                .append(user.getUserPassword()).append("\');");

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
    public int updateUserPasswordByUserId(String userId, String userPassword) {
        //todo#4-User 비밀번호 변경
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("UPDATE jdbc_users SET user_password = \'")
                .append(userPassword).append("\' WHERE user_id = \'")
                .append(userId).append("\';");
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
    public int deleteByUserId(String userId) {
        //todo#5 - User 삭제
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("DELETE FROM jdbc_users WHERE user_id = \'")
                .append(userId).append("\';");
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
