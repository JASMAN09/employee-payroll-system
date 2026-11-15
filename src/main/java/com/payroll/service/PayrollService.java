package com.payroll.service;
import com.payroll.db.Database; import com.payroll.model.Employee;
import java.sql.*; import java.util.*;

public class PayrollService {
    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> list=new ArrayList<>();
        try(Connection c=Database.getConnection(); PreparedStatement ps=c.prepareStatement("SELECT * FROM employees ORDER BY name"); ResultSet rs=ps.executeQuery()) {
            while(rs.next()) list.add(map(rs));
        } return list;
    }
    public void saveEmployee(Employee e) throws SQLException {
        String sql = e.getId()==0 ? "INSERT INTO employees(name,email,department,designation,basic_salary,allowances,deductions) VALUES(?,?,?,?,?,?,?)" : "UPDATE employees SET name=?,email=?,department=?,designation=?,basic_salary=?,allowances=?,deductions=? WHERE id=?";
        try(Connection c=Database.getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1,e.getName()); ps.setString(2,e.getEmail()); ps.setString(3,e.getDepartment()); ps.setString(4,e.getDesignation());
            ps.setDouble(5,e.getBasicSalary()); ps.setDouble(6,e.getAllowances()); ps.setDouble(7,e.getDeductions()); if(e.getId()!=0) ps.setInt(8,e.getId()); ps.executeUpdate();
        }
    }
    public void deleteEmployee(int id) throws SQLException { try(Connection c=Database.getConnection(); PreparedStatement ps=c.prepareStatement("DELETE FROM employees WHERE id=?")){ps.setInt(1,id);ps.executeUpdate();} }
    public Map<String,double[]> departmentReport() throws SQLException {
        Map<String,double[]> report=new LinkedHashMap<>();
        String sql="SELECT department, COUNT(*) AS staff, SUM(basic_salary) AS basic, SUM(allowances) AS allowances, SUM(deductions) AS deductions, SUM(basic_salary+allowances-deductions) AS net FROM employees GROUP BY department ORDER BY department";
        try(Connection c=Database.getConnection(); PreparedStatement ps=c.prepareStatement(sql); ResultSet rs=ps.executeQuery()) {
            while(rs.next()) report.put(rs.getString("department"),new double[]{rs.getDouble("staff"),rs.getDouble("basic"),rs.getDouble("allowances"),rs.getDouble("deductions"),rs.getDouble("net")});
        } return report;
    }
    private Employee map(ResultSet r)throws SQLException{return new Employee(r.getInt("id"),r.getString("name"),r.getString("email"),r.getString("department"),r.getString("designation"),r.getDouble("basic_salary"),r.getDouble("allowances"),r.getDouble("deductions"));}
}