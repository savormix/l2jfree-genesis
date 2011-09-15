package com.l2jfree.gameserver.datatables;

import com.l2jfree.gameserver.templates.L2PlayerTemplate;
import com.l2jfree.gameserver.templates.player.ClassId;
import com.l2jfree.gameserver.templates.player.ClassType;
import com.l2jfree.gameserver.templates.player.Gender;
import com.l2jfree.gameserver.templates.player.PlayerBaseGenderTemplate;
import com.l2jfree.gameserver.templates.player.PlayerBaseTemplate;
import com.l2jfree.gameserver.templates.player.Race;
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
		
		// TODO load xml
		
		for (final ClassId classId : ClassId.VALUES)
		{
			if (classId.isDummy())
				continue;
			
			final PlayerBaseTemplate playerBaseTemplate =
					_playerBaseTemplates[classId.getRace().ordinal()][classId.getBaseType().ordinal()];
			
			_playerTemplates[classId.ordinal()] = new L2PlayerTemplate(classId, playerBaseTemplate);
		}
		
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
