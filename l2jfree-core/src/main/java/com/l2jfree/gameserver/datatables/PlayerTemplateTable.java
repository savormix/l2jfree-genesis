package com.l2jfree.gameserver.datatables;

import com.l2jfree.gameserver.templates.L2PlayerTemplate;
import com.l2jfree.gameserver.templates.player.ClassId;
import com.l2jfree.gameserver.templates.player.ClassType;
import com.l2jfree.gameserver.templates.player.Gender;
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
	
	private final PlayerBaseTemplate[][][] _playerBaseTemplates;
	private final L2PlayerTemplate[] _playerTemplates = new L2PlayerTemplate[ClassId.VALUES.length()];
	
	private PlayerTemplateTable()
	{
		final Race[] races = Race.values();
		final ClassType[] types = new ClassType[] { ClassType.Fighter, ClassType.Mystic };
		final Gender[] genders = Gender.values();
		
		final PlayerBaseTemplate[][][] tmp = new PlayerBaseTemplate[races.length][types.length][genders.length];
		
		for (Race race : races)
		{
			for (ClassType type : types)
			{
				for (Gender gender : genders)
				{
					if (race == Race.Dwarf || race == Race.Kamael)
						if (type == ClassType.Mystic)
							continue;
					
					tmp[race.ordinal()][type.ordinal()][gender.ordinal()] = new PlayerBaseTemplate(race, type, gender);
				}
			}
		}
		
		_playerBaseTemplates = tmp;
		
		// TODO load xml
		
		for (final ClassId classId : ClassId.VALUES)
		{
			if (classId.isDummy())
				continue;
			
			final PlayerBaseTemplate[] playerBaseTemplates =
					_playerBaseTemplates[classId.getRace().ordinal()][classId.getBaseType().ordinal()];
			
			_playerTemplates[classId.ordinal()] = new L2PlayerTemplate(classId, playerBaseTemplates);
		}
		
		_log.info("PlayerTemplateTable: Initialized.");
	}
	
	public PlayerBaseTemplate getPlayerBaseTemplate(ClassId classId, Gender gender)
	{
		return getPlayerBaseTemplate(classId.getRace(), classId.getBaseType(), gender);
	}
	
	public PlayerBaseTemplate getPlayerBaseTemplate(Race race, ClassType type, Gender gender)
	{
		return _playerBaseTemplates[race.ordinal()][type.ordinal()][gender.ordinal()];
	}
	
	public L2PlayerTemplate getPlayerTemplate(ClassId classId)
	{
		return _playerTemplates[classId.ordinal()];
	}
}