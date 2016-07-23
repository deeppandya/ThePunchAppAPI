package Util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Helper {

	public static boolean isBrowser(String userAgent){
		String  user = userAgent.toLowerCase();
        String os = "";
        String browser = "";
        boolean result = false;
        //os
        /*if (userAgent.toLowerCase().indexOf("windows") >= 0 )
        {
        	os = "Windows";
        } else if(userAgent.toLowerCase().indexOf("mac") >= 0)
        {
        	os = "Mac";
        } else if(userAgent.toLowerCase().indexOf("x11") >= 0)
        {
        	os = "Unix";
        } else if(userAgent.toLowerCase().indexOf("android") >= 0)
        {
        	os = "Android";
        } else if(userAgent.toLowerCase().indexOf("iphone") >= 0)
        {
        	os = "IPhone";
        }else{
        	os = "UnKnown, More-Info: "+userAgent;
        }*/
        //Browser
        if (user.contains("msie"))
        {	
        	String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
        	browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
        	result = true;
        } else if (user.contains("safari") && user.contains("version"))
        {
        	browser=(userAgent.substring(userAgent.indexOf("Safari"))
        			.split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version"))
        					.split(" ")[0]).split("/")[1];
        	result = true;
        } else if ( user.contains("opr") || user.contains("opera"))
        {
        	if(user.contains("opera")){
        		browser=(userAgent.substring(userAgent.indexOf("Opera"))
        				.split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version"))
        						.split(" ")[0]).split("/")[1];
        		result = true;
        	}
        	else if(user.contains("opr")){
        		browser=((userAgent.substring(userAgent.indexOf("OPR"))
        				.split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
        		result = true;
        	}
        } else if (user.contains("chrome"))
        {
        	browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        	result = true;
        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) )
        {
        	//browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
        	browser = "Netscape-?";
        	result = true;
        } else if (user.contains("firefox"))
        {
        	browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        	result = true;
        } else if(user.contains("rv"))
        {
        	browser="IE";
        	result = true;
        } else
        {
        	browser = "UnKnown, More-Info: "+userAgent;
        }
        //System.out.println(os);
        //System.out.println(browser);
        return result;
	}
	
	public static String md5(String input) {
        
        String md5 = null;
         
        if(null == input) 
        	return null;
         
        try {
             
        //Create MessageDigest object for MD5
        MessageDigest digest = MessageDigest.getInstance("MD5");
         
        //Update input string in message digest
        digest.update(input.getBytes(), 0, input.length());
 
        //Converts message digest value in base 16 (hex) 
        md5 = new BigInteger(1, digest.digest()).toString(16);
 
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }
	

}
