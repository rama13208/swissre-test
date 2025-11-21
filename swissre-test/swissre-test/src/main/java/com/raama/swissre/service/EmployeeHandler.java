package com.raama.swissre.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.raama.swissre.pojo.Employee;

import lombok.Getter;

@Getter
public class EmployeeHandler {

    private int totalEmployees;
    private int totalMangers;
    private int minimumEmployeesPerManager;
    private Set<String> generatedIds = new HashSet<>();
    private List<Employee> employees = new ArrayList<>();
    

    public EmployeeHandler(int totalEmployees) {
        this.totalEmployees = totalEmployees;       
    }
    public EmployeeHandler(){}

    public List<Employee> setupData(){        
        employees.add(new Employee(id(), "CEO_F", "CEO_L", calculateSalary(), ""));

        for(int i=2;i<=totalEmployees;i++) {
            String id = id();
            employees.add(new Employee(id, "EMP_F_"+i, "EMP_L_"+i, calculateSalary(), setManagerId(id)));

        }

        managers().forEach((k,v)->{System.out.println("manager id = "+k+", count = "+v);});
        return employees;
    }

    private String id() {
        int id;
        do {
            id = new Random().nextInt(1000, 10000);
        } while (generatedIds.contains(String.valueOf(id)));

        generatedIds.add(String.valueOf(id));
        return String.valueOf(id);
}

    private Integer calculateSalary(){
        return new Random().nextInt(30000, 1500000);
    }

    private String setManagerId(String id){       
        // choose any generated id that is not equal to the provided id
        return generatedIds.stream()
                .filter(gid -> !gid.equals(id))
                .findAny()
                .orElse("");
    }
    
    public Map<String,Double> getAverageSalaryMap(List<Employee> empList){
        Map<String,Double> avgSalaryMap = new HashMap<>();
        Map<String,List<Employee>> managerMap = empList.stream().
        filter(emp->emp.getManagerId().isEmpty()==false).collect(Collectors.groupingBy(Employee::getManagerId));
        managerMap.forEach((k,v)->{
            double avg = v.stream().mapToDouble(Employee::getSalary).average().orElse(0.0);
           avgSalaryMap.put(k, avg);
        });

        return avgSalaryMap;
    }
    
    public Map<String,Double> getManagersEarningLess(List<Employee> empList){

       // managers list with average salaries of their subordinates

       Map<String,Double>  averageSalaries = getAverageSalaryMap(empList);

        Map<String,Double> managersEarnLess = new HashMap<>();
      
        for (Employee manager : empList) {
            Double avgSal = averageSalaries.get(manager.getId());
            if (avgSal == null || avgSal <= 0.0) {
                // no subordinates or average cannot be computed; skip
                continue;
            }

            double averageSalary = avgSal;
            double managerSalary = manager.getSalary();

            if (managerSalary < averageSalary) {
                // make manager salary minimum 20% more than average salary of subordinates
                double newSalary = averageSalary * 1.2;
                // % increase to current salary
                double increasePercent = ((newSalary - managerSalary) / managerSalary) * 100;
                managersEarnLess.put(manager.getId(), increasePercent);
            }
        }
        return managersEarnLess;
        
    }
    public Map<String,Double> getManagersEarnMore(List<Employee> empList){
         // managers list with average salaries of their subordinates

       Map<String,Double>  averageSalaries = getAverageSalaryMap(empList);
        Map<String,Double> managersEarnMore = new HashMap<>();
        
         for (Employee manager : empList) {
            Double avgObj = averageSalaries.get(manager.getId());
            if (avgObj == null || avgObj <= 0.0) {
                continue;
            }
            double averageSalary = avgObj;
            double managerSalary = manager.getSalary();

            if (managerSalary > averageSalary) {
                // check how much % more manager has
                double moreSalaryPerct = ((managerSalary - averageSalary) / averageSalary) * 100;
                // the maximum allowed is 50% , we can remove 50% from managers' extra salary
                double extraSalary = moreSalaryPerct - 50.0;
                double extra = Math.max(extraSalary, 0.0);
                if (extra > 0) {
                    managersEarnMore.put(manager.getId(), extra);
                }
            }
        }
        return managersEarnMore;
    }
    
    

    public List<Employee> getEmployeesMoreManagers(int managerCount,List<Employee> empList){
        
        Map<String,Employee> empMap = empList.stream()
            .collect(Collectors.toMap(Employee::getId, emplyee -> emplyee, (existing, replacement) -> existing));
        return empList.stream().filter(emplyee->countHierachymanagers(emplyee,empMap) > managerCount).collect(Collectors.toList());
       
        
    }

    private int countHierachymanagers(Employee emp, Map<String, Employee> empMap) {
        int count = 0;
        Set<String> seen = new HashSet<>();
        String managerId = emp.getManagerId();

        while (managerId != null && !managerId.isEmpty() && empMap.containsKey(managerId)) {
            
            if (seen.contains(managerId)) {
                break;
            }
            seen.add(managerId);
            count++;
            Employee mgr = empMap.get(managerId);
            if (mgr == null) {
                break;
            }
            managerId = mgr.getManagerId();
        }

        return count;
    }
    public Map<String, Long> managers() {
        
        Map<String, Long> map = employees.stream()
            .filter(e -> e.getManagerId() != null && !e.getManagerId().isEmpty())
            .collect(Collectors.groupingBy(Employee::getManagerId, Collectors.counting()));
        return map;
    }
    
}
