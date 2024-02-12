package utils;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

public class sqlFile {
	static Logger logger = new CommonLogger(sqlFile.class).getLogger();

	
	public static void append(String statement) {
		String filePath = "/home/gokul-zstk330/Zoho WorkDrive (Enterprise)/My Folders/ZS/eclipse-workspace/zbi-data.sql";
		String query = statement.substring(statement.indexOf(":") + 1).trim();
		try {
            FileWriter writer = new FileWriter(filePath, true); // true parameter for appending
            writer.write(query+"\n");
            writer.close();
        } catch (IOException e) {
            logger.error("Can't append to sql file: "+ query);
        }

	}
	
	
	public static void main(String[] args) {
		
		String hashPw = "$2a$10$tPgtYSwH1.yFuyMao8wJD.JZtmwCcIFpP964Zw1fycvR8hapNpFxa";
		System.out.println(BCrypt.checkpw("Hermione", hashPw));
	}
}
