package dev.xkmc.spawn_range_adjuster.init;

import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SpawnRangeAdjuster.MODID)
@Mod.EventBusSubscriber(modid = SpawnRangeAdjuster.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpawnRangeAdjuster {

	public static final String MODID = "spawn_range_adjuster";
	public static final Logger LOGGER = LogManager.getLogger();

	public SpawnRangeAdjuster() {
		SRAConfig.init();
	}

	@SubscribeEvent
	public static void registerCaps(RegisterCapabilitiesEvent event) {
	}

}
