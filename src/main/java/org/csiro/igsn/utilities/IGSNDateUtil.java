package org.csiro.igsn.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class IGSNDateUtil {
	
	public static SimpleDateFormat getISODateFormatter(){
		return new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ssXXX");
	}
	
	public static SimpleDateFormat getXMLGregorianDateFormatter(){
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	}
	
	public static SimpleDateFormat getISODateFormatterShort(){
		return new SimpleDateFormat("yyyy-MM-dd");
		
		
	}

	public static XMLGregorianCalendar parseForGregorianCalendar(String w3cDate) throws NumberFormatException, DatatypeConfigurationException {
		String year = "";
		String month="00", day = "01";
		String  hour="00", minute = "00";
		if (w3cDate.length() >= 4) {
			year = w3cDate.substring(0, 4);
		}

		if (w3cDate.length() >= 7) {
			month = w3cDate.substring(5,7);
		}

		if (w3cDate.length() >= 10) {
			day = w3cDate.substring(8,10);
		}
		if (w3cDate.length() >= 13) {
			hour = w3cDate.substring(11,13);
		}
		if (w3cDate.length() >= 15) {
			minute = w3cDate.substring(14,16);
		}
		
		GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(year),Integer.parseInt(month),
				Integer.parseInt(day),Integer.parseInt(hour),Integer.parseInt(minute),0);
		
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);

	}

}
