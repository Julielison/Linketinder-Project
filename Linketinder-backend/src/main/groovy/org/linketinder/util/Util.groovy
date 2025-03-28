package org.linketinder.util

import java.text.SimpleDateFormat

class Util {
	static String formatarData(Date data, String formato) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(formato)
		return dateFormat.format(data)
	}
}