package com.thiagosil.timeout;

import java.util.Date;

public class Shift {

	private Date checkIn, checkOut;
	private int id;
	
	public Shift()
	{
		
	}
	
	public Shift(int id, Date checkIn, Date checkOut) {
		this.id = id;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
	}
	
	public enum Status {
	   CHECKED_IN, CHECKED_OUT
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public Date getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(Date checkIn) {
		this.checkIn = checkIn;
	}
	
	public void setCheckIn(Long checkIn){
		this.checkIn = new Date(checkIn);
	}

	public Date getCheckOut() {
		return checkOut;
	}

	public void setCheckOut(Date checkOut) {
		this.checkOut = checkOut;
	}
	
	public void setCheckOut(Long checkOut) {
		this.checkOut =  new Date(checkOut);
	}
	
	public Status getStatus() {
		if (checkIn != null) {
			return Shift.Status.CHECKED_IN;
		}
		else {
			return Shift.Status.CHECKED_OUT;
		}
	}
}
