package dk.muj.derius.farming.entity;

import java.util.Map;

import org.bukkit.Material;

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
	public String getSkillId = "Derius_Farming";
	
	/**
	 * The Id of the double drop and replace Ability. Each ability has it's own Id
	 */
	public String getDoubleDropAndReplantId = "Derius_Farming_DoubleDropAndReplant";
	
	/**
	 * The Id of the Tree harvest Ability. Each ability has it's own Id
	 */
	public String getFertilizeFieldId = "Derius_Farming_FertilizeField";
	

	
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

	/**
	 * This Map stores, which Material gives you how many exp (second Integer).
	 * Melon stem = 104, pumpkin stem = 105
	 */
	public Map<Material, Integer> expGain = MUtil.map(
			Material.WHEAT,	20,
			Material.CACTUS,	10,
			Material.SUGAR_CANE_BLOCK,	20,
			Material.PUMPKIN,	40,
			Material.MELON_BLOCK,	40,
			Material.NETHER_WARTS,	30,
			Material.COCOA,	10,
			Material.CARROT,	20,
			Material.POTATO,	20
			);
}
