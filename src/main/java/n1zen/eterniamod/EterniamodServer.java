package n1zen.eterniamod;

import n1zen.eterniamod.blocks.PlacedBlockAttachment;
import n1zen.eterniamod.commands.skills.exp.ShowAllSkillExpSelf;
import n1zen.eterniamod.commands.skills.exp.ShowSpecificSkillExpOther;
import n1zen.eterniamod.commands.skills.exp.ShowSpecificSkillExpSelf;
import n1zen.eterniamod.commands.skills.admin.AddXp;
import n1zen.eterniamod.commands.skills.admin.ClearXp;
import n1zen.eterniamod.commands.skills.level.ShowAllSkillLevelSelf;
import n1zen.eterniamod.commands.skills.level.ShowSpecificSkillLevelOther;
import n1zen.eterniamod.commands.skills.level.ShowSpecificSkillLevelSelf;
import n1zen.eterniamod.skills.PlayerSkillLevelState;
import n1zen.eterniamod.skills.PlayerSkillXpState;
import n1zen.eterniamod.skills.SkillType;
import n1zen.eterniamod.skills.level.effects.CombatEffects;
import n1zen.eterniamod.skills.level.effects.MiningEffects;
import n1zen.eterniamod.skills.xp.rewards.reward.BlockXpReward;
import n1zen.eterniamod.skills.xp.rewards.BlockXpRewards;
import n1zen.eterniamod.skills.xp.rewards.reward.EntityXpReward;
import n1zen.eterniamod.skills.xp.rewards.EntityXpRewards;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static n1zen.eterniamod.Eterniamod.MOD_ID;

public class EterniamodServer implements DedicatedServerModInitializer {

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static Holder<Enchantment> silkTouch;

    @Override
    public void onInitializeServer() {

        ServerLifecycleEvents.SERVER_STARTING.register(server -> LOGGER.info("Eterniamod initializing..."));

        ServerLifecycleEvents.SERVER_STARTED.register(server -> LOGGER.info("Eterniamod has been initialized!"));

        ServerPlayerEvents.JOIN.register(player -> {
           PlayerSkillLevelState lvlState = PlayerSkillLevelState.get(player.level());
           for(SkillType skill :  SkillType.values()) {
               int skillLevel = lvlState.getSkillLevel(player.getUUID(), skill);
               applySkillEffect(player, lvlState, skillLevel, skill);
           }
        });

        PlayerBlockBreakEvents.AFTER.register((level, player, blockPos, blockState, block) -> {
            if (!level.isClientSide()) {
                if(player.isCreative()) {
                    return;
                }
                BlockBreakGainXp((ServerLevel) level, player, blockState, blockPos);
            }
        });

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if(damageSource.getEntity() instanceof Player) {
                ServerPlayer serverPlayer = (ServerPlayer) damageSource.getEntity();
                if(!serverPlayer.isCreative()) {
                    EntityKillGainXp(serverPlayer, entity);
                }
            }
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

            // Show All Skills of Self
            ShowAllSkillExpSelf.register(dispatcher);
            ShowAllSkillLevelSelf.register(dispatcher);

            // Show Specific Skills of Self
            ShowSpecificSkillExpSelf.register(dispatcher);
            ShowSpecificSkillLevelSelf.register(dispatcher);

            // Show Specific Skills of Other Players
            ShowSpecificSkillExpOther.register(dispatcher);
            ShowSpecificSkillLevelOther.register(dispatcher);

            // Add XP to a player's skill (OP only)
            AddXp.register(dispatcher);

            // Clear skill xp of a player (OP only)
            ClearXp.register(dispatcher);

        });
    }

    private static boolean isFarmedPumpkinOrMelon(ServerLevel level, BlockPos pos) {
        List<Block> blocks = List.of(
                level.getBlockState(pos.north()).getBlock(),
                level.getBlockState(pos.east()).getBlock(),
                level.getBlockState(pos.south()).getBlock(),
                level.getBlockState(pos.west()).getBlock()
        );
        for(Block block : blocks) {
            if(block == Blocks.MELON_STEM || block == Blocks.PUMPKIN_STEM) {
                return true;
            }
        }
        return false;
    }

    private static void EntityKillGainXp(ServerPlayer player, Entity entityKilled) {
        EntityType<?> entityType = entityKilled.getType();
        EntityXpReward xpRewards = EntityXpRewards.REWARDS.get(entityType);

        ServerLevel level = player.level();
        giveRewardToPlayer(level, player, xpRewards);
    }

    private static void BlockBreakGainXp(ServerLevel level, Player player, BlockState blockState, BlockPos pos) {
        Block brokenBlock = blockState.getBlock();
        if (PlacedBlockAttachment.isPlacedAndUnmark(level, pos)) {
            return;
        }


        BlockXpReward reward = getBlockReward(level, pos, brokenBlock);


        if (reward != null && reward.skill() == SkillType.MINING) {
            double chance = getMultiplyOreChance(level, player);

            if(Math.random() < chance)
                MultiplyOre(level, player, blockState, pos);
        }

        giveRewardToPlayer(level, player, reward);
    }

    private static double getMultiplyOreChance(ServerLevel level, Player player) {
        UUID playerUUID =  player.getUUID();
        PlayerSkillLevelState levelState = PlayerSkillLevelState.get(level);

        int miningLevel =  levelState.getSkillLevel(playerUUID, SkillType.MINING);
        return miningLevel * 0.016;
    }
    private static void MultiplyOre(ServerLevel level, Player player, BlockState blockState, BlockPos pos) {
        ItemStack tool = player.getMainHandItem();

        ItemEnchantments itemEnchantments = tool.getEnchantments();

        if (hasSilkTouch(level, itemEnchantments)) return;

        LootParams.Builder lootParams = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.TOOL, tool)
                .withOptionalParameter(LootContextParams.THIS_ENTITY, player);

        List<ItemStack> drops = blockState.getDrops(lootParams);

        for(ItemStack drop : drops) {
            Block.popResource(level, pos, drop.copy());
        }
    }

    private static boolean hasSilkTouch(ServerLevel level, ItemEnchantments itemEnchantments) {
        if (silkTouch == null) {
            silkTouch = level.registryAccess()
                    .lookupOrThrow(Registries.ENCHANTMENT)
                    .getOrThrow(Enchantments.SILK_TOUCH);
        }
        return itemEnchantments.getLevel(silkTouch) > 0;
    }

    // EntityXpReward
    private static void giveRewardToPlayer(ServerLevel level, Player player, EntityXpReward reward) {
        if (reward != null) {
            double amount = reward.amount();
            SkillType skillType = reward.skill();

            UUID playerUUID = player.getUUID();

            PlayerSkillLevelState lvlState = PlayerSkillLevelState.get(level);
            PlayerSkillXpState xpState = PlayerSkillXpState.get(level);

            xpState.addSkillExp(playerUUID, skillType, amount);

            double xp = xpState.getSkillExp(playerUUID, skillType);

            SkillLvlUp(player, lvlState, xp, playerUUID, skillType);
        }
    }

    // BlockXpReward
    private static void giveRewardToPlayer(ServerLevel level, Player player, BlockXpReward reward) {
        if (reward != null) {
            double amount = reward.amount();
            SkillType skillType = reward.skill();

            UUID playerUUID = player.getUUID();

            PlayerSkillLevelState lvlState = PlayerSkillLevelState.get(level);
            PlayerSkillXpState xpState = PlayerSkillXpState.get(level);

            xpState.addSkillExp(playerUUID, skillType, amount);

            double xp = xpState.getSkillExp(playerUUID, skillType);

            SkillLvlUp(player, lvlState, xp, playerUUID, skillType);
        }
    }

    private static BlockXpReward getBlockReward(ServerLevel level, BlockPos pos, Block brokenBlock) {
        BlockXpReward reward = BlockXpRewards.REWARDS.get(brokenBlock);

        if(brokenBlock == Blocks.PUMPKIN || brokenBlock == Blocks.MELON) {
            if (isFarmedPumpkinOrMelon(level, pos)) {
                reward = new BlockXpReward(SkillType.FARMING, 10);
            } else {
                reward = new BlockXpReward(SkillType.FORAGING, 25);
            }
        }
        return reward;
    }

    private static void SkillLvlUp(Player player, PlayerSkillLevelState lvlState, double xp, UUID playerUUID, SkillType skillType) {
        if (lvlState.levelledUp(xp, playerUUID, skillType)) {
            player.sendSystemMessage(
                    Component.literal(skillType.name() + " levelled up!")
            );

            int level = lvlState.getLvlForExp(xp);

            applySkillEffect((ServerPlayer) player, lvlState, level, skillType);
        }
    }

    private static void applySkillEffect(ServerPlayer player, PlayerSkillLevelState lvlState, int level, SkillType skillType) {
        switch (skillType) {
            case MINING:
                MiningEffects.applyMiningSpeedBonus(player, level);
                break;
            case COMBAT:
                CombatEffects.applyAttackDamageModifier(player, level);
                break;
            default:
                break;
        }
    }

}
