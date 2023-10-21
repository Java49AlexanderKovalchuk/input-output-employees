package telran.employees;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;

import telran.employees.dto.*;
import telran.employees.service.Company;
import telran.net.*;

public class CompanyProtocol implements ApplProtocol {

	private Company company;
	Class<?> clazz;
	private Method method;
	
	public CompanyProtocol(Company company) {
		this.company = company;
		
	}
	
	@Override
	public Response getResponse(Request request) {
		
		Response response = null;
		String requestType = request.requestType();
		Serializable data = request.requestData();
		clazz = this.getClass();					
			
		try {
			
			method = clazz.getDeclaredMethod(requestType.replace("/", "_"), Serializable.class);
		
			Serializable responseData = (Serializable) method.invoke(this, data);
			

			response = (responseData instanceof Response) ? 
					(Response) responseData :
				new Response(ResponseCode.OK, responseData);
			
		} 
		catch (NoSuchMethodException e) {
			new Response(ResponseCode.WRONG_TYPE, requestType +
	    		" is unsupported in the Company Protocol");
		} catch(Exception e) {
			response = new Response(ResponseCode.WRONG_DATA, e.toString());
		}
		return response;
	}

	 private Serializable employees_age(Serializable data) {
		FromTo fromTo = (FromTo) data;
		int ageFrom = fromTo.from();
		int ageTo = fromTo.to();
		return new ArrayList<>(company.getEmployeesByAge(ageFrom, ageTo));
	}

	private Serializable employees_salary(Serializable data) {
		FromTo fromTo = (FromTo) data;
		int salaryFrom = fromTo.from();
		int salaryTo = fromTo.to();
		return new ArrayList<>(company.getEmployeesBySalary(salaryFrom, salaryTo));
	}

	private Serializable employees_department(Serializable data) {
		String departament = (String) data;
		return new ArrayList<>(company.getEmployeesByDepartment(departament));
	}

	private Serializable salary_distribution(Serializable data) {
		int interval = (int) data;
		return new ArrayList<>(company.getSalaryDistribution(interval));
	}

	private Serializable department_salary_distribution(Serializable data) {
		return new ArrayList<> (company.getDepartmentSalaryDistribution());
	}

	private Serializable employee_remove(Serializable data) {
		long id = (long) data;
		return company.removeEmployee(id);
	}

	private Serializable salary_update(Serializable data) {
		
		@SuppressWarnings("unchecked")
		UpdateData<Integer> updateData = (UpdateData<Integer>) data;
		long id = updateData.id();
		int newSalary = updateData.data();
		return company.updateSalary(id, newSalary);
	}

	private Serializable department_update(Serializable data) {
		
		@SuppressWarnings("unchecked")
		UpdateData<String> updateData = (UpdateData<String>) data;
		long id = updateData.id();
		String department = updateData.data();
		return company.updateDepartment(id, department);
	}
	 
	 Serializable employees_get(Serializable data) {
		
		return new ArrayList<> (company.getEmployees());
	}

	Serializable employee_get(Serializable data) {
		long id = (long) data;
		return company.getEmployee(id );
	}

	Serializable employee_add(Serializable data) {
		
		Employee empl = (Employee) data;
		return company.addEmployee(empl);
	}

}
