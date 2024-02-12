package com.accounts;

import java.time.LocalDate;
import java.util.List;

import com.customer.Customers;

public class Account {
	String accountNumber;
	Customers customer;
	String ifsc;
	String account_type;
	double balance;
	LocalDate openedDate;
	boolean status;
	public Account(String accountNumber, String ifsc, String account_type, double balance, LocalDate openedDate,
			boolean status) {
		
		this.accountNumber = accountNumber;
		this.ifsc = ifsc;
		this.account_type = account_type;
		this.balance = balance;
		this.openedDate = openedDate;
		this.status = status;
	}
	
	
//	getters
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public Customers getCustomer() {
		return customer;
	}
	public String getIfsc() {
		return ifsc;
	}
	public String getAccount_type() {
		return account_type;
	}
	public double getBalance() {
		return balance;
	}
	public LocalDate getOpenedDate() {
		return openedDate;
	}
	public boolean isStatus() {
		return status;
	}
	
	// setters
	
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public void setCustomer(Customers customer) {
		this.customer = customer;
	}
	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}
	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public void setOpenedDate(LocalDate openedDate) {
		this.openedDate = openedDate;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
//	List<Card> Cards;
//	List<Loan> Loans;
	
	
}
