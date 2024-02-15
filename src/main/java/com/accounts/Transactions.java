package com.accounts;

import java.time.LocalDate;

public class Transactions {
	String Transaction_id;
	LocalDate Transaction_date;
	String Description;
	double Cr_amount;
	double Dr_amount;
	double Net_Balance;
	
	public Transactions(String id, LocalDate date, String desc, double cr, double dr, double netbal) {
		this.Transaction_date = date;
		this.Transaction_id = id;
		this.Cr_amount = cr;
		this.Dr_amount = dr;
		this.Net_Balance = netbal;
		this.Description = desc;
	}

	public String getTransaction_id() {
		return Transaction_id;
	}

	public LocalDate getTransaction_date() {
		return Transaction_date;
	}

	public String getDescription() {
		return Description;
	}

	public double getCr_amount() {
		return Cr_amount;
	}

	public double getDr_amount() {
		return Dr_amount;
	}

	public double getNet_Balance() {
		return Net_Balance;
	}

	public void setTransaction_id(String transaction_id) {
		Transaction_id = transaction_id;
	}

	public void setTransaction_date(LocalDate transaction_date) {
		Transaction_date = transaction_date;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public void setCr_amount(double cr_amount) {
		Cr_amount = cr_amount;
	}

	public void setDr_amount(double dr_amount) {
		Dr_amount = dr_amount;
	}

	public void setNet_Balance(double net_Balance) {
		Net_Balance = net_Balance;
	}
	
	
	
}
