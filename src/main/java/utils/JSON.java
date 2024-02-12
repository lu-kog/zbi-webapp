package utils;

import java.net.HttpCookie;
import java.net.HttpURLConnection;
import javax.net.ServerSocketFactory;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.*;


public class JSON {

	static Logger logger = new CommonLogger(JSON.class).getLogger();
	
	
	
	public static void main(String[] args) throws JSONException {
		// TODO Auto-generated method stub
		int age = 19;
		String teString = "{\"name\":\"krish\", \"age\":"+age+"}";
		JSONObject obj=new JSONObject(teString);  

		System.out.println(obj);
	}
	
	public static JSONObject CreateErrorJson(int code, String msg) {

		JSONObject errorObj = new JSONObject();
		try {
			errorObj.put("statuscode", code);
			errorObj.put("error", msg);
		} catch (JSONException e) {
			logger.error("Json Exception on creating new errorObject");
		}
		
		return errorObj;
	}
		

}
