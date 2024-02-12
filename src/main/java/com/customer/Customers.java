package com.customer;
import java.time.LocalDate;
import org.apache.log4j.Logger;
import utils.CommonLogger;

public class Customers {
    private String cusID;
    private String firstName;
    private String lastName;
    private LocalDate DOB;
    private String email;
    private String phone;
    private String address;

	static Logger logger = new CommonLogger(Customers.class).getLogger();
	
    // Constructor
    public Customers(String cusID, String firstName, String lastName, String DOB, String email, String phone, String address) {    	
    	this.cusID = cusID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.DOB = LocalDate.parse(DOB);
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    // Getter methods
    public String getCusID() {
        return cusID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDOB() {
        return DOB;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
}

