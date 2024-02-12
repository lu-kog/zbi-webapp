package utils;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class Generator {
	static Logger logger = new CommonLogger(Generator.class).getLogger();
	
	public static String Numerical(int len) {
		String outString = "";
        for (int i = 0; i < len; i++) {
            outString += (int) (Math.random() * 10);
        }
        return outString;

	}
	
	public static String ValidNumerical(int len, String table, String column) {

		String genString = Numerical(len);
		if (DB.checkValueisExist(genString, table, column)) {
			return ValidNumerical(len, table, column);
		}
		else {
			return genString;
		}

	}

	public static String createUUID(String table, String column) {

		String uid = UUID.randomUUID().toString();
		if (DB.checkValueisExist(uid, table, column)) {
			return createUUID(table, column);
		}

		return uid;
	}
	
	
}
