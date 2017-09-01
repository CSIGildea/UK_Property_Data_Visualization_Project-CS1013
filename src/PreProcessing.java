package project10;

import processing.core.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

public class PreProcessing extends PApplet
{
	PreProcessingQuery query;
	Connection connection;
	ArrayList<String> countiesList = new ArrayList<String>();
	public LinkedHashMap<String, Double> latitude = new LinkedHashMap<String, Double>();
	public LinkedHashMap<String, Double> longitude = new LinkedHashMap<String, Double>();
	double[] englandBoxes;
	
	public static void main(String[] args)
	{
		PApplet.main("project10.PreProcessing");
	}
	
	public void setup()
	{
		query = new PreProcessingQuery(this);
		try
		{
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "group10", "group10");
		} 
		catch (SQLException error)
		{
			System.out.println(error);
		}
		catch (ClassNotFoundException error)
		{
			System.out.println(error);
		}
		
		// Generate list of counties
		query.connection.query("SELECT DISTINCT county FROM fulldataset");
		while (query.connection.next())
			countiesList.add(query.connection.getString("county"));
		//System.out.println(countiesList.size());
		//for (int i = 0; i < countiesList.size(); i++)
			//System.out.println(countiesList.get(i) + ":" + i);
		
		
		// NB DO NOT CALL THESE AGAIN
		//prepareData("D"); // Detached		 - DONE
		//prepareData("S"); // Semi-Detached - DONE
		//prepareData("T"); // Terraced		 - DONE
		//prepareData("F"); // Flat			 - DONE
		//prepareData("O"); // Other		 - DONE
		
		// Generate hashmaps for county/latitude and county/longitude
		try{
			putGPSLatitudes(new File("data/files/englandGPSLatitude.txt"));
			putGPSLatitudes(new File("data/files/walesGPSLatitude.txt"));
			putGPSLongitudes(new File("data/files/englandGPSLongitude.txt"));
			putGPSLongitudes(new File("data/files/walesGPSLongitude.txt"));
		}
		catch (FileNotFoundException error){}
		
		// NB DO NOT CALL THESE AGAIN
		//insertGPSLatitudes(); 	- DONE
		//insertGPSLongitudes();	- DONE
	}
	
	// generate table with average price, max price and number of sales, for every county, for every year, for every property type
	public void prepareData(String propertyType)
	{
		for (int i = 1995; i <= 2017; i++)
		{
			for (int j = 0; j < countiesList.size(); j++)
			{
				int average = query.getAvgPrice(i, countiesList.get(j), propertyType);
				int max = query.getMaxPrice(i, countiesList.get(j), propertyType);
				int num = query.getNumTransactions(i, countiesList.get(j), propertyType);
				
				String query1 = "INSERT INTO sys.prepareddata (county,yearofsale,propertytype,averageprice,maxprice,numsales)"
						+ "VALUES('" + countiesList.get(j) + "','" + i + "','" + propertyType + "','"
						+ average + "','" + max + "','" + num + "')";
				try
				{
					PreparedStatement prepStatement = connection.prepareStatement(query1);
					prepStatement.executeUpdate();
				}
				catch (SQLException error)
				{}
				System.out.println(propertyType + ":" + i + ":" + j);
			}
		}
	}
	
	// populate latitude hashmap
	public void putGPSLatitudes(File file) throws FileNotFoundException
	{
		Scanner textFile = new Scanner(file);
		int length = textFile.nextInt();
		textFile.nextLine();
		for (int i = 0; i < length; i++)
		{
			String[] line = textFile.nextLine().split(",");
			latitude.put(line[0], Double.parseDouble(line[1]));
		}
		textFile.close();
	}
	
	// populate longitude hashmap
	public void putGPSLongitudes(File file) throws FileNotFoundException
	{
		Scanner textFile = new Scanner(file);
		int length = textFile.nextInt();
		textFile.nextLine();
		for (int i = 0; i < length; i++)
		{
			String[] line = textFile.nextLine().split(",");
			longitude.put(line[0], Double.parseDouble(line[1]));
		}
		textFile.close();
	}
	
	// send latitude values to SQL table
	public void insertGPSLatitudes()
	{
		Iterator<Entry<String, Double>> it = latitude.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Double> pair = (Map.Entry<String, Double>) it.next();
			
			String query1 = "UPDATE sys.prepareddata SET latitude = '" + pair.getValue()
												+ "' WHERE county = '" + pair.getKey() + "';";
			try
			{
				PreparedStatement prepStatement = connection.prepareStatement(query1);
				prepStatement.executeUpdate();
			}
			catch (SQLException error){}
		}
		System.out.println("Finished");
	}
	
	// send longitude values to SQL table
	public void insertGPSLongitudes()
	{
		Iterator<Entry<String, Double>> it = longitude.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Double> pair = (Map.Entry<String, Double>) it.next();
			
			String query1 = "UPDATE sys.prepareddata SET longitude = '" + pair.getValue()
												+ "' WHERE county = '" + pair.getKey() + "';";
			try
			{
				PreparedStatement prepStatement = connection.prepareStatement(query1);
				prepStatement.executeUpdate();
			}
			catch (SQLException error){}
		}
		System.out.println("Finished");
	}
}
