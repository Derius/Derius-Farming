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
		this.writeConfig(Const.JSON_FERTILIZE_FIELD_RADIUS_STEP_PER_LEVELS, 25);
		this.writeConfig(Const.JSON_FERTILIZE_FIELD_BASE_RADIUS, 2.5);
		this.writeConfig(Const.JSON_FERTILIZE_FIELD_RADIUS_PER_STEP, 1.5);
		this.writeConfig(Const.JSON_FERTILIZE_FIELD_GROWTH_STEPS_PER_LEVEL, 1.5);
		this.writeConfig(Const.JSON_FERTILIZE_FIELD_GROWTH_AMOUNT_PER_STEPS, 1.5);
		this.writeConfig(Const.JSON_FERTILIZE_FIELD_GROWTH_MAX, 1.5);
		
		this.writeConfig(Const.JSON_DOUBLE_DROP_LEVEL_PER_PERCENT, 20);
		this.writeConfig(Const.JSON_REPLANT_LEVEL_PER_PERCENT, 50);
		this.writeConfig(Const.JSON_EXP_GAIN, MUtil.map(
				Material.BROWN_MUSHROOM, 5,
				Material.CACTUS, 10,
				Material.CARROT, 20,
				Material.COCOA, 10,
				Material.DOUBLE_PLANT, 2,
				Material.LONG_GRASS, 1,
				Material.PUMPKIN, 40,
				Material.MELON_BLOCK, 40,
				Material.NETHER_WARTS, 30,
				Material.POTATO, 20,
				Material.RED_MUSHROOM, 2,
				Material.RED_ROSE, 2,
				Material.SUGAR_CANE_BLOCK, 20,
				Material.WATER_LILY, 2,
				Material.CROPS, 20,
				Material.YELLOW_FLOWER, 2
				), new TypeToken<Map<Material, Integer>>(){});
		
		this.writeConfig(Const.JSON_CAREFUL_HARVESTING, MUtil.map(
				0, 0.5,
				1000, 1.5,
				2000, 3.0), new TypeToken<Map<Integer, Double>>(){});
		
		this.writeConfig(Const.JSON_REPLANT_MATERIALS, MUtil.list(
				Material.WHEAT,
				Material.NETHER_WARTS,
				Material.COCOA,
				Material.CARROT,
				Material.POTATO
				), new TypeToken<List<Material>>(){});
		
		this.writeConfig(Const.JSON_FERTILIZE_FIELD_MATERIALS, MUtil.list(
				Material.WHEAT,
				Material.NETHER_WARTS,
				Material.CARROT,
				Material.COCOA,
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
	
	// Exp
	public static Map<Material, Integer> getExpGain()
	{
		return get().readConfig(Const.JSON_EXP_GAIN, new TypeToken<Map<Material, Integer>>(){});
	}
	
	// DoubleDropAndReplant
	public static int getDoubleDropLevelPerPercent()
	{
		return get().readConfig(Const.JSON_DOUBLE_DROP_LEVEL_PER_PERCENT, Integer.class);
	}
	
	public static int getReplantLevelPerPercent()
	{
		return get().readConfig(Const.JSON_REPLANT_LEVEL_PER_PERCENT, Integer.class);
	}	
	
	public static List<Material> getReplantMaterials()
	{
		return get().readConfig(Const.JSON_REPLANT_MATERIALS, new TypeToken<List<Material>>(){});
	}
	
	// FertilizeField
	public static int getFertilizeFieldMinLvl()
	{
		return get().readConfig(Const.JSON_FERTILIZE_FIELD_MIN_LVL, Integer.class);
	}
	
	public static List<Material> getFertilizeFieldMaterials()
	{
		return get().readConfig(Const.JSON_FERTILIZE_FIELD_MATERIALS, new TypeToken<List<Material>>(){});
	}

	public static double getFertilizeFieldBaseRadius()
	{
		return get().readConfig(Const.JSON_FERTILIZE_FIELD_BASE_RADIUS, Double.class);
	}

	public static int getFertilizeFieldRadiusStepPerLevels()
	{
		return get().readConfig(Const.JSON_FERTILIZE_FIELD_RADIUS_STEP_PER_LEVELS, Integer.class);
	}

	public static double getFertilizeFieldRadiusPerStep()
	{
		return get().readConfig(Const.JSON_FERTILIZE_FIELD_RADIUS_PER_STEP, Double.class);
	}
	
	//  -- Field Growth
	public static double getFertilizeFieldGrowthStepsPerLevels()
	{
		return get().readConfig(Const.JSON_FERTILIZE_FIELD_GROWTH_STEPS_PER_LEVEL, Double.class);
	}
	
	public static double getFertilizeFieldGrowthAmountPerStep()
	{
		return get().readConfig(Const.JSON_FERTILIZE_FIELD_GROWTH_AMOUNT_PER_STEPS, Double.class);
	}

	public static byte getFertilizeFieldGrowthMax()
	{
		return get().readConfig(Const.JSON_FERTILIZE_FIELD_GROWTH_MAX, Byte.class);
	}
	
	// Durability Multiplier
	public static Map<Integer, Double> getDurabilityMultiplier()
	{
		return get().readConfig(Const.JSON_CAREFUL_HARVESTING, new TypeToken<Map<Integer, Double>>(){});
	}

}
