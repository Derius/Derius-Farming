package dk.muj.derius.farming;

import org.bukkit.Material;

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
		
		// Implement config here
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public String getId()
	{
		return MConf.get().getSkillId;
	}

}
