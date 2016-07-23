package constants;

final public class Constants {
	public static final String HOST = "159.203.42.206";
	public static final int TOMCAT_PORT = 8080;
	public static final int MONGO_PORT = 27017;
	public static final String DATABASENAME = "punchdb";
	public static final String COOKIE_DOMAIN = "";	//it will identify domain automatically
	public static final String COOKIE_PATH = "/";	//for all the api paths
	public static final int COOKIE_AGE = -1;		//default which is equal to the session of browser, age is in sec
}
