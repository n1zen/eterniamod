package n1zen.eterniamod.skills.xp.rewards;

import n1zen.eterniamod.skills.SkillType;
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

        // Iron
        REWARDS.put(Blocks.IRON_ORE, new BlockXpReward(SkillType.MINING, 12));
        REWARDS.put(Blocks.DEEPSLATE_IRON_ORE, new BlockXpReward(SkillType.MINING, 12));

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

        // Emerald
        REWARDS.put(Blocks.EMERALD_ORE, new BlockXpReward(SkillType.MINING, 40));
        REWARDS.put(Blocks.DEEPSLATE_EMERALD_ORE, new BlockXpReward(SkillType.MINING, 40));

        // Diamond
        REWARDS.put(Blocks.DIAMOND_ORE, new BlockXpReward(SkillType.MINING, 35));
        REWARDS.put(Blocks.DEEPSLATE_DIAMOND_ORE, new BlockXpReward(SkillType.MINING, 35));

        // Ancient Debris
        REWARDS.put(Blocks.ANCIENT_DEBRIS, new BlockXpReward(SkillType.MINING, 60));
    }
}
