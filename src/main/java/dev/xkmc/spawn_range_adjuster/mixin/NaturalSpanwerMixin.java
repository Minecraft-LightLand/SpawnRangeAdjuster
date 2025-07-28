package dev.xkmc.spawn_range_adjuster.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.spawn_range_adjuster.init.SRAConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NaturalSpawner.class)
public class NaturalSpanwerMixin {

	@Inject(method = "spawnForChunk", at = @At("HEAD"), cancellable = true)
	private static void spawnRangeAdjuster$spawnEarlyTermination(ServerLevel sl, LevelChunk c, NaturalSpawner.SpawnState state, boolean friendly, boolean hostile, boolean persistent, CallbackInfo ci) {
		if (!persistent && (sl.random.nextFloat() < SRAConfig.COMMON.skipSpawnChance.get())) {
			ci.cancel();
			return;
		}
		int dist = SRAConfig.COMMON.maxSpawnRange.get();
		if (dist > 100) return;
		var middle = c.getPos().getMiddleBlockPosition(64);
		for (var e : sl.getPlayers(EntitySelector.NO_SPECTATORS)) {
			var horDist = e.position().subtract(middle.getCenter()).horizontalDistance();
			if (horDist < dist + 8) {
				return;
			}
		}
		ci.cancel();
	}

	@WrapOperation(method = "spawnCategoryForPosition(Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;Lnet/minecraft/world/level/NaturalSpawner$AfterSpawnCallback;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getNearestPlayer(DDDDZ)Lnet/minecraft/world/entity/player/Player;"))
	private static Player spawnRangeAdjuster$getNearestPlayer(ServerLevel sl, double x, double y, double z, double r, boolean flag, Operation<Player> original) {
		int dist = SRAConfig.COMMON.maxSpawnRange.get();
		var ans = original.call(sl, x, y, z, (double) dist, flag);
		if (ans == null) return null;
		int ydiff = SRAConfig.COMMON.maxSpawnYDiff.get();
		if (Math.abs(ans.position().y() - y) > ydiff)
			return null;
		return ans;
	}
}
