package dk.muj.derius.farming;

import dk.muj.derius.entity.DeriusSkill;
import dk.muj.derius.farming.entity.MConf;

public class FarmingSkill extends DeriusSkill
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
