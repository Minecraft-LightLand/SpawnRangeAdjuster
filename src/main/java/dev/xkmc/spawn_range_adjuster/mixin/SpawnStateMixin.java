package dev.xkmc.spawn_range_adjuster.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.spawn_range_adjuster.init.SRAConfig;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NaturalSpawner.SpawnState.class)
public class SpawnStateMixin {

	@WrapOperation(method = "canSpawnForCategory",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/MobCategory;getMaxInstancesPerChunk()I"))
	private int spawnRangeAdjuster$getMaxInstancePerChunk(MobCategory instance, Operation<Integer> original) {
		if (instance == MobCategory.MONSTER) {
			return SRAConfig.COMMON.maxMonsterPerChunk.get();
		}
		return (int) (original.call(instance) * SRAConfig.COMMON.maxCreaturePerChunkFactor.get());
	}


	@WrapOperation(method = "canSpawnForCategory",
			at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2IntOpenHashMap;getInt(Ljava/lang/Object;)I"))
	private int spawnRangeAdjuster$getCurrent(Object2IntOpenHashMap instance, Object k, Operation<Integer> original) {
		int ans = original.call(instance, k);
		if (k instanceof MobCategory c) {
			int max;
			if (c == MobCategory.MONSTER) {
				max = SRAConfig.COMMON.maxTotalMonster.get();
			} else {
				max = (int) (SRAConfig.COMMON.maxTotalCreatureFactor.get() * c.getMaxInstancesPerChunk());
			}
			if (max > 0 && ans > max) {
				return ans * 100;
			}
		}
		return ans;
	}

}
