/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jfree.sql;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.l2jfree.lang.L2TextBuilder;
import com.l2jfree.util.L2XML;

/**
 * @author NB4L1
 */
// FIXME error management, rollback, etc
// TODO avoid duplication of xml schema files
public final class L2DatabaseInstaller
{
	public static void check() throws SAXException, IOException, ParserConfigurationException
	{
		final TreeMap<String, String> tables = new TreeMap<String, String>();
		final TreeMap<Double, String> updates = new TreeMap<Double, String>();
		
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false); // FIXME add validation
		factory.setIgnoringComments(true);
		
		final List<Document> documents = new ArrayList<Document>();
		
		InputStream is = null;
		try
		{
			// load default database schema from resources
			is = L2DatabaseInstaller.class.getResourceAsStream("database_schema.xml");
			
			documents.add(factory.newDocumentBuilder().parse(is));
		}
		finally
		{
			IOUtils.closeQuietly(is);
		}
		
		final File f = new File("./config/database_schema.xml");
		
		// load optional project specific database tables/updates (fails on already existing)
		if (f.exists())
			documents.add(factory.newDocumentBuilder().parse(f));
		
		for (Document doc : documents)
		{
			for (Node n1 : L2XML.listNodesByNodeName(doc, "database"))
			{
				for (Node n2 : L2XML.listNodesByNodeName(n1, "table"))
				{
					final String name = L2XML.getAttribute(n2, "name");
					final String definition = L2XML.getAttribute(n2, "definition");
					
					final String oldDefinition = tables.put(name, definition);
					if (oldDefinition != null)
						System.err.println("Found multiple tables with name " + name + "!");
				}
				
				for (Node n2 : L2XML.listNodesByNodeName(n1, "update"))
				{
					final Double revision = Double.valueOf(L2XML.getAttribute(n2, "revision"));
					final String query = L2XML.getAttribute(n2, "query");
					
					final String oldQuery = updates.put(revision, query);
					if (oldQuery != null)
						System.err.println("Found multiple updates with revision " + revision + "!");
				}
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
		else
		// check for possibly required updates
		{
			for (Entry<String, String> table : tables.entrySet())
			{
				final String tableName = table.getKey();
				final String tableDefinition = table.getValue();
				
				if (L2Database.tableExists(tableName))
					continue;
				
				System.err.println("Table '" + tableName + "' is missing, so the server attempts to install it.");
				System.err.println("WARNING! It's highly recommended to check the results manually.");
				
				installTable(tableName, tableDefinition);
			}
			
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
			ps.executeUpdate();
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
			ps.executeUpdate();
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
			ps.executeUpdate();
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
		System.out.println("Checking revision table.");
		
		if (L2Database.tableExists("_revision"))
		{
			System.out.println("Already exists.");
			return;
		}
		
		System.out.println("Creating revision table.");
		
		Connection con = null;
		try
		{
			con = L2Database.getConnection();
			
			L2TextBuilder tb = new L2TextBuilder();
			tb.append("CREATE TABLE _revision (");
			tb.append("  revision DECIMAL NOT NULL,");
			tb.append("  date BIGINT NOT NULL,");
			tb.append("  PRIMARY KEY (revision)");
			tb.append(')');
			
			PreparedStatement ps = con.prepareStatement(tb.moveToString());
			ps.executeUpdate();
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
			
			// FIXME non-standard SQL
			PreparedStatement ps =
					con.prepareStatement("SELECT revision FROM _revision ORDER BY revision DESC LIMIT 1");
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
