package telran.employees.controller;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

import telran.employees.dto.DepartmentSalary;
import telran.employees.dto.Employee;
import telran.employees.dto.SalaryDistribution;
import telran.employees.service.Company;
import telran.view.InputOutput;
import telran.view.Item;


public class CompanyController {
   private static final long MIN_ID = 100000;
private static final long MAX_ID = 999999;
private static final int MIN_SALARY = 6000;
private static final int MAX_SALARY = 50000;
private static final int MAX_AGE = 75;
private static final int MIN_AGE = 20;
private static final int MIN_INTERVAL = 500;
private static final int MAX_INTERVAL = 5000;
static Company company;
	public static ArrayList<Item> getCompanyItems(Company company) {
		CompanyController.company = company;
		ArrayList<Item> res = new ArrayList<>(Arrays.asList(
				getItems()));
		return res;
	}
	private static Item[] getItems() {
		
		return new Item[] {
				Item.of("Add new Employee", CompanyController::addEmployeeItem),
				Item.of("Remove Employee", CompanyController::removeEmployeeItem),
				Item.of("All Employees", CompanyController::getEmployeesItem),
				Item.of("Data about Employee", CompanyController::getEmployeeItem),
				Item.of(" Employees by Salary", CompanyController::getEmployeesBySalaryItem),
				Item.of("Employees by Department", CompanyController::getEmployeesByDepartmentItem),
				Item.of("Update salary", CompanyController::updateSalaryItem),
				Item.of("Departments and Salary", CompanyController::getDepartmentSalaryDistributionItem),
				Item.of("Distribution by Salary", CompanyController::getSalaryDistributionItem),
				Item.of("Employees by Age", CompanyController::getEmployeesByAgeItem),
				Item.of("Update Department", CompanyController::updateDepartmentItem)
		};
	}
	static private Long getId(InputOutput io, boolean isExists) {
		Long id = io.readLong("Enter Employee identity", "Wrong identity value",
				MIN_ID, MAX_ID);
		Employee empl = company.getEmployee(id);
		
		String exceptionText = "";
		Long res = (empl != null && isExists) || (empl == null && !isExists) ?
				id : null;
		if(res == null ) {
			exceptionText = isExists ? String.format("Employee with id %d doesn't exist", id)
					: String.format("Employee with id %d already exists", id);
		} 
		if (!exceptionText.isEmpty()) {
			throw new RuntimeException(exceptionText);
		}
		return res;
		
	}
	static private  <T> void displayResult(List<T> list, InputOutput io) {
		if(list.isEmpty()) {
			io.writeLine("No data matching the request");
		}
		list.forEach(io::writeLine);
	}
	static private Set<String> departments = new HashSet<>(Arrays.asList(new String[] {
			"QA", "Development", "Audit", "Management", "Accounting"
	}));
	static void addEmployeeItem(InputOutput io) {
		Long id = getId(io, false);
		
		String name = io.readString("Enter name", "Wrong name",
				str -> str.matches("[A-Z][a-z]+"));
		String department = getDepartment(io);
		int salary = io.readInt("Enter salary", "Wrong salary", MIN_SALARY, MAX_SALARY);
		LocalDate birthDate = io.readDate("Enter birth data", "Wrong birth date entered",
				getBirthdate(MAX_AGE), getBirthdate(MIN_AGE));
		boolean res = company.addEmployee(new Employee(id, name, department, salary, birthDate));
		io.writeLine(res ? String.format("Employee with id %d has been added", id) : 
			String.format("Employee with id %d already exists", id));
	}
	private static String getDepartment(InputOutput io) {
		return io.readString("Enter department " + departments, "Wrong department", departments );
	}
	private static LocalDate getBirthdate(int age) {
		
		return LocalDate.now().minusYears(age);
	}
	static void removeEmployeeItem(InputOutput io) {
		Long id = getId(io, true);
		
		io.write("Removed employee is ");
		io.writeLine(company.removeEmployee(id));
	}
	static void getEmployeeItem(InputOutput io) {
		Long id = getId(io, true);
		
		io.write("employee is ");
		io.writeLine(company.getEmployee(id));
	}
	static void getEmployeesItem(InputOutput io) {
		displayResult(company.getEmployees(), io);
	}
	static void getDepartmentSalaryDistributionItem(InputOutput io) {
		displayResult(company.getDepartmentSalaryDistribution(), io);
		
	}
	static void getSalaryDistributionItem(InputOutput io) {
		int interval = io.readInt("Enter salary distribution interval" , "Wrong interval",
				MIN_INTERVAL, MAX_INTERVAL);
		displayResult(company.getSalaryDistribution(interval), io);
	}
	static void getEmployeesByDepartmentItem(InputOutput io) {
		String department = getDepartment(io);
		displayResult(company.getEmployeesByDepartment(department), io);
	}
	static void getEmployeesBySalaryItem(InputOutput io) {
		int[] fromTo = getSalaries(io);
		displayResult(company.getEmployeesBySalary(fromTo[0], fromTo[1]), io);
	}
	private static int[] getSalaries(InputOutput io) {
		int from = io.readInt("Enter salary from", "Wrong salary-from value", MIN_SALARY,
				MAX_SALARY - 1);
		int to =  io.readInt("Enter salary to", "Wrong salary-to value", from, MAX_SALARY);
		return new int[] {from, to};
	}
	static void getEmployeesByAgeItem(InputOutput io) {
		int [] fromTo = getAgies(io);
		displayResult(company.getEmployeesByAge(fromTo[0], fromTo[1]), io);
	}
	private static int[] getAgies(InputOutput io) {
		int from = io.readInt("Enter age from", "Wrong age-from value", MIN_AGE, MAX_AGE - 1);
		int to =  io.readInt("Enter age to", "Wrong age-to value", from, MAX_AGE);
		return new int[] {from, to};
	}
	static void updateSalaryItem(InputOutput io) {
		Long id = getId(io, true);
		
		int salary = io.readInt("Enter new salary value", "Wrong salary value",
				MIN_SALARY, MAX_SALARY);
		Employee empl = company.updateSalary(id, salary);
		io.writeLine(String.format("old salary value %d of employee %d"
				+ " has been updated with new value %d", empl.salary(),
				empl.id(), salary));
	}
	static void updateDepartmentItem(InputOutput io) {
		Long id = getId(io, true);
		
		String department = getDepartment(io);
		Employee empl = company.updateDepartment(id, department);
		io.writeLine(String.format("old deprtment %s of employee %d"
				+ " has been updated with department %s", empl.department(),
				empl.id(), department));
	}
	
	

}
