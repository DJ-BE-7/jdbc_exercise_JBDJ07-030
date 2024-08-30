package com.nhnacademy.jdbc.student.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Student {

    public enum GENDER{
        M,F
    }
    private final String id;
    private final String name;
    private final GENDER gender;
    private final Integer age;
    private final LocalDateTime createdAt;


    //todo#0 필요한 method가 있다면 추가합니다.
    public Student(String id, String name, GENDER gender, Integer age){
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public GENDER getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public Timestamp getCreatedAt() {
        return Timestamp.valueOf(createdAt.truncatedTo(ChronoUnit.MINUTES));
    }
}
