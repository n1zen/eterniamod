package n1zen.eterniamod.mixin;

import n1zen.eterniamod.blocks.PlacedBlockAttachment;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockPlacementMixin {
    @Inject(method = "setPlacedBy", at = @At("TAIL"))
    private void onSetPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack, CallbackInfo ci) {
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel && placer instanceof ServerPlayer) {
            PlacedBlockAttachment.markPlaced(serverLevel, pos);
        }
    }
}
