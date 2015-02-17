package dk.muj.derius.farming;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.skill.DeriusSkill;
import dk.muj.derius.farming.entity.MConf;

public class FarmingSkill extends DeriusSkill implements Skill
{
	
	private static FarmingSkill i = new FarmingSkill();
	public static FarmingSkill get() { return i; }
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	public FarmingSkill()
	{
		this.setName("Farming");
		
		this.setDesc("Makes you better at farming.");
		
		this.addEarnExpDescs("Harvest crops and other vegetables!");
		
		this.setIcon(Material.WHEAT);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getId()
	{
		return MConf.get().getSkillId;
	}

	@Override
	public List<Ability> getActiveAbilities()
	{
		List<Ability> activeAbilities = new ArrayList<Ability>();
		
		activeAbilities.add(FertilizeField.get());
		
		return activeAbilities;
	}

	@Override
	public List<Ability> getPassiveAbilities()
	{
		List<Ability> passiveAbilities = new ArrayList<Ability>();
		
		passiveAbilities.add(DoubleDropAndReplant.get());
		
		return passiveAbilities;
	}

}