package dev.xkmc.spawn_range_adjuster.init;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class SRAConfig {

	public static class Common {

		public final ForgeConfigSpec.IntValue maxSpawnRange;
		public final ForgeConfigSpec.IntValue maxSpawnYDiff;
		public final ForgeConfigSpec.IntValue maxMonsterPerChunk;
		public final ForgeConfigSpec.IntValue maxTotalMonster;
		public final ForgeConfigSpec.DoubleValue maxCreaturePerChunkFactor;
		public final ForgeConfigSpec.DoubleValue maxTotalCreatureFactor;

		Common(ForgeConfigSpec.Builder builder) {
			maxSpawnRange = builder.comment("Maximum spawn distance to player")
					.defineInRange("maxSpawnRange", 128, 0, 128);

			maxSpawnYDiff = builder.comment("Maximum spawn vertical distance to player")
					.defineInRange("maxSpawnYDiff", 128, 0, 128);

			maxMonsterPerChunk = builder.comment("Maximum number of monster per 289 chunks")
					.defineInRange("maxMonsterPerChunk", 70, 0, 128);

			maxTotalMonster = builder.comment("Maximum number of monster in total. 0 means unlimited")
					.defineInRange("maxTotalMonster", 0, 0, 10000);

			maxCreaturePerChunkFactor = builder.comment("Maximum number of each types of creatures per 289 chunks, as a factor of vanilla limits")
					.defineInRange("maxCreaturePerChunkFactor", 1d, 0, 1);

			maxTotalCreatureFactor = builder.comment("Maximum number of creatures in total for each types, as a factor of vanilla limits per 289 chunks. 0 means unlimited")
					.defineInRange("maxTotalCreatureFactor", 0d, 0, 100);

		}

	}

	public static final ForgeConfigSpec COMMON_SPEC;
	public static final Common COMMON;

	static {

		final Pair<Common, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = common.getRight();
		COMMON = common.getLeft();
	}

	/**
	 * Registers any relevant listeners for config
	 */
	public static void init() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);
	}


}
