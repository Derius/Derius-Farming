package dk.muj.derius.farming;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.mutable.MutableBoolean;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.CocoaPlant;

import com.massivecraft.massivecore.particleeffect.ParticleEffect;

import dk.muj.derius.api.ability.AbilityAbstract;
import dk.muj.derius.api.player.DPlayer;
import dk.muj.derius.api.req.ReqCooldownIsExpired;
import dk.muj.derius.api.req.ReqHasEnoughStamina;
import dk.muj.derius.api.req.ReqIsAtleastLevel;
import dk.muj.derius.api.skill.Skill;
import dk.muj.derius.api.util.AbilityUtil;
import dk.muj.derius.lib.BlockUtil;

public class FertilizeField extends AbilityAbstract<Block>
{
	private static FertilizeField i = new FertilizeField();
	public static FertilizeField get() { return i; }
	
	// -------------------------------------------- //
	// DESCRIPTION
	// -------------------------------------------- //
	
	public FertilizeField()
	{
		this.setName("Fertilize Field");
		
		this.setDesc("Fertilizes a field of plants");
		
		this.setType(AbilityType.ACTIVE);
		this.setCooldownMillis(1000);//1000 * 5 * 60);
		this.setStaminaUsage(2); //50.0);
		
		this.addActivateRequirements(ReqIsAtleastLevel.get( () -> FarmingSkill.getFertilizeFieldMinLvl()));
		this.addActivateRequirements(ReqCooldownIsExpired.get());
		this.addActivateRequirements(ReqHasEnoughStamina.get());
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
	// LVL DESCRIPTION MSG
	// -------------------------------------------- //
	
	@Override
	public Optional<String> getLvlDescriptionMsg(int lvl)
	{
		return Optional.of("Fertilizes a field with the radius <h>" + String.valueOf(getFertilizationRadius(lvl)));
	}

	private static double getFertilizationRadius(int lvl)
	{
		lvl = Math.max(lvl - FarmingSkill.getFertilizeFieldMinLvl(), 0);
		double baseRadius = FarmingSkill.getFertilizeFieldBaseRadius();
		double steps = lvl / FarmingSkill.getFertilizeFieldRadiusStepPerLevels();
		
		return baseRadius + (steps * FarmingSkill.getFertilizeFieldRadiusPerStep());
	}
	

	// -------------------------------------------- //
	// ABILITY ACTIVATION
	// -------------------------------------------- //

	@Override
	public Object onActivate(DPlayer dplayer, Block block)
	{
		if (block == null) return null;
		
		int level = dplayer.getLvl(FarmingSkill.get());
		
		// Getting the Field
		Set<Block> field = getField(block, level);
		if (field == null || field.isEmpty()) return AbilityUtil.CANCEL;
		
		// Apply growth for each
		field.forEach(b -> applyGrowth(b, level));
		
		return null;
	}
	
	@Override
	public void onDeactivate(DPlayer dplayer, Object other) { }
	
	// -------------------------------------------- //
	// GROWTH
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	private void applyGrowth(Block block, int level)
	{
		byte data = (byte) (block.getData());
		
		// If it is already fully grown, return.
		if (BlockUtil.isGrowthStateFull(block.getState())) return;

		// Do math to find out how much the field grows and limit it
		level = Math.max(level - FarmingSkill.getFertilizeFieldMinLvl(), 0);
		double steps = level / FarmingSkill.getFertilizeFieldGrowthStepsPerLevels();
		byte additionalData = (byte) (steps * Math.random() * FarmingSkill.getFertilizeFieldGrowthAmountPerStep());
		additionalData = (byte) Math.min(additionalData, FarmingSkill.getFertilizeFieldGrowthMax());
		
		// Limit number to fully grown and set 
		byte limitedData = (byte) Math.min(data + additionalData, getDataOfFullyGrown(block));
		limitedData = (byte) Math.max(data, 0);
		
		// Cocoas are special special
		if (block.getType() == Material.COCOA)
		{
			if(additionalData <= 1) block.setData((byte) (data + 4));
		}
		else
		{
			block.setData(limitedData);
		}
		
		ParticleEffect.DRIP_WATER.display((float) 0.3, (float) 0.3, (float) 0.3, (float) 0.02, 30, block.getLocation(), 32);
		return;
	}
	
	private byte getDataOfFullyGrown(Block block)
	{
		// Every "crop" has it's own grown data
		// Visit http://minecraft.gamepedia.com/Data_values for more information
		Material mat = block.getType();
		
		if (mat == Material.CROPS || mat == Material.CARROT || mat == Material.POTATO)
		{
			return 7;
		}
		else if (mat == Material.NETHER_WARTS)
		{
			return 3;
		}
		else if (mat == Material.COCOA)
		{
			// Cocoa plants are the weirdest plant to handle
			byte data = 0;
			CocoaPlant plant = (CocoaPlant) block.getState().getData();
			BlockFace face = plant.getFacing();
			
			// The blockface it faces changes its byte value
			if (face == BlockFace.WEST)
			{
				data += 1;
			}
			else if (face == BlockFace.NORTH)
			{
				data += 2;
			}
			else if (face == BlockFace.EAST)
			{
				data += 3;
			}
			
			// And the fully grown state should be 8. This adds up to the result.
			return (byte) (data + 8);
		}
		else
		{
			// Default, we just set it to seven. The probability of this being correct if new materials are added is really high.
			return 7;
		}
	}

	// -------------------------------------------- //
	// FIELD
	// -------------------------------------------- //
	
	// Thanks for this beautiful stream in Woodcutting Madus! :D
	public static Set<Block> getField(final Block source, int level)
	{
		// We create a return...
		Set<Block> ret = new HashSet<>();
		// ... which initially contains the source block.
		ret.add(source);
		
		// This boolean determines if we should look any further.
		// it is set to false if an operation doesn't add anything to the return value.
		MutableBoolean someLeft = new MutableBoolean(true);
		
		// The latest added blocks, we use this to prevent looking through
		// the same blocks multiple times.
		// Initially it is just the source block.
		Set<Block> latest = ret;
		  
		while (someLeft.booleanValue())
		{
			// The blocks we are going to add next time we modify return.
			Set<Block> add = new HashSet<Block>();
			
			// For all the latest added blocks, we look through
			// the nearest ones to add to the three.
			latest.forEach( (Block block) ->
				add.addAll(BlockUtil.getSurroundingBlocks(block)
							.stream()
							// Of course it must be a field material and the same as the source
							.filter(b -> isMaterialOk(b.getState()))
							.filter(b -> isMaterialSame(source.getType(), b.getType()))
							// And it may not be too far away.
							.filter(b -> isLocationOk(source.getState(), b.getState(), level))
							.collect(Collectors.toSet()))
			);
			// So if true is returned, we modified the return...
			// and there might still be wood to find.
			someLeft.setValue(ret.addAll(add));
			// The latest added blocks are the ones we just added.
			latest = add;	
		}
		// Filter out the soil material before returning it.
		return ret.stream().filter(b -> ! FarmingSkill.getFertilizeFieldSoilMaterials().contains(b.getType())).collect(Collectors.toSet());
	}

	private static boolean isLocationOk(BlockState source, BlockState compared, int level)
	{
		// We calculate the radius with the methode.
		int radius = (int) Math.round(getFertilizationRadius(level));
		
		// Check for all three dimensions, this is way less heavy then sqrt
		// If distance on the axes is too much, location isn't ok.
		if (Math.abs(source.getX() - compared.getX()) > radius) return false;
		if (Math.abs(source.getY() - compared.getY()) > radius) return false;
		if (Math.abs(source.getZ() - compared.getZ()) > radius) return false;
		
		// It passed all the checks.
		return true;
	}
	
	private static boolean isMaterialOk(BlockState source)
	{
		Material material = source.getType();
		if (FarmingSkill.getFertilizeFieldMaterials().contains(material)) return true;
		if (FarmingSkill.getFertilizeFieldSoilMaterials().contains(material)) return true;
		
		return false;
	}
	
	private static boolean isMaterialSame(Material sourceMat, Material compareMat)
	{
		// Is it the same?
		if (sourceMat == compareMat) return true;
		// Is it the soil? This allows for 3D grabbing of a field
		if (FarmingSkill.getFertilizeFieldSoilMaterials().contains(compareMat)) return true;
		
		return false;
	}

}
