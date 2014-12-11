package dk.muj.farming;

import dk.muj.derius.entity.MPlayer;
import dk.muj.derius.skill.Skill;
import dk.muj.derius.farming.entity.MConf;

public class FarmingSkill extends Skill
{
	
	private static FarmingSkill i = new FarmingSkill();
	public static FarmingSkill get() { return i; }
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	public FarmingSkill()
	{
		this.setName("Farming");
		this.setDescription("Makes you better at farming.");
		
		this.addEarnExpDesc("Harvest crops and other vegetables!");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public int getId()
	{
		return MConf.get().getSkillId;
	}

	@Override
	public boolean CanPlayerLearnSkill(MPlayer p)
	{
		return true;
	}
}
