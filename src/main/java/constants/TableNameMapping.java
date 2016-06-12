package constants;

import java.util.HashMap;

import schema.CompanySchema;

final public class TableNameMapping {
	public static final HashMap<Class, String> CLASS_TO_TABLENAME_MAPPING = new HashMap<Class, String>();
	
	//initialize class to tablename map
	static{
		CLASS_TO_TABLENAME_MAPPING.put(CompanySchema.class, "company");
		
	}
}
