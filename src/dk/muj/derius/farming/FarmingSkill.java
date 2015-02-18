package dk.muj.derius.farming;

import java.util.Map;

import org.bukkit.Material;

import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.xlib.gson.reflect.TypeToken;

import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.skill.DeriusSkill;

public class FarmingSkill extends DeriusSkill implements Skill
{
	
	private static FarmingSkill i = new FarmingSkill();
	public static FarmingSkill get() { return i; }
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	public FarmingSkill()
	{
		// Skill properties
		this.setName("Farming");
		
		this.setDesc("Makes you better at farming.");
		
		this.addEarnExpDescs("Harvest crops and other vegetables!");
		
		this.setIcon(Material.WHEAT);
		
		// Config
		this.writeConfig("fertilizeFieldMinLvl", 500);
		this.writeConfig(Const.JSON_EXP_GAIN, MUtil.map(
				Material.WHEAT, 20,
				Material.CACTUS, 10,
				Material.SUGAR_CANE_BLOCK, 20,
				Material.PUMPKIN, 40,
				Material.MELON_BLOCK, 40,
				Material.NETHER_WARTS, 30,
				Material.COCOA, 10,
				Material.CARROT, 20,
				Material.POTATO, 20
				), new TypeToken<Map<Material, Integer>>(){});
		
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getId()
	{
		return "derius:farming";
	}

	// -------------------------------------------- //
	// CONFIG GETTERS
	// -------------------------------------------- //
	
	public static Map<Material, Integer> getExpGain()
	{
		return get().readConfig(Const.JSON_EXP_GAIN, new TypeToken<Map<Material, Integer>>(){});
	}
	
	public static int getFertilizeFieldMinLvl()
	{
		return get().readConfig("fertilizeFieldMinLvl", int.class);
	}
	
}
