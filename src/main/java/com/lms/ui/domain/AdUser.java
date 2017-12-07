package com.lms.ui.domain;

import com.univocity.parsers.annotations.Parsed;

public class AdUser {
	@Parsed(field = "First Name",defaultNullRead="")
	private String firstName;
    @Parsed(field = "Last Name",defaultNullRead="")
	private String lastName;
    @Parsed(field = "Full Name",defaultNullRead="")
    private String FullName;
	@Parsed(field = "Company email",defaultNullRead="")
	private String emailId;
	@Parsed(field = "Start Date") //
	private String hireDate;
	@Parsed(field = "Employee Type",defaultNullRead="") //
	private String employeeType;
	@Parsed(field = "Departments",defaultNullRead="AppDirect Default Department")
	private String department;
	@Parsed(field = "Job Title",defaultNullRead="")
	private String jobTitle;
	@Parsed(field = "Division",defaultNullRead="AppDirect Default Division")
	private String division;
	@Parsed(field = "Reports To Full Name",defaultNullRead="")
	private String supervisorName;
	@Parsed(field = "Reports To Email",defaultNullRead="")
	private String supervisorEmail;
	@Parsed(field = "Office City",defaultNullRead="")
	private String city;
	@Parsed(field = "Office Country",defaultNullRead="")
	private String country;
	@Parsed(field = "Office Location Address 1",defaultNullRead="")
	private String address1;
	@Parsed(field = "Office Location Address 2",defaultNullRead="")
	private String address2;
	@Parsed(field = "Office Location Zip",defaultNullRead="")
	private String zipCode;
	@Parsed(field = "User status",defaultNullRead="")
	private String userStatus;
	@Parsed(field = "Subsidiary Company",defaultNullRead="") 
	private String subsidiaryCompany;
	
	public String getZipCode() {
		if(zipCode != null){
			return zipCode.trim();
		}
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getFirstName() {
		if(firstName != null){
			return firstName.trim().replaceAll(" +", " ");
		}
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		if(lastName != null){
			return lastName.trim().replaceAll(" +", " ");
		}
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailId() {
		if(emailId != null){
			return emailId.trim().replaceAll(" +", " ");
		}
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getHireDate() {
		if(hireDate != null){
			return hireDate.trim().replaceAll(" +", " ");
		}
		return hireDate;
	}
	public void setHireDate(String hireDate) {
		this.hireDate = hireDate;
	}
	public String getEmployeeType() {
		if(employeeType != null){
			return employeeType.trim().replaceAll(" +", " ");
		}
		return employeeType;
	}
	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}
	public String getDepartment() {
		if(department != null){
			return department.trim().replaceAll(" +", " ");
		}
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getJobTitle() {
		if(jobTitle != null){
			return jobTitle.trim().replaceAll(" +", " ");
		}
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getDivision() {
		if(division != null){
			return division.trim().replaceAll(" +", " ");
		}
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getSupervisorName() {
		if(supervisorName != null){
			return supervisorName.trim().replaceAll(" +", " ");
		}
		return supervisorName;
	}
	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}
	public String getSupervisorEmail() {
		if(supervisorEmail != null){
			return supervisorEmail.trim().replaceAll(" +", " ");
		}
		return supervisorEmail;
	}
	public void setSupervisorEmail(String supervisorEmail) {
		this.supervisorEmail = supervisorEmail;
	}
	public String getCity() {
		if(city != null){
			return city.trim().replaceAll(" +", " ");
		}
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		if(country != null){
			return country.trim().replaceAll(" +", " ");
		}
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getAddress1() {
		if(address1 != null){
			return address1.trim().replaceAll(" +", " ");
		}
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		if(address2 != null){
			return address2.trim().replaceAll(" +", " ");
		}
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getUserStatus() {
		if(userStatus != null){
			return userStatus.trim().replaceAll(" +", " ");
		}
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getSubsidiaryCompany() {
		if(subsidiaryCompany != null){
			return subsidiaryCompany.trim().replaceAll(" +", " ");
		}
		return subsidiaryCompany;
	}
	public void setSubsidiaryCompany(String subsidiaryCompany) {
		this.subsidiaryCompany = subsidiaryCompany;
	}
}
