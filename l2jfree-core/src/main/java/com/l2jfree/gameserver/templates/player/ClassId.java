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
package com.l2jfree.gameserver.templates.player;

import com.l2jfree.gameserver.datatables.PlayerTemplateTable;
import com.l2jfree.gameserver.templates.L2PlayerTemplate;
import com.l2jfree.util.EnumValues;

/**
 * @author NB4L1
 */
public enum ClassId
{
	HumanFighter/*					*/(0, Race.Human, ClassType.Fighter, ClassLevel.First, null),
	/*	*/Warrior/*					*/(1, Race.Human, ClassType.Fighter, ClassLevel.Second, HumanFighter),
	/*		*/Gladiator/*			*/(2, Race.Human, ClassType.Fighter, ClassLevel.Third, Warrior),
	/*		*/Warlord/*				*/(3, Race.Human, ClassType.Fighter, ClassLevel.Third, Warrior),
	/*	*/HumanKnight/*				*/(4, Race.Human, ClassType.Fighter, ClassLevel.Second, HumanFighter),
	/*		*/Paladin/*				*/(5, Race.Human, ClassType.Fighter, ClassLevel.Third, HumanKnight),
	/*		*/DarkAvenger/*			*/(6, Race.Human, ClassType.Fighter, ClassLevel.Third, HumanKnight),
	/*	*/Rogue/*					*/(7, Race.Human, ClassType.Fighter, ClassLevel.Second, HumanFighter),
	/*		*/TreasureHunter/*		*/(8, Race.Human, ClassType.Fighter, ClassLevel.Third, Rogue),
	/*		*/Hawkeye/*				*/(9, Race.Human, ClassType.Fighter, ClassLevel.Third, Rogue),
	
	HumanMystic/*					*/(10, Race.Human, ClassType.Mystic, ClassLevel.First, null),
	/*	*/Wizard/*					*/(11, Race.Human, ClassType.Mystic, ClassLevel.Second, HumanMystic),
	/*		*/Sorcerer/*			*/(12, Race.Human, ClassType.Mystic, ClassLevel.Third, Wizard),
	/*		*/Necromancer/*			*/(13, Race.Human, ClassType.Mystic, ClassLevel.Third, Wizard),
	/*		*/Warlock/*				*/(14, Race.Human, ClassType.Summoner, ClassLevel.Third, Wizard),
	/*	*/Cleric/*					*/(15, Race.Human, ClassType.Priest, ClassLevel.Second, HumanMystic),
	/*		*/Bishop/*				*/(16, Race.Human, ClassType.Priest, ClassLevel.Third, Cleric),
	/*		*/Prophet/*				*/(17, Race.Human, ClassType.Priest, ClassLevel.Third, Cleric),
	
	ElvenFighter/*					*/(18, Race.Elf, ClassType.Fighter, ClassLevel.First, null),
	/*	*/ElvenKnight/*				*/(19, Race.Elf, ClassType.Fighter, ClassLevel.Second, ElvenFighter),
	/*		*/TempleKnight/*		*/(20, Race.Elf, ClassType.Fighter, ClassLevel.Third, ElvenKnight),
	/*		*/Swordsinger/*			*/(21, Race.Elf, ClassType.Fighter, ClassLevel.Third, ElvenKnight),
	/*	*/ElvenScout/*				*/(22, Race.Elf, ClassType.Fighter, ClassLevel.Second, ElvenFighter),
	/*		*/Plainswalker/*		*/(23, Race.Elf, ClassType.Fighter, ClassLevel.Third, ElvenScout),
	/*		*/SilverRanger/*		*/(24, Race.Elf, ClassType.Fighter, ClassLevel.Third, ElvenScout),
	
	ElvenMystic/*					*/(25, Race.Elf, ClassType.Mystic, ClassLevel.First, null),
	/*	*/ElvenWizard/*				*/(26, Race.Elf, ClassType.Mystic, ClassLevel.Second, ElvenMystic),
	/*		*/Spellsinger/*			*/(27, Race.Elf, ClassType.Mystic, ClassLevel.Third, ElvenWizard),
	/*		*/ElementalSummoner/*	*/(28, Race.Elf, ClassType.Summoner, ClassLevel.Third, ElvenWizard),
	/*	*/ElvenOracle/*				*/(29, Race.Elf, ClassType.Priest, ClassLevel.Second, ElvenMystic),
	/*		*/ElvenElder/*			*/(30, Race.Elf, ClassType.Priest, ClassLevel.Third, ElvenOracle),
	
	DarkFighter/*					*/(31, Race.DarkElf, ClassType.Fighter, ClassLevel.First, null),
	/*	*/PalusKnight/*				*/(32, Race.DarkElf, ClassType.Fighter, ClassLevel.Second, DarkFighter),
	/*		*/ShillienKnight/*		*/(33, Race.DarkElf, ClassType.Fighter, ClassLevel.Third, PalusKnight),
	/*		*/Bladedancer/*			*/(34, Race.DarkElf, ClassType.Fighter, ClassLevel.Third, PalusKnight),
	/*	*/Assassin/*				*/(35, Race.DarkElf, ClassType.Fighter, ClassLevel.Second, DarkFighter),
	/*		*/AbyssWalker/*			*/(36, Race.DarkElf, ClassType.Fighter, ClassLevel.Third, Assassin),
	/*		*/PhantomRanger/*		*/(37, Race.DarkElf, ClassType.Fighter, ClassLevel.Third, Assassin),
	
	DarkMystic/*					*/(38, Race.DarkElf, ClassType.Mystic, ClassLevel.First, null),
	/*	*/DarkWizard/*				*/(39, Race.DarkElf, ClassType.Mystic, ClassLevel.Second, DarkMystic),
	/*		*/Spellhowler/*			*/(40, Race.DarkElf, ClassType.Mystic, ClassLevel.Third, DarkWizard),
	/*		*/PhantomSummoner/*		*/(41, Race.DarkElf, ClassType.Summoner, ClassLevel.Third, DarkWizard),
	/*	*/ShillienOracle/*			*/(42, Race.DarkElf, ClassType.Priest, ClassLevel.Second, DarkMystic),
	/*		*/ShillienElder/*		*/(43, Race.DarkElf, ClassType.Priest, ClassLevel.Third, ShillienOracle),
	
	OrcFighter/*					*/(44, Race.Orc, ClassType.Fighter, ClassLevel.First, null),
	/*	*/OrcRaider/*				*/(45, Race.Orc, ClassType.Fighter, ClassLevel.Second, OrcFighter),
	/*		*/Destroyer/*			*/(46, Race.Orc, ClassType.Fighter, ClassLevel.Third, OrcRaider),
	/*	*/Monk/*					*/(47, Race.Orc, ClassType.Fighter, ClassLevel.Second, OrcFighter),
	/*		*/Tyrant/*				*/(48, Race.Orc, ClassType.Fighter, ClassLevel.Third, Monk),
	
	OrcMystic/*						*/(49, Race.Orc, ClassType.Mystic, ClassLevel.First, null),
	/*	*/OrcShaman/*				*/(50, Race.Orc, ClassType.Mystic, ClassLevel.Second, OrcMystic),
	/*		*/Overlord/*			*/(51, Race.Orc, ClassType.Mystic, ClassLevel.Third, OrcShaman),
	/*		*/Warcryer/*			*/(52, Race.Orc, ClassType.Mystic, ClassLevel.Third, OrcShaman),
	
	DwarvenFighter/*				*/(53, Race.Dwarf, ClassType.Fighter, ClassLevel.First, null),
	/*	*/Scavenger/*				*/(54, Race.Dwarf, ClassType.Fighter, ClassLevel.Second, DwarvenFighter),
	/*		*/BountyHunter/*		*/(55, Race.Dwarf, ClassType.Fighter, ClassLevel.Third, Scavenger),
	/*	*/Artisan/*					*/(56, Race.Dwarf, ClassType.Fighter, ClassLevel.Second, DwarvenFighter),
	/*		*/Warsmith/*			*/(57, Race.Dwarf, ClassType.Fighter, ClassLevel.Third, Artisan),
	
	dummyEntry58(58),
	dummyEntry59(59),
	dummyEntry60(60),
	dummyEntry61(61),
	dummyEntry62(62),
	dummyEntry63(63),
	dummyEntry64(64),
	dummyEntry65(65),
	dummyEntry66(66),
	dummyEntry67(67),
	dummyEntry68(68),
	dummyEntry69(69),
	dummyEntry70(70),
	dummyEntry71(71),
	dummyEntry72(72),
	dummyEntry73(73),
	dummyEntry74(74),
	dummyEntry75(75),
	dummyEntry76(76),
	dummyEntry77(77),
	dummyEntry78(78),
	dummyEntry79(79),
	dummyEntry80(80),
	dummyEntry81(81),
	dummyEntry82(82),
	dummyEntry83(83),
	dummyEntry84(84),
	dummyEntry85(85),
	dummyEntry86(86),
	dummyEntry87(87),
	
	Duelist/*			*/(88, Race.Human, ClassType.Fighter, ClassLevel.Fourth, Gladiator),
	Dreadnought/*		*/(89, Race.Human, ClassType.Fighter, ClassLevel.Fourth, Warlord),
	PhoenixKnight/*		*/(90, Race.Human, ClassType.Fighter, ClassLevel.Fourth, Paladin),
	HellKnight/*		*/(91, Race.Human, ClassType.Fighter, ClassLevel.Fourth, DarkAvenger),
	Sagittarius/*		*/(92, Race.Human, ClassType.Fighter, ClassLevel.Fourth, Hawkeye),
	Adventurer/*		*/(93, Race.Human, ClassType.Fighter, ClassLevel.Fourth, TreasureHunter),
	Archmage/*			*/(94, Race.Human, ClassType.Mystic, ClassLevel.Fourth, Sorcerer),
	Soultaker/*			*/(95, Race.Human, ClassType.Mystic, ClassLevel.Fourth, Necromancer),
	ArcanaLord/*		*/(96, Race.Human, ClassType.Summoner, ClassLevel.Fourth, Warlock),
	Cardinal/*			*/(97, Race.Human, ClassType.Priest, ClassLevel.Fourth, Bishop),
	Hierophant/*		*/(98, Race.Human, ClassType.Priest, ClassLevel.Fourth, Prophet),
	
	EvasTemplar/*		*/(99, Race.Elf, ClassType.Fighter, ClassLevel.Fourth, TempleKnight),
	SwordMuse/*			*/(100, Race.Elf, ClassType.Fighter, ClassLevel.Fourth, Swordsinger),
	WindRider/*			*/(101, Race.Elf, ClassType.Fighter, ClassLevel.Fourth, Plainswalker),
	MoonlightSentinel/*	*/(102, Race.Elf, ClassType.Fighter, ClassLevel.Fourth, SilverRanger),
	MysticMuse/*		*/(103, Race.Elf, ClassType.Mystic, ClassLevel.Fourth, Spellsinger),
	ElementalMaster/*	*/(104, Race.Elf, ClassType.Summoner, ClassLevel.Fourth, ElementalSummoner),
	EvasSaint/*			*/(105, Race.Elf, ClassType.Priest, ClassLevel.Fourth, ElvenElder),
	
	ShillienTemplar/*	*/(106, Race.DarkElf, ClassType.Fighter, ClassLevel.Fourth, ShillienKnight),
	SpectralDancer/*	*/(107, Race.DarkElf, ClassType.Fighter, ClassLevel.Fourth, Bladedancer),
	GhostHunter/*		*/(108, Race.DarkElf, ClassType.Fighter, ClassLevel.Fourth, AbyssWalker),
	GhostSentinel/*		*/(109, Race.DarkElf, ClassType.Fighter, ClassLevel.Fourth, PhantomRanger),
	StormScreamer/*		*/(110, Race.DarkElf, ClassType.Mystic, ClassLevel.Fourth, Spellhowler),
	SpectralMaster/*	*/(111, Race.DarkElf, ClassType.Summoner, ClassLevel.Fourth, PhantomSummoner),
	ShillienSaint/*		*/(112, Race.DarkElf, ClassType.Priest, ClassLevel.Fourth, ShillienElder),
	
	Titan/*				*/(113, Race.Orc, ClassType.Fighter, ClassLevel.Fourth, Destroyer),
	GrandKhavatari/*	*/(114, Race.Orc, ClassType.Fighter, ClassLevel.Fourth, Tyrant),
	Dominator/*			*/(115, Race.Orc, ClassType.Mystic, ClassLevel.Fourth, Overlord),
	Doomcryer/*			*/(116, Race.Orc, ClassType.Mystic, ClassLevel.Fourth, Warcryer),
	
	FortuneSeeker/*		*/(117, Race.Dwarf, ClassType.Fighter, ClassLevel.Fourth, BountyHunter),
	Maestro/*			*/(118, Race.Dwarf, ClassType.Fighter, ClassLevel.Fourth, Warsmith),
	
	dummyEntry119(119),
	dummyEntry120(120),
	dummyEntry121(121),
	dummyEntry122(122),
	
	MaleSoldier/*					*/(123, Race.Kamael, ClassType.Fighter, ClassLevel.First, null),
	FemaleSoldier/*					*/(124, Race.Kamael, ClassType.Fighter, ClassLevel.First, null),
	/*	*/Trooper/*					*/(125, Race.Kamael, ClassType.Fighter, ClassLevel.Second, MaleSoldier),
	/*	*/Warder/*					*/(126, Race.Kamael, ClassType.Fighter, ClassLevel.Second, FemaleSoldier),
	/*		*/Berserker/*			*/(127, Race.Kamael, ClassType.Fighter, ClassLevel.Third, Trooper),
	/*		*/MaleSoulBreaker/*		*/(128, Race.Kamael, ClassType.Fighter, ClassLevel.Third, Trooper),
	/*		*/FemaleSoulBreaker/*	*/(129, Race.Kamael, ClassType.Fighter, ClassLevel.Third, Warder),
	/*		*/Arbalester/*			*/(130, Race.Kamael, ClassType.Fighter, ClassLevel.Third, Warder),
	/*			*/Doombringer/*		*/(131, Race.Kamael, ClassType.Fighter, ClassLevel.Fourth, Berserker),
	/*			*/MaleSoulHound/*	*/(132, Race.Kamael, ClassType.Fighter, ClassLevel.Fourth, MaleSoulBreaker),
	/*			*/FemaleSoulHound/*	*/(133, Race.Kamael, ClassType.Fighter, ClassLevel.Fourth, FemaleSoulBreaker),
	/*			*/Trickster/*		*/(134, Race.Kamael, ClassType.Fighter, ClassLevel.Fourth, Arbalester),
	/*		*/Inspector/*			*/(135, Race.Kamael, ClassType.Fighter, ClassLevel.Third, Warder),
	/*			*/Judicator/*		*/(136, Race.Kamael, ClassType.Fighter, ClassLevel.Fourth, Inspector);
	
	private final int _id;
	private final Race _race;
	private final ClassType _type;
	private final ClassLevel _level;
	private final ClassId _parent;
	private final boolean _dummy;
	private final String _name;
	
	private ClassId(int id)
	{
		_id = id;
		_race = null;
		_type = null;
		_level = null;
		_parent = null;
		_dummy = true;
		_name = "";
	}
	
	private ClassId(int id, Race race, ClassType type, ClassLevel level, ClassId parent)
	{
		_id = id;
		_race = race;
		_type = type;
		_level = level;
		_parent = parent;
		_dummy = false;
		
		final StringBuilder sb = new StringBuilder();
		for (char ch : name().toCharArray())
		{
			if (Character.isUpperCase(ch))
				sb.append(' ');
			sb.append(ch);
		}
		_name = sb.toString().replace("Evas", "Eva's").trim();
	}
	
	public int getId()
	{
		return _id;
	}
	
	public Race getRace()
	{
		return _race;
	}
	
	public ClassType getType()
	{
		return _type;
	}
	
	public ClassLevel getLevel()
	{
		return _level;
	}
	
	public ClassId getParent()
	{
		return _parent;
	}
	
	public boolean isDummy()
	{
		return _dummy;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public ClassType getBaseType()
	{
		return _type.isMage() ? ClassType.Mystic : ClassType.Fighter;
	}
	
	public L2PlayerTemplate getTemplate()
	{
		return PlayerTemplateTable.getInstance().getTemplate(this);
	}
	
	public static final EnumValues<ClassId> VALUES = new EnumValues<ClassId>(ClassId.class);
}
