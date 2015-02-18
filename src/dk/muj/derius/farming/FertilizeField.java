package dk.muj.derius.farming;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.ability.DeriusAbility;
import dk.muj.derius.req.ReqIsAtleastLevel;

public class FertilizeField extends DeriusAbility implements Ability
{
	private static FertilizeField i = new FertilizeField();
	public static FertilizeField get() { return i; }
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	public FertilizeField()
	{
		this.setName("Fertilize Field");
		
		this.setDesc("Fertilizes a field");
		
		this.setType(AbilityType.ACTIVE);
		
		this.addActivateRequirements(ReqIsAtleastLevel.get(FarmingSkill.getFertilizeFieldMinLvl()));
	}
	
	// -------------------------------------------- //
	// SKILL & ID
	// -------------------------------------------- //
	
	@Override
	public String getId()
	{
		return "derius:farming:fertilizefield";
	}
	
	@Override
	public Skill getSkill()
	{
		return FarmingSkill.get();
	}

	// -------------------------------------------- //
	// ABILITY ACTIVATION
	// -------------------------------------------- //

	@Override
	public Object onActivate(DPlayer p, Object other)
	{
		// TODO: Add fertilization skill by radius
		return null;
	}
	
	@Override
	public void onDeactivate(DPlayer p, Object other)
	{
		// Nothing at the moment.
	}
	
	// -------------------------------------------- //
	// Level description
	// -------------------------------------------- //
	
	// TODO: Add in actual radius method
	@Override
	public String getLvlDescriptionMsg(int lvl)
	{
		int radius = 1;
		return Txt.parse("Fertilizes a field with the radius %s", radius);
	}

}
