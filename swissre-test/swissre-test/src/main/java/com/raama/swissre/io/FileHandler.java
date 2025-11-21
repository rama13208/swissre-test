package com.raama.swissre.io;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.raama.swissre.pojo.Employee;
import com.raama.swissre.service.EmployeeHandler;

public class FileHandler {

    private EmployeeHandler handler;
    public FileHandler(EmployeeHandler handler) {
        this.handler = handler;
    }
    public FileHandler(){}  

    public static List<Employee> loadEmployees(final Path filePath) {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split with limit to ensure 5 values, last can be empty
                String[] parts = line.split(",", -1);
                if (parts.length == 5) {
                    Employee emp = new Employee(
                        parts[0],
                        parts[1],
                        parts[2],
                        Integer.parseInt(parts[3]),
                        parts[4]
                    );
                    employees.add(emp);                
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employees;
    }

    public void writeEmpplyees(final Path outputPath) throws IOException {

        List<Employee> employees = handler.setupData();
        try(OutputStream os = new FileOutputStream(outputPath.toFile())) {
            for(Employee emp : employees){
                os.write((emp+"\n").getBytes());
            }
            
        } 

    }
}
