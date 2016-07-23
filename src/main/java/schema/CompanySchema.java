package schema;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class CompanySchema {
	private String address;
	private String email;
	private String name;
	private int isActive;	//1-online, 0-offline
	private String password;
	private HashMap<String, String> employees = new HashMap<String, String>();
	private HashMap<String, String> tasks = new HashMap<String, String>();
	
	public HashMap<String, String> getEmployees() {
		return employees;
	}
	public void setEmployees(HashMap<String, String> employees) {
		this.employees = employees;
	}
	public HashMap<String, String> getTasks() {
		return tasks;
	}
	public void setTasks(HashMap<String, String> tasks) {
		this.tasks = tasks;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;	
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
