package dk.muj.derius.farming;

import com.massivecraft.massivecore.MassivePlugin;

import dk.muj.derius.api.DeriusAPI;

public class DeriusFarming extends MassivePlugin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static DeriusFarming i;
	public static DeriusFarming get() { return i; }
	public DeriusFarming() { i = this; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void onEnable()
	{
		if ( ! super.preEnable()) return;
			
		FarmingSkill.get().register();
		FertilizeField.get().register();
		DoubleDropAndReplant.get().register();
		CarefulHarvesting.get().register();
		
		EngineFarming.get().activate();
		
		DeriusAPI.registerPreparableTools(EngineFarming.HOE_MATERIALS);
		DeriusAPI.addBlockTypesToListenFor(FarmingSkill.getExpGain().keySet());
		DeriusAPI.registerExpGain(FarmingExpGain.get());
		
		super.postEnable();
	}
}
