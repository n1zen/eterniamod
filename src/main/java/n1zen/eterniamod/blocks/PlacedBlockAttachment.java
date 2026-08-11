package n1zen.eterniamod.blocks;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static n1zen.eterniamod.Eterniamod.MOD_ID;

public class PlacedBlockAttachment {
    private static final Codec<Set<BlockPos>> PLACED_POSITIONS_CODEC =
            BlockPos.CODEC.listOf().xmap(
                    HashSet::new,
                    ArrayList::new
            );

    public static final AttachmentType<Set<BlockPos>> ATTACHMENT = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MOD_ID, "placed_positions"),
            builder -> builder.persistent(PLACED_POSITIONS_CODEC)
                    .initializer(HashSet::new)
    );

    public static void markPlaced(ServerLevel level, BlockPos pos) {
        ChunkAccess chunkAccess = level.getChunk(pos);

        Set<BlockPos> updated = new HashSet<>(chunkAccess.getAttachedOrCreate(ATTACHMENT));
        updated.add(pos);
        chunkAccess.setAttached(ATTACHMENT, updated);
    }

    public static boolean isPlacedAndUnmark(ServerLevel level, BlockPos pos) {
        ChunkAccess chunkAccess = level.getChunk(pos);

        Set<BlockPos> updated = new HashSet<>(chunkAccess.getAttachedOrCreate(ATTACHMENT));
        boolean wasPlaced = updated.remove(pos);

        if(wasPlaced) {
            chunkAccess.setAttached(ATTACHMENT, updated);
        }

        return wasPlaced;
    }
}
