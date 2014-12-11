package dk.muj.derius.farming.entity;

import java.util.Map;

import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

public class MConf extends Entity<MConf>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	protected static transient MConf i;
	public static MConf get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// -------------------------------------------- //
	// ID DEFINITION
	// -------------------------------------------- //
	
	/* 
	 * Note: skillId and abilityId are not related,
	 * but we use the tenners to define the skillId and all the values in between as the abilityId's.
	 */
	 
	/**
	 * The Id of the skill, there is only one of these in each skill.
	 */
	public int getSkillId = 30;
	
	/**
	 * The Id of the double drop and replace Ability. Each ability has it's own Id
	 */
	public int getDoubleDropAndReplantId = 31;
	
	/**
	 * The Id of the Tree harvest Ability. Each ability has it's own Id
	 */
	public int getFertilizeFieldId = 32;
	

	
	// -------------------------------------------- //
	// ABILITY REQUIREMENTS
	// -------------------------------------------- //
	
	/**
	 * The level the player has to have for executing the fertilize field ability
	 */
	public int getFertilizeFieldMinLvl = 500;
	
	// -------------------------------------------- //
	// EXP GAIN
	// -------------------------------------------- //
	
	// TODO: Change this map to farming related IDs!
	/**
	 * This Map stores, which blockId (first Integer) gives you how many exp (second Integer).
	 */
	public Map<Integer, Integer> expGain = MUtil.map(
			17,	20,
			162,	20
			);
}
