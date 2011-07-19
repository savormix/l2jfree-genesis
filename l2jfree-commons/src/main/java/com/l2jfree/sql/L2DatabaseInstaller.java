package com.l2jfree.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.l2jfree.lang.L2TextBuilder;
import com.l2jfree.util.L2XML;

/**
 * @author NB4L1
 */
// FIXME: error management, rollback, etc
public final class L2DatabaseInstaller
{
	public static void check() throws SAXException, IOException, ParserConfigurationException
	{
		final TreeMap<String, String> tables = new TreeMap<String, String>();
		final TreeMap<Double, String> updates = new TreeMap<Double, String>();
		
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false); // FIXME: add validation
		factory.setIgnoringComments(true);
		
		final Document doc = factory.newDocumentBuilder().parse(
				L2DatabaseInstaller.class.getResourceAsStream("database_schema.xml"));
		
		for (Node n1 : L2XML.listNodesByNodeName(doc, "database"))
		{
			for (Node n2 : L2XML.listNodesByNodeName(n1, "table"))
			{
				final String name = L2XML.getAttribute(n2, "name");
				final String definition = L2XML.getAttribute(n2, "definition");
				
				tables.put(name, definition);
			}
			
			for (Node n2 : L2XML.listNodesByNodeName(n1, "update"))
			{
				final Double revision = Double.valueOf(L2XML.getAttribute(n2, "revision"));
				final String query = L2XML.getAttribute(n2, "query");
				
				updates.put(revision, query);
			}
		}
		
		createRevisionTable();
		
		final double databaseRevision = getDatabaseRevision();
		
		if (databaseRevision == -1) // no table exists
		{
			for (Entry<String, String> table : tables.entrySet())
			{
				final String tableName = table.getKey();
				final String tableDefinition = table.getValue();
				
				installTable(tableName, tableDefinition);
			}
			
			if (updates.isEmpty())
				insertRevision(0);
			else
				insertRevision(updates.lastKey());
		}
		else // check for possibly required updates
		{
			// FIXME: check for non-existing tables and install if necessary
			
			for (Entry<Double, String> update : updates.entrySet())
			{
				final double updateRevision = update.getKey();
				final String updateQuery = update.getValue();
				
				if (updateRevision > databaseRevision)
				{
					executeUpdate(updateQuery);
					
					insertRevision(updateRevision);
				}
			}
		}
	}
	
	private static void installTable(String tableName, String tableDefinition)
	{
		System.out.println("Installing table '" + tableName + "'.");
		
		Connection con = null;
		try
		{
			con = L2Database.getConnection();
			
			PreparedStatement ps = con.prepareStatement(tableDefinition);
			ps.execute();
			ps.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			L2Database.close(con);
		}
		
		System.out.println("Done.");
	}
	
	private static void executeUpdate(String updateQuery)
	{
		System.out.println("Executing update '" + updateQuery + "'.");
		
		Connection con = null;
		try
		{
			con = L2Database.getConnection();
			
			PreparedStatement ps = con.prepareStatement(updateQuery);
			ps.execute();
			ps.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			L2Database.close(con);
		}
		
		System.out.println("Done.");
	}
	
	private static void insertRevision(double revision)
	{
		System.out.println("Saving revision '" + revision + "'.");
		
		Connection con = null;
		try
		{
			con = L2Database.getConnection();
			
			PreparedStatement ps = con.prepareStatement("INSERT INTO _revision VALUES (?,?)");
			ps.setDouble(1, revision);
			ps.setLong(2, System.currentTimeMillis());
			ps.execute();
			ps.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			L2Database.close(con);
		}
		
		System.out.println("Done.");
	}
	
	private static void createRevisionTable()
	{
		System.out.println("Creating revision table.");
		
		Connection con = null;
		try
		{
			con = L2Database.getConnection();
			
			L2TextBuilder tb = L2TextBuilder.newInstance();
			tb.append("CREATE TABLE IF NOT EXISTS _revision ("); // FIXME: non-standard SQL
			tb.append("  revision DECIMAL NOT NULL,");
			tb.append("  date BIGINT NOT NULL,");
			tb.append("  PRIMARY KEY (revision)");
			tb.append(") DEFAULT CHARSET=utf8;");
			
			PreparedStatement ps = con.prepareStatement(tb.moveToString());
			ps.execute();
			ps.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			L2Database.close(con);
		}
		
		System.out.println("Done.");
	}
	
	private static double getDatabaseRevision()
	{
		double revision = -1;
		
		Connection con = null;
		try
		{
			con = L2Database.getConnection();
			
			// FIXME: non-standard SQL
			PreparedStatement ps = con.prepareStatement("SELECT revision FROM _revision ORDER BY revision DESC LIMIT 1");
			ResultSet rs = ps.executeQuery();
			
			if (rs.next())
				revision = rs.getDouble(1);
			
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			L2Database.close(con);
		}
		
		return revision;
	}
}
