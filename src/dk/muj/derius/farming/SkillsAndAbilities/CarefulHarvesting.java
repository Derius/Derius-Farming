package dk.muj.derius.farming.SkillsAndAbilities;

import java.util.Collection;
import java.util.Map;

import org.bukkit.Material;

import dk.muj.derius.api.ability.AbilityDurabilityMultiplier;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.farming.EngineFarming;

public class CarefulHarvesting extends AbilityDurabilityMultiplier
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static CarefulHarvesting i = new CarefulHarvesting();
	public static CarefulHarvesting get() { return i; }

	public CarefulHarvesting()
	{

	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getId()
	{
		return "derius:farming:careful";
	}

	@Override
	public Skill getSkill()
	{
		return FarmingSkill.get();
	}

	@Override
	public Collection<Material> getToolTypes()
	{
		return EngineFarming.HOE_MATERIALS;
	}

	@Override
	public Map<Integer, Double> getDurabilityMultiplier()
	{
		return FarmingSkill.getDurabilityMultiplier();
	}

	@Override
	public String getToolName()
	{
		return "Hoe";
	}
}
