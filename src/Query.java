package project10;

import processing.core.PApplet;
import de.bezier.data.sql.*;
import java.text.DecimalFormat;
import org.apache.commons.lang3.text.WordUtils;
import java.util.ArrayList;
import com.jcraft.jsch.*;

public class Query {
	PApplet parent;
	String server = "localhost";
	String database = "sys";
	String username = "group10";
	String password = "group10";
	MySQL connection;
	
	// SH - connects to SQL database
	Query (PApplet parent, boolean local)
	{
		this.parent = parent;
		
		if (local)
		{
			System.out.println("Connecting local...");
			connection =  new MySQL(parent, server, database, username, password);
			connection.connect();
		}
		// Credit to Michael McAndrew, Group 12, for the remote access code and server
		else
		{
			System.out.println("Connecting remote...");
			String sshUsername = "***";
			String sshPassword = "***";
			String sshHost = "***";
			int localport = ***;
			String localURL = "localhost";
			int remoteport = 3306;
			
			server = "localhost" + ":" + localport;
			database = "***";
			username = "***";
			password = "***";
			try
			{
				java.util.Properties config = new java.util.Properties();
				config.put("StrictHostKeyChecking", "no");
				JSch jsch = new JSch();
				Session session = jsch.getSession(sshUsername, sshHost, 22);
				session.setPassword(sshPassword);
				session.setConfig(config);
				session.connect();
				session.setPortForwardingL(localport, localURL, remoteport);
			}
			catch (Exception error){System.out.println("Error at SSH");}
			
			connection =  new MySQL(parent, server, database, username, password);
			connection.connect();
		}
	}
	
	// SH - get average for all property types
	public int getAvgPrice(String table, int year, String county)
	{
		String query = "SELECT averageprice FROM " + table + " WHERE yearofsale = " + year
														   + "  AND  county = '" + county + "';";
		connection.query(query);
		while (connection.next())
			return (connection.getInt("averageprice"));
		System.out.println("error at getAvgPrice");
		return 0;
	}
	
	// SH - get average for a specific property type
	public int getAvgPrice(String table, int year, String county, String propertyType)
	{
		String query = "SELECT averageprice FROM " + table + " WHERE yearofsale = " + year
														   + "  AND  county = '" + county + "'"
														   + "  AND  propertytype = '" + propertyType + "';";
		connection.query(query);
		while (connection.next())
			return (connection.getInt("averageprice"));
		System.out.println("error at getAvgPrice");
		return 0;
	}

	// SH - get numSales for all property types
	public int getNumSales(String table, int year, String county)
	{
		String query = "SELECT numsales FROM " + table + " WHERE yearofsale = " + year
													   + "  AND  county = '" + county + "';";
		connection.query(query);
		while (connection.next())
			return connection.getInt("numsales");
		System.out.println("error at getNumSales");
		return 0;
	}
	
	// SH - get numSales for a specific property type
	public int getNumSales(String table, int year, String county, String propertyType)
	{
		String query = "SELECT numsales FROM " + table + " WHERE yearofsale = " + year
													   + "  AND  county = '" + county + "'"
													   + "  AND  propertytype = '" + propertyType + "';";
		connection.query(query);
		while (connection.next())
			return connection.getInt("numsales");
		System.out.println("error at getNumSales");
		return 0;
	}
	
	// SH - get max for all property types
	public int getMaxPrice(String table, int year, String county)
	{
		String query = "SELECT maxprice FROM " + table + " WHERE yearofsale = " + year
													   + "  AND  county = '" + county + "';";
		connection.query(query);
		while (connection.next())
			return connection.getInt("maxprice");
		System.out.println("error at getMaxPrice");
		return 0;
	}
	
	// SH - get max for a specific property type
	public int getMaxPrice(String table, int year, String county, String propertyType)
	{
		String query = "SELECT maxprice FROM " + table + " WHERE yearofsale = " + year
													   + "  AND  county = '" + county + "'"
													   + "  AND  propertytype = '" + propertyType + "';";
		connection.query(query);
		while (connection.next())
			return connection.getInt("maxprice");
		System.out.println("error at getMaxPrice");
		return 0;
	}
	
	// SH - retrieve latitude and longitude values for a county
	public String[] getGPS(String table, String county)
	{
		String[] GPS = new String[2];
		String query = "SELECT latitude,longitude FROM " + table + " WHERE county = '" + county + "';";
		connection.query(query);
		while (connection.next())
		{
			GPS[0] = connection.getString("latitude");
			GPS[1] = connection.getString("longitude");
		}
		return GPS;
	}
	
	// SH - takes a postcode and returns all info for that postcode
	public ArrayList<String[]> getPostcodeInfo(String table, String userInput)
	{
		ArrayList<String[]> results = new ArrayList<String[]>();
		String fullAddress;
		String address1;
		String address2;
		String address3;
		String address4;
		String address5;
		String address6;
		String address7;
		
		int intPrice;
		String price;
		String dateofsale;
		String property;
		String oldnew;
		String ownership;
		
		String query = "SELECT * from " + table + " WHERE postcode = '" + userInput + "'";
		connection.query(query);
		while (connection.next())
		{
			address1 = connection.getString("PAON");
			address2 = connection.getString("SAON");
			address3 = connection.getString("street");
			address4 = connection.getString("locality");
			address5 = connection.getString("town");
			address6 = connection.getString("district");
			address7 = connection.getString("county");
			
			fullAddress = (!address1.equals("") ? address1 + ", " : "") + (!address2.equals("") ? address2 + ", " : "")
					+ (!address3.equals("") ? address3 + ", " : "") + (!address4.equals("") ? address4 + ", " : "")
					+ "\n" + (!address5.equals("") ? address5 + ", " : "") + (!address6.equals("") ? address6 + ", " : "")
					+ (!address7.equals("") ? address7 : "");
			fullAddress = WordUtils.capitalizeFully(fullAddress, ' ', '-', '\n');
			
			intPrice = connection.getInt("price");
			dateofsale = connection.getString("dateofsale");
			property = connection.getString("propertytype");
			oldnew = connection.getString("oldnew");
			ownership = connection.getString("duration");
			
			DecimalFormat formatter = new DecimalFormat("#,###");
			price = formatter.format(intPrice);
			property = (property.equals("D") ? "Detached House" : property.equals("S") ? "Semi-Detached House"
					: property.equals("T") ? "Terraced House" : property.equals("F") ? "Flat/Maisonette"
							: property.equals("O") ? "Other" : "");
			oldnew = (oldnew.equals("Y") ? oldnew = "Newly Built" : oldnew.equals("N") ? "Established Property" : "");
			ownership = (ownership.equals("F") ? "Freehold" : ownership.equals("L") ? "Leasehold" : "");
			
			String[] temp = {fullAddress, dateofsale.substring(0, 10), price, property, oldnew, ownership};
			results.add(temp);
		}
		return results;
	}
}