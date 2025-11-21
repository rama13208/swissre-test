package com.raama.swissre;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.raama.swissre.io.FileHandler;
import com.raama.swissre.pojo.Employee;
import com.raama.swissre.service.EmployeeHandler;

public class Main {
    public static void main(String[] args) {
        

        Scanner scanner = new Scanner(System.in);
        boolean valid = true;
        while (valid) {
            System.out.println("Options:   1. Generate Employees Data  2. managers with less salary  3. managers with more salary 4.Reporting Line ");
            String input = scanner.nextLine();
             EmployeeHandler handler = new EmployeeHandler();
            switch (input) {
                case "1":
                    System.out.println("Option 1 selected. How many employees to generate?");
                    String count = scanner.nextLine();
                    generateEmployeesData(Integer.parseInt(count), Path.of("swissre-test/config/employees_"+System.currentTimeMillis()+".csv"));
                    break;
                case "2":
                    System.out.println("Manager with less salary. Give file name from config folder.");                    
                    List<Employee> empList = FileHandler.loadEmployees(Path.of("swissre-test/config/"+scanner.nextLine())); 
                   
                    Map<String,Double> lessSalarymanagers =  handler.getManagersEarningLess(empList);                 

                    lessSalarymanagers.forEach((k,v)->{
                        System.out.println("Manager earning less :  id = "+k+", % increase = "+v);
                    });      
                    
                   
                    break;
                case "3":
                    System.out.println("Manager with mmore salary. Give file name from config folder.");
                    Map<String,Double> moreSalManagers =  handler.getManagersEarnMore(FileHandler.loadEmployees(Path.of("swissre-test/config/"+scanner.nextLine())));                 

                    moreSalManagers.forEach((k,v)->{
                        System.out.println("Manager earning more :  id = "+k+", % increase = "+v);
                    });                         
                   
                    break;
                case "4":
                    System.out.println("Manager Hierachy. Give file name from config folder.");
                    String  file = scanner.nextLine();
                    System.out.println("manager count threshold ?");
                    int  mCount  = Integer.parseInt(scanner.nextLine());
                    List<Employee> employeeHierachy =  handler.getEmployeesMoreManagers(mCount, FileHandler.loadEmployees(Path.of("swissre-test/config/"+file)));                 

                    employeeHierachy.forEach(emp->{
                        System.out.println("Manager more hirarcheey :  id = "+emp.getId());
                    });                         
                   
                    break;
                default:
                    valid = false;
            }
        }
    }

    private static void generateEmployeesData(int count,Path filePath) {
         FileHandler fileHandler = new FileHandler(new EmployeeHandler(count));
         try {
            // Path filePath = Path.of("config/employees.csv");           
            Files.createDirectories(filePath.getParent());
            fileHandler.writeEmpplyees(filePath);
         } catch (Exception e) {
             e.printStackTrace();
        }
    }

    private static List<Employee> loadEmployees(final  Path filePath) {
        return FileHandler.loadEmployees(filePath);
    }

}