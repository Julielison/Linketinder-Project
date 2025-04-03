package org.linketinder.util


import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Util {
	static convertToLocalDate(String dateStr, String format){
		DateTimeFormatter formatDate = DateTimeFormatter.ofPattern(format)
		return LocalDate.parse(dateStr, formatDate)
	}
	static String convertToBrFormat(LocalDate data) {
		if (data == null) return ""
		return data.format(DateTimeFormatter.ofPattern('dd/MM/yyyy'))
	}
}