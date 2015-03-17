package dk.muj.derius.farming;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.xlib.gson.reflect.TypeToken;

import dk.muj.derius.api.skill.SkillAbstract;

public class FarmingSkill extends SkillAbstract
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
		this.writeConfig(Const.JSON_FERTILIZE_FIELD_MIN_LVL, 500);
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
		
		this.writeConfig(Const.JSON_CAREFUL_HARVESTING, MUtil.map(
				0, 0.5,
				1000, 1.5,
				2000, 3.0), new TypeToken<Map<Integer, Double>>(){});
		
		this.writeConfig(Const.JSON_REPLANT_MATERIALS, MUtil.map(
				Material.WHEAT,
				Material.NETHER_WARTS,
				Material.COCOA,
				Material.CARROT,
				Material.POTATO
				), new TypeToken<List<Material>>(){});
		
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Plugin getPlugin()
	{
		return DeriusFarming.get();
	}
	
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
		return get().readConfig(Const.JSON_FERTILIZE_FIELD_MIN_LVL, Integer.class);
	}

	public static Map<Integer, Double> getDurabilityMultiplier()
	{
		return get().readConfig(Const.JSON_CAREFUL_HARVESTING, new TypeToken<Map<Integer, Double>>(){});
	}
	
	public static List<Material> getReplantMaterials()
	{
		return get().readConfig(Const.JSON_REPLANT_MATERIALS, new TypeToken<List<Material>>(){});
	}

}
