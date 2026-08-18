package n1zen.eterniamod.skills.xp.rewards;

import n1zen.eterniamod.skills.SkillType;
import n1zen.eterniamod.skills.xp.rewards.reward.BlockXpReward;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;

public class BlockXpRewards {
    public static final Map<Block, BlockXpReward> REWARDS = new HashMap<>();

    static {

        // Mining

        // Coal
        REWARDS.put(Blocks.COAL_ORE, new BlockXpReward(SkillType.MINING, 3));
        REWARDS.put(Blocks.DEEPSLATE_COAL_ORE, new BlockXpReward(SkillType.MINING, 3));

        // Copper
        REWARDS.put(Blocks.COPPER_ORE, new BlockXpReward(SkillType.MINING, 5));
        REWARDS.put(Blocks.DEEPSLATE_COPPER_ORE, new BlockXpReward(SkillType.MINING, 5));
        REWARDS.put(Blocks.RAW_COPPER_BLOCK, new BlockXpReward(SkillType.MINING, 12));

        // Iron
        REWARDS.put(Blocks.IRON_ORE, new BlockXpReward(SkillType.MINING, 12));
        REWARDS.put(Blocks.DEEPSLATE_IRON_ORE, new BlockXpReward(SkillType.MINING, 12));
        REWARDS.put(Blocks.RAW_IRON_BLOCK, new BlockXpReward(SkillType.MINING, 18));

        // Lapis
        REWARDS.put(Blocks.LAPIS_ORE, new BlockXpReward(SkillType.MINING, 12));
        REWARDS.put(Blocks.DEEPSLATE_LAPIS_ORE, new BlockXpReward(SkillType.MINING, 12));

        // Redstone
        REWARDS.put(Blocks.REDSTONE_ORE, new BlockXpReward(SkillType.MINING, 15));
        REWARDS.put(Blocks.DEEPSLATE_REDSTONE_ORE, new BlockXpReward(SkillType.MINING, 15));

        // Quartz
        REWARDS.put(Blocks.NETHER_QUARTZ_ORE, new BlockXpReward(SkillType.MINING, 15));

        // Gold
        REWARDS.put(Blocks.GOLD_ORE, new BlockXpReward(SkillType.MINING, 18));
        REWARDS.put(Blocks.DEEPSLATE_GOLD_ORE, new BlockXpReward(SkillType.MINING, 18));
        REWARDS.put(Blocks.NETHER_GOLD_ORE, new BlockXpReward(SkillType.MINING, 5));
        REWARDS.put(Blocks.GILDED_BLACKSTONE, new BlockXpReward(SkillType.MINING, 5));
        REWARDS.put(Blocks.GOLD_BLOCK, new BlockXpReward(SkillType.MINING, 30));

        // Emerald
        REWARDS.put(Blocks.EMERALD_ORE, new BlockXpReward(SkillType.MINING, 40));
        REWARDS.put(Blocks.DEEPSLATE_EMERALD_ORE, new BlockXpReward(SkillType.MINING, 40));

        // Diamond
        REWARDS.put(Blocks.DIAMOND_ORE, new BlockXpReward(SkillType.MINING, 35));
        REWARDS.put(Blocks.DEEPSLATE_DIAMOND_ORE, new BlockXpReward(SkillType.MINING, 35));

        // Ancient Debris
        REWARDS.put(Blocks.ANCIENT_DEBRIS, new BlockXpReward(SkillType.MINING, 60));

        // Farming

        REWARDS.put(Blocks.WHEAT, new BlockXpReward(SkillType.FARMING, 5));
        REWARDS.put(Blocks.POTATOES, new BlockXpReward(SkillType.FARMING, 5));
        REWARDS.put(Blocks.CARROTS, new BlockXpReward(SkillType.FARMING, 5));
        REWARDS.put(Blocks.BEETROOTS, new BlockXpReward(SkillType.FARMING, 5));

        // Foraging

        // Wood logs
        REWARDS.put(Blocks.ACACIA_LOG, new BlockXpReward(SkillType.FORAGING, 5));
        REWARDS.put(Blocks.BIRCH_LOG, new BlockXpReward(SkillType.FORAGING, 5));
        REWARDS.put(Blocks.CHERRY_LOG, new BlockXpReward(SkillType.FORAGING, 5));
        REWARDS.put(Blocks.DARK_OAK_LOG, new BlockXpReward(SkillType.FORAGING, 5));
        REWARDS.put(Blocks.JUNGLE_LOG, new BlockXpReward(SkillType.FORAGING, 5));
        REWARDS.put(Blocks.OAK_LOG, new BlockXpReward(SkillType.FORAGING, 5));
        REWARDS.put(Blocks.MANGROVE_LOG, new BlockXpReward(SkillType.FORAGING, 5));
        REWARDS.put(Blocks.SPRUCE_LOG, new BlockXpReward(SkillType.FORAGING, 5));
        REWARDS.put(Blocks.PALE_OAK_LOG, new BlockXpReward(SkillType.FORAGING, 5));
        REWARDS.put(Blocks.CRIMSON_STEM, new BlockXpReward(SkillType.FORAGING, 20));
        REWARDS.put(Blocks.WARPED_STEM, new BlockXpReward(SkillType.FORAGING, 20));

        // Mushrooms
        REWARDS.put(Blocks.MUSHROOM_STEM, new BlockXpReward(SkillType.FORAGING, 10));

        REWARDS.put(Blocks.BROWN_MUSHROOM, new BlockXpReward(SkillType.FORAGING, 5));
        REWARDS.put(Blocks.BROWN_MUSHROOM_BLOCK, new BlockXpReward(SkillType.FORAGING, 15));

        REWARDS.put(Blocks.RED_MUSHROOM, new BlockXpReward(SkillType.FORAGING, 5));
        REWARDS.put(Blocks.RED_MUSHROOM_BLOCK, new BlockXpReward(SkillType.FORAGING, 15));

        // Berries
        REWARDS.put(Blocks.SWEET_BERRY_BUSH, new BlockXpReward(SkillType.FORAGING, 8));
        REWARDS.put(Blocks.CAVE_VINES_PLANT, new BlockXpReward(SkillType.FORAGING, 10));

        // Cacti
        REWARDS.put(Blocks.CACTUS, new BlockXpReward(SkillType.FORAGING, 5));
        REWARDS.put(Blocks.CACTUS_FLOWER, new BlockXpReward(SkillType.FORAGING, 15));

        // Sugar Cane and Bamboo
        REWARDS.put(Blocks.SUGAR_CANE, new BlockXpReward(SkillType.FORAGING, 5));
        REWARDS.put(Blocks.BAMBOO, new BlockXpReward(SkillType.FORAGING, 5));

        // Kelp
        REWARDS.put(Blocks.KELP, new BlockXpReward(SkillType.FORAGING, 5));
        REWARDS.put(Blocks.KELP_PLANT, new BlockXpReward(SkillType.FORAGING, 5));

        // Chorus
        REWARDS.put(Blocks.CHORUS_FLOWER, new BlockXpReward(SkillType.FORAGING, 15));
        REWARDS.put(Blocks.CHORUS_PLANT, new BlockXpReward(SkillType.FORAGING, 15));

        // Cocoa
        REWARDS.put(Blocks.COCOA, new BlockXpReward(SkillType.FORAGING, 6));
    }
}
