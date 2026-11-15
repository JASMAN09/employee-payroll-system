package com.payroll.model;

public class Employee {
    private int id; private String name, email, department, designation;
    private double basicSalary, allowances, deductions;
    public Employee(int id, String name, String email, String department, String designation, double basicSalary, double allowances, double deductions) {
        this.id=id; this.name=name; this.email=email; this.department=department; this.designation=designation;
        this.basicSalary=basicSalary; this.allowances=allowances; this.deductions=deductions;
    }
    public Employee(String name, String email, String department, String designation, double basicSalary, double allowances, double deductions) {
        this(0,name,email,department,designation,basicSalary,allowances,deductions);
    }
    public int getId(){return id;} public String getName(){return name;} public String getEmail(){return email;}
    public String getDepartment(){return department;} public String getDesignation(){return designation;}
    public double getBasicSalary(){return basicSalary;} public double getAllowances(){return allowances;} public double getDeductions(){return deductions;}
    public double getNetSalary(){return basicSalary+allowances-deductions;}
}