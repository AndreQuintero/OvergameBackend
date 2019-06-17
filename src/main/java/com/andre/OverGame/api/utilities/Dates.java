package com.andre.OverGame.api.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dates {

	// retorna uma data no formato dd-MM-yyyy
	public String DateToString(Date date) {
		
		DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
		
		String strDate = dateFormat.format(date);
		
		return strDate;
	}
	
	// recebe uma data dd-mm-yyyy e devolve um util.date
	public Date stringToDate(String strDate) {
		
		DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
		
		Date date = null;
		try {
			date = (Date) dateFormat.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return date;
	}
}
