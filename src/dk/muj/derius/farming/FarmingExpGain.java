package dk.muj.derius.farming;

import java.util.Collection;
import java.util.Map;

import org.bukkit.Material;

import dk.muj.derius.api.BlockBreakExpGain;
import dk.muj.derius.api.skill.Skill;

public class FarmingExpGain implements BlockBreakExpGain
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
   
	private static FarmingExpGain i = new FarmingExpGain();
	public static FarmingExpGain get() { return i; }
	private FarmingExpGain() { }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public Map<Material, Integer> getBlockTypes()
	{
		return FarmingSkill.getExpGain();
	}
	
	@Override
	public Collection<Material> getToolTypes()
	{
		return EngineFarming.HOE_MATERIALS;
	}
	
	@Override
	public Skill getSkill()
	{
		return FarmingSkill.get();
	}
}
