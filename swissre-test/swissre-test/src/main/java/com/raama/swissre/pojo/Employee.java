package com.raama.swissre.pojo;

public class Employee {

    private String id;
    private String firstName;
    private String lastName;
    private Integer salary;
    private String managerId;

    public Employee(String id, String firstName, String lastName, Integer salary, String managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public double getSalary() {
        return salary;
    }
    public String getManagerId() {
        return managerId;
    }

    @Override
    public String toString() {
        return id + "," + firstName + "," + lastName + "," + salary + "," + managerId;
    }
    
}
