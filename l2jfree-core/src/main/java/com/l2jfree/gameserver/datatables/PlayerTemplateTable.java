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
package com.l2jfree.gameserver.datatables;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.l2jfree.config.L2Properties;
import com.l2jfree.gameserver.templates.L2PlayerTemplate;
import com.l2jfree.gameserver.templates.player.ClassId;
import com.l2jfree.gameserver.templates.player.ClassType;
import com.l2jfree.gameserver.templates.player.Gender;
import com.l2jfree.gameserver.templates.player.PlayerBaseGenderTemplate;
import com.l2jfree.gameserver.templates.player.PlayerBaseTemplate;
import com.l2jfree.gameserver.templates.player.Race;
import com.l2jfree.gameserver.util.Datapack;
import com.l2jfree.util.Introspection;
import com.l2jfree.util.L2XML;
import com.l2jfree.util.logging.L2Logger;

/**
 * @author NB4L1
 */
public final class PlayerTemplateTable
{
	private static final L2Logger _log = L2Logger.getLogger(PlayerTemplateTable.class);
	
	private static final class SingletonHolder
	{
		public static final PlayerTemplateTable INSTANCE = new PlayerTemplateTable();
	}
	
	public static PlayerTemplateTable getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private final PlayerBaseTemplate[][] _playerBaseTemplates;
	private final L2PlayerTemplate[] _playerTemplates = new L2PlayerTemplate[ClassId.VALUES.length()];
	
	@SuppressWarnings("unused")
	private PlayerTemplateTable()
	{
		final Race[] races = Race.values();
		final ClassType[] types = new ClassType[] { ClassType.Fighter, ClassType.Mystic };
		final Gender[] genders = Gender.values();
		
		final PlayerBaseTemplate[][] baseTemplates = new PlayerBaseTemplate[races.length][types.length];
		
		for (Race race : races)
		{
			for (ClassType type : types)
			{
				if (race == Race.Dwarf || race == Race.Kamael)
					if (type == ClassType.Mystic)
						continue;
				
				final PlayerBaseGenderTemplate[] genderTemplate = new PlayerBaseGenderTemplate[Gender.VALUES.length()];
				for (Gender gender : genders)
					genderTemplate[gender.ordinal()] = new PlayerBaseGenderTemplate(gender);
				
				baseTemplates[race.ordinal()][type.ordinal()] = new PlayerBaseTemplate(race, type, genderTemplate);
			}
		}
		
		_playerBaseTemplates = baseTemplates;
		
		for (final ClassId classId : ClassId.VALUES)
		{
			if (classId.isDummy())
				continue;
			
			final PlayerBaseTemplate playerBaseTemplate =
					_playerBaseTemplates[classId.getRace().ordinal()][classId.getBaseType().ordinal()];
			
			_playerTemplates[classId.ordinal()] = new L2PlayerTemplate(classId, playerBaseTemplate);
		}
		
		System.out.println(Introspection.toString(getTemplate(ClassId.AbyssWalker)));
		
		// FIXME replace once a marshaller/unmarshaller fit for our needs are implemented
		final Document doc;
		try
		{
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false); // FIXME add validation
			factory.setIgnoringComments(true);
			
			doc = factory.newDocumentBuilder().parse(Datapack.getDatapackFile("player_templates.xml"));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		
		final L2Properties[] defaults = new L2Properties[2];
		defaults[ClassType.Fighter.ordinal()] = new L2Properties();
		defaults[ClassType.Mystic.ordinal()] = new L2Properties();
		
		for (Node list : L2XML.listNodesByNodeName(doc, "list"))
		{
			for (Node n1 : L2XML.listNodesByNodeName(list, "baseTemplates"))
			{
				for (Node n2 : L2XML.listNodesByNodeName(n1, "baseTemplate.defaults"))
				{
					final L2Properties s2 = L2XML.getSetters(n2);
					
					defaults[ClassType.Fighter.ordinal()].load(s2);
					defaults[ClassType.Mystic.ordinal()].load(s2);
					
					for (Node n3 : L2XML.listNodesByNodeName(n2, "baseTemplate.defaults.type"))
					{
						final L2Properties p3 = new L2Properties(n3);
						final ClassType type = p3.getEnum(ClassType.class, "type");
						
						final L2Properties s3 = L2XML.getSetters(n3);
						
						defaults[type.ordinal()].load(s3);
					}
				}
			}
		}
		
		for (Node list : L2XML.listNodesByNodeName(doc, "list"))
		{
			for (Node n1 : L2XML.listNodesByNodeName(list, "baseTemplates"))
			{
				for (Node n2 : L2XML.listNodesByNodeName(n1, "baseTemplate"))
				{
					final L2Properties p2 = new L2Properties(n2);
					final Race race = p2.getEnum(Race.class, "race");
					final ClassType type = p2.getEnum(ClassType.class, "type");
					final PlayerBaseTemplate baseTemplate = getBaseTemplate(race, type);
					
					final L2Properties s2 = new L2Properties();
					s2.load(defaults[type.ordinal()]);
					s2.load(L2XML.getSetters(n2));
					
					baseTemplate.setRunSpeed(s2.getInteger("runSpeed"));
					baseTemplate.setWalkSpeed(s2.getInteger("walkSpeed"));
					baseTemplate.setRunSpeedInWater(s2.getInteger("runSpeedInWater"));
					baseTemplate.setWalkSpeedInWater(s2.getInteger("walkSpeedInWater"));
					baseTemplate.setRunSpeedInAir(s2.getInteger("runSpeedInAir"));
					baseTemplate.setWalkSpeedInAir(s2.getInteger("walkSpeedInAir"));
					baseTemplate.setRunSpeedNoble(s2.getInteger("runSpeedNoble"));
					baseTemplate.setWalkSpeedNoble(s2.getInteger("walkSpeedNoble"));
					baseTemplate.setAttackRange(s2.getInteger("attackRange"));
					baseTemplate.setPhysicalAttack(s2.getInteger("physicalAttack"));
					baseTemplate.setBreath(s2.getInteger("breath"));
					// TODO
					
					for (Node n3 : L2XML.listNodesByNodeName(n2, "genderTemplate"))
					{
						final L2Properties p3 = new L2Properties(n3);
						final Gender gender = p3.getEnum(Gender.class, "gender");
						final PlayerBaseGenderTemplate genderTemplate = baseTemplate.getGenderTemplate(gender);
						
						final L2Properties s3 = L2XML.getSetters(n3);
						
						genderTemplate.setCollisionRadius(s3.getDouble("collisionRadius"));
						genderTemplate.setCollisionHeight(s3.getDouble("collisionHeight"));
						genderTemplate.setSafeFallHeight(s3.getInteger("safeFallHeight"));
						// TODO
					}
				}
			}
		}
		
		for (Node list : L2XML.listNodesByNodeName(doc, "list"))
		{
			for (Node n1 : L2XML.listNodesByNodeName(list, "playerTemplates"))
			{
				for (Node n2 : L2XML.listNodesByNodeName(n1, "playerTemplate.defaults"))
				{
					final L2Properties s2 = L2XML.getSetters(n2);
					
					// TODO
				}
			}
		}
		
		for (Node list : L2XML.listNodesByNodeName(doc, "list"))
		{
			for (Node n1 : L2XML.listNodesByNodeName(list, "playerTemplates"))
			{
				for (Node n2 : L2XML.listNodesByNodeName(n1, "playerTemplate"))
				{
					final L2Properties p2 = new L2Properties(n2);
					final L2Properties s2 = L2XML.getSetters(n2);
					
					// TODO
				}
			}
		}
		
		System.out.println(Introspection.toString(getTemplate(ClassId.AbyssWalker)));
		
		_log.info("PlayerTemplateTable: Initialized.");
	}
	
	public PlayerBaseTemplate getBaseTemplate(ClassId classId)
	{
		return getBaseTemplate(classId.getRace(), classId.getBaseType());
	}
	
	public PlayerBaseTemplate getBaseTemplate(Race race, ClassType type)
	{
		return _playerBaseTemplates[race.ordinal()][type.ordinal()];
	}
	
	public L2PlayerTemplate getTemplate(ClassId classId)
	{
		return _playerTemplates[classId.ordinal()];
	}
}
