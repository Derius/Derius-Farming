package dk.muj.derius.farming;

import org.bukkit.Material;

import com.massivecraft.massivecore.collections.WorldExceptionSet;
import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.Ability;
import dk.muj.derius.api.DPlayer;
import dk.muj.derius.api.Skill;
import dk.muj.derius.entity.ability.DeriusAbility;
import dk.muj.derius.farming.entity.MConf;
import dk.muj.derius.req.ReqIsAtleastLevel;
import dk.muj.derius.util.Listener;

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
		
		Listener.registerBlockBreakKey(FarmingListener.get(),
				Material.WOOD_HOE,
				Material.STONE_HOE,
				Material.IRON_HOE,
				Material.GOLD_HOE,
				Material.DIAMOND_HOE);
		
		this.addActivateRequirements(ReqIsAtleastLevel.get(MConf.get().getFertilizeFieldMinLvl));
	}
	
	// -------------------------------------------- //
	// SKILL & ID
	// -------------------------------------------- //
	
	@Override
	public String getId()
	{
		return MConf.get().getFertilizeFieldId;
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

	@Override
	public void setWorldsEarn(WorldExceptionSet worldsUse)
	{
		// TODO Auto-generated method stub
		
	}
}
