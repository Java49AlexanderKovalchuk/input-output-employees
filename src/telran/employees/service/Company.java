package telran.employees.service;

import telran.employees.dto.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
public interface Company {
	boolean addEmployee(Employee empl);
	Employee removeEmployee(long id);
	Employee getEmployee(long id);
	List<Employee> getEmployees();
	List<DepartmentSalary> getDepartmentSalaryDistribution();//returns list of all departments with average salary
	List<SalaryDistribution> getSalaryDistribution(int interval);//returns salary values distribution 
	default void restore(String filePath) {
		if(Files.exists(Path.of(filePath))) {
			try(ObjectInputStream stream = new ObjectInputStream(new FileInputStream(filePath))) {
				List<Employee> employeesRestore = (List<Employee>) stream.readObject();
				employeesRestore.forEach(e -> addEmployee(e));
			}catch(Exception e) {
				throw new RuntimeException(e.toString());
			}
		}

	}
	
	default public void save(String filePath) {
		try(ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(filePath))) {
			stream.writeObject(getEmployees());
		}catch(Exception e) {
			throw new RuntimeException(e.toString());
		}

	}
	//New methods for HW #36
	List<Employee> getEmployeesByDepartment(String department);
	List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo);
	List<Employee> getEmployeesByAge(int ageFrom, int ageTo);
	Employee updateSalary(long id, int newSalary);
	Employee updateDepartment(long id, String newDepartment);
	
}
