package dk.muj.derius.farming;

import java.util.Optional;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.ability.AbilityAbstract;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.req.ReqIsAtleastLevel;
import dk.muj.derius.api.skill.Skill;

public class FertilizeField extends AbilityAbstract
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
		
		this.addActivateRequirements(ReqIsAtleastLevel.get( () -> FarmingSkill.getFertilizeFieldMinLvl() ));
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
	public Optional<String> getLvlDescriptionMsg(int lvl)
	{
		int radius = 1;
		return Optional.of(Txt.parse("Fertilizes a field with the radius %s", radius));
	}

}
