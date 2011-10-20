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
import com.l2jfree.gameserver.templates.L2PlayerTemplate.L2PlayerGenderTemplate;
import com.l2jfree.gameserver.templates.player.ClassId;
import com.l2jfree.gameserver.templates.player.ClassType;
import com.l2jfree.gameserver.templates.player.Gender;
import com.l2jfree.gameserver.templates.player.Race;
import com.l2jfree.gameserver.util.Datapack;
import com.l2jfree.util.Introspection;
import com.l2jfree.util.L2Collections;
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
	
	private final L2PlayerTemplate[] _playerTemplates = new L2PlayerTemplate[ClassId.VALUES.length()];
	
	private PlayerTemplateTable()
	{
		for (final ClassId classId : ClassId.VALUES)
		{
			if (classId.isDummy())
				continue;
			
			_playerTemplates[classId.ordinal()] = new L2PlayerTemplate(classId);
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
		
		final Iterable<ClassId> validClasses =
				L2Collections.filteredIterable(ClassId.class, ClassId.VALUES, new L2Collections.Filter<ClassId>() {
					@Override
					public boolean accept(ClassId classId)
					{
						return !classId.isDummy();
					}
				});
		
		final L2Properties[] defaultsByClassId = new L2Properties[ClassId.VALUES.length()];
		for (ClassId classId : validClasses)
			defaultsByClassId[classId.ordinal()] = new L2Properties();
		
		final L2Properties[][] defaultsByClassIdAndGender =
				new L2Properties[ClassId.VALUES.length()][Gender.VALUES.length()];
		for (ClassId classId : validClasses)
			for (Gender gender : Gender.VALUES)
				defaultsByClassIdAndGender[classId.ordinal()][gender.ordinal()] = new L2Properties();
		
		for (Node list : L2XML.listNodesByNodeName(doc, "list"))
		{
			for (Node n1 : L2XML.listNodesByNodeName(list, "playerTemplate.defaults"))
			{
				final L2Properties s1 = L2XML.getSetters(n1);
				
				for (ClassId classId : validClasses)
					defaultsByClassId[classId.ordinal()].load(s1);
				
				for (Node n2 : L2XML.listNodesByNodeName(n1, "playerTemplate.defaults.type"))
				{
					final L2Properties p2 = new L2Properties(n2);
					final ClassType type = p2.getEnum(ClassType.class, "type");
					
					final L2Properties s2 = L2XML.getSetters(n2);
					
					for (ClassId classId : validClasses)
						if (classId.getBaseType() == type)
							defaultsByClassId[classId.ordinal()].load(s2);
				}
				
				for (Node n2 : L2XML.listNodesByNodeName(n1, "playerTemplate.defaults.race.type"))
				{
					final L2Properties p2 = new L2Properties(n2);
					final Race race = p2.getEnum(Race.class, "race");
					final ClassType type = p2.getEnum(ClassType.class, "type");
					
					final L2Properties s2 = L2XML.getSetters(n2);
					
					for (ClassId classId : validClasses)
						if (classId.getRace() == race)
							if (classId.getBaseType() == type)
								defaultsByClassId[classId.ordinal()].load(s2);
					
					for (Node n3 : L2XML.listNodesByNodeName(n2, "genderTemplate.defaults.race.type"))
					{
						final L2Properties p3 = new L2Properties(n3);
						final Gender gender = p3.getEnum(Gender.class, "gender");
						
						final L2Properties s3 = L2XML.getSetters(n3);
						
						for (ClassId classId : validClasses)
							if (classId.getRace() == race)
								if (classId.getBaseType() == type)
									defaultsByClassIdAndGender[classId.ordinal()][gender.ordinal()].load(s3);
					}
				}
			}
			
			for (Node n1 : L2XML.listNodesByNodeName(list, "playerTemplate"))
			{
				final L2Properties p1 = new L2Properties(n1);
				final ClassId classId = ClassId.VALUES.valueOf(p1.getInteger("classId"));
				
				final L2Properties s1 = new L2Properties();
				s1.load(defaultsByClassId[classId.ordinal()]);
				s1.load(L2XML.getSetters(n1));
				
				final L2PlayerTemplate template = _playerTemplates[classId.ordinal()];
				
				template.setRunSpeed(s1.getInteger("runSpeed"));
				template.setWalkSpeed(s1.getInteger("walkSpeed"));
				template.setRunSpeedInWater(s1.getInteger("runSpeedInWater"));
				template.setWalkSpeedInWater(s1.getInteger("walkSpeedInWater"));
				template.setRunSpeedInAir(s1.getInteger("runSpeedInAir"));
				template.setWalkSpeedInAir(s1.getInteger("walkSpeedInAir"));
				template.setRunSpeedNoble(s1.getInteger("runSpeedNoble"));
				template.setWalkSpeedNoble(s1.getInteger("walkSpeedNoble"));
				template.setAttackRange(s1.getInteger("attackRange"));
				template.setPhysicalAttack(s1.getInteger("physicalAttack"));
				template.setBreath(s1.getInteger("breath"));
				// TODO
				
				final L2Properties[] genders = new L2Properties[2];
				for (Gender gender : Gender.VALUES)
				{
					genders[gender.ordinal()] = new L2Properties();
					genders[gender.ordinal()].load(defaultsByClassIdAndGender[classId.ordinal()][gender.ordinal()]);
				}
				
				for (Node n2 : L2XML.listNodesByNodeName(n1, "genderTemplate"))
				{
					final L2Properties p2 = new L2Properties(n2);
					final Gender gender = p2.getEnum(Gender.class, "gender");
					
					final L2Properties s2 = genders[gender.ordinal()];
					s2.load(L2XML.getSetters(n2));
				}
				
				for (Gender gender : Gender.VALUES)
				{
					final L2Properties s2 = genders[gender.ordinal()];
					
					final L2PlayerGenderTemplate genderTemplate = template.getGenderTemplate(gender);
					
					genderTemplate.setCollisionRadius(s2.getDouble("collisionRadius"));
					genderTemplate.setCollisionHeight(s2.getDouble("collisionHeight"));
					genderTemplate.setSafeFallHeight(s2.getInteger("safeFallHeight"));
					// TODO
				}
			}
		}
		
		System.out.println(Introspection.toString(getTemplate(ClassId.AbyssWalker)));
		
		_log.info("PlayerTemplateTable: Initialized.");
	}
	
	public L2PlayerTemplate getTemplate(ClassId classId)
	{
		return _playerTemplates[classId.ordinal()];
	}
}
