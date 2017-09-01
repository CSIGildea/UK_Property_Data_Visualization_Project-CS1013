package project10;
import java.util.*;
/**
 * @author Catalina
 *
 */
public class Maps {
	public LinkedHashMap<String, Boolean> countries = new LinkedHashMap<String, Boolean>();
	public LinkedHashMap<String,Boolean> england = new LinkedHashMap<String,Boolean>();
	public LinkedHashMap<String,Boolean> wales = new LinkedHashMap<String,Boolean>();
	public LinkedHashMap<String,Boolean> analysis=new LinkedHashMap<String,Boolean>();
	public LinkedHashMap<String,Boolean> propertyTypes=new LinkedHashMap<String,Boolean>();
	public void populateProperties(){
		propertyTypes.put("All",true);
		propertyTypes.put("Detached",false);
		propertyTypes.put("Semi-detached",false);
		propertyTypes.put("Terraced", false);
		propertyTypes.put("Flats/Maisonettes", false);
		propertyTypes.put("Other", false);
	}
	public void populateTypes(){
		analysis.put("Average price",true);
		analysis.put("Maximum price",false);
		analysis.put("Number of transactions",false);
		analysis.put("Sales by type",false);
	}
	
	public void  populateCountries(){
		countries.put("England", false);
		countries.put("Wales",false);
	}
	
	public void populateCounties(String[] englandCounties,String[] walesCounties){
		for(int i = 0;i<englandCounties.length;i++){
			england.put(englandCounties[i],false);
		}
		for(int i = 0;i<walesCounties.length;i++){
			wales.put(walesCounties[i],false);
		}
		
	}
}
