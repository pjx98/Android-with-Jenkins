package com.example.convenienestudy;

import java.util.ArrayList;

public class School{

    private String name, studentIdCounter, instructorIdCounter, schoolId;

    public School() {
        this.studentIdCounter = "-1";
        this.instructorIdCounter = "-1";
    }


    public School(String schoolId, String name){
        this.schoolId = schoolId;
        this.name = name;
        this.studentIdCounter = "-1";
        this.instructorIdCounter = "-1";
    }

    public School(String schoolId, String name, String studentIdCounter, String instructorIdCounter) {
        this.schoolId = schoolId;
        this.name = name;
        this.studentIdCounter = studentIdCounter;
        this.instructorIdCounter = instructorIdCounter;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public String getName() {
        return name;
    }

    public String getStudentIdCounter() {
        return studentIdCounter;
    }

    public String getInstructorIdCounter() {
        return instructorIdCounter;
    }


}
