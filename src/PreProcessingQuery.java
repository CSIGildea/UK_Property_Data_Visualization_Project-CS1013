package project10;

import processing.core.PApplet;
import de.bezier.data.sql.*;

// SH - This class calculates values from the full dataset, only used for pre-processing
public class PreProcessingQuery {
	
	PApplet parent;
	String server = "localhost";
	String database = "sys";
	String table = "fulldataset";
	String username = "group10";
	String password = "group10";
	MySQL connection;
	
	PreProcessingQuery (PApplet parent)
	{
		this.parent = parent;
		connection =  new MySQL(parent, server, database, username, password);
		connection.connect();
	}
	
	public int getAvgPrice(int year, String county, String propertyType)
	{
		String query = "SELECT AVG(price) FROM " + table + " WHERE yearofsale = " + year
														 + "  AND  county = '" + county + "'"
														 + "  AND  propertytype = '" + propertyType + "'";
		connection.query(query);
		while (connection.next())
			return (connection.getInt("AVG(price)"));
		System.out.println("error at getAvgPrice");
		return 0;
	}
	
	public int getNumTransactions(int year, String county, String propertyType)
	{
		int transactions = 0;
		String query = "SELECT price FROM " + table + " WHERE yearofsale = " + year
													+ "  AND  county = '" + county + "'"
													+ "  AND  propertytype = '" + propertyType + "'";
		connection.query(query);
		while (connection.next())
			transactions++;
		return transactions;
	}
	
	public int getMaxPrice(int year, String county, String propertyType)
	{
		String query = "SELECT MAX(price) FROM " + table + " WHERE yearofsale = " + year
													     + "  AND  county = '" + county + "'"
													     + "  AND  propertytype = '" + propertyType + "'";
		connection.query(query);
		while (connection.next())
			return connection.getInt("MAX(price)");
		System.out.println("error at getMaxPrice");
		return 0;
	}
} 