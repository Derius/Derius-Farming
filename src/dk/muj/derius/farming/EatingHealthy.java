package dk.muj.derius.farming;

import java.util.Optional;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.util.Txt;

import dk.muj.derius.api.ability.AbilityAbstract;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.skill.Skill;

public class EatingHealthy extends AbilityAbstract<Integer>
{
	private static EatingHealthy i = new EatingHealthy();
	public static EatingHealthy get() { return i; }
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	public EatingHealthy()
	{
		this.setName("Eating Healthy");
		
		this.setDesc("Gives you more food regeneration for eating healthy foods.");
		
		this.setType(AbilityType.PASSIVE);
	}
	
	// -------------------------------------------- //
	// ABILITY ACTIVATION
	// -------------------------------------------- //
	
	@Override
	public Object onActivate(DPlayer dplayer, Integer foodGained)
	{
		Player player = dplayer.getPlayer();
		int currentFood = player.getFoodLevel();
		
		int extra = (int) Math.round(foodGained.intValue() * getPercentage(dplayer.getLvl(getSkill()))) - foodGained;
		
		player.setFoodLevel(Math.max(currentFood + extra, 20));
		return null;
	}

	@Override
	public void onDeactivate(DPlayer dplayer, Object other) { }
	
	// -------------------------------------------- //
	// SKILL & ID
	// -------------------------------------------- //
	
	@Override
	public String getId()
	{
		return "derius.farming.eating_healty";
	}

	@Override
	public Skill getSkill()
	{
		return FarmingSkill.get();
	}

	// -------------------------------------------- //
	// Level description
	// -------------------------------------------- //
	
	@Override
	public Optional<String> getLvlDescriptionMsg(int lvl)
	{
		return Optional.of(Txt.parse("Multiplies the hunger you get from healthy foods by %s.2", getPercentage(lvl)));
	}

	private double getPercentage(int lvl)
	{
		double amount = FarmingSkill.getEatingHealthyBaseMultiplier();
		amount += lvl/FarmingSkill.getEatingHealthyStepPerLevels()*FarmingSkill.getEatingHealthyAmountPerStep();
		
		return amount;
	}

}
