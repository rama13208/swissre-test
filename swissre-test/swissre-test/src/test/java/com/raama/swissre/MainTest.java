package com.raama.swissre;


import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.raama.swissre.io.FileHandler;
import com.raama.swissre.pojo.Employee;
import com.raama.swissre.service.EmployeeHandler;

public class MainTest {

 
    List<Employee> empList = new ArrayList<>();
    EmployeeHandler handler = new EmployeeHandler();

    @BeforeEach
    public void setUpStreams() {
        empList = FileHandler.loadEmployees(Path.of("config/test.csv")); 
    }


    @Test
    public void testMnagerEarnsLess() throws Exception {
         Map<String,Double> lessSalarymanagers =  handler.getManagersEarningLess(empList);                 

        lessSalarymanagers.forEach((k,v)->{
                        System.out.println("Manager earning less :  id = "+k+", % increase = "+v);
                    });     
        boolean has14 = lessSalarymanagers.keySet().stream().anyMatch(k -> k != null && k.trim().equals("14"));
        assertTrue(has14, "Should report manager 14 as earning less");
    }

    @Test
    public void testManagerEarnsMore() throws Exception {
           Map<String,Double> moreSalManagers =  handler.getManagersEarnMore(empList);                 

            moreSalManagers.forEach((k,v)->{
                        System.out.println("Manager earning more :  id = "+k+", % increase = "+v);
                    }); 
        boolean valid = moreSalManagers.keySet().stream().anyMatch(k -> k != null && k.trim().equals("12"));  
        assertTrue(valid, "Should report manager 12 as earning more");
    }

    @Test
    public void testHierarchy() throws Exception {
       int  cunt  = 4;
        List<Employee> employeeHierachy =  handler.getEmployeesMoreManagers(cunt, empList );                 

                    employeeHierachy.forEach(emp->{
                        System.out.println("Manager more hirarcheey :  id = "+emp.getId());
                    });   
        assertTrue(employeeHierachy.size() > 0  );
    }
}
