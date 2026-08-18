package n1zen.eterniamod.skills.xp.rewards;

import n1zen.eterniamod.skills.SkillType;
import n1zen.eterniamod.skills.xp.rewards.reward.EntityXpReward;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;

import java.util.HashMap;
import java.util.Map;

public class EntityXpRewards {
    public static final Map<EntityType<?>, EntityXpReward> REWARDS = new HashMap<>();



    static {

        // Combat

        // Combat - PVP
        REWARDS.put(EntityTypes.PLAYER, new EntityXpReward(SkillType.COMBAT, 30));

        // Combat — common hostiles
        REWARDS.put(EntityTypes.ZOMBIE, new EntityXpReward(SkillType.COMBAT, 5));
        REWARDS.put(EntityTypes.HUSK, new EntityXpReward(SkillType.COMBAT, 6));
        REWARDS.put(EntityTypes.DROWNED, new EntityXpReward(SkillType.COMBAT, 6));
        REWARDS.put(EntityTypes.ZOMBIE_VILLAGER, new EntityXpReward(SkillType.COMBAT, 5));
        REWARDS.put(EntityTypes.SKELETON, new EntityXpReward(SkillType.COMBAT, 5));
        REWARDS.put(EntityTypes.STRAY, new EntityXpReward(SkillType.COMBAT, 6));
        REWARDS.put(EntityTypes.SPIDER, new EntityXpReward(SkillType.COMBAT, 4));
        REWARDS.put(EntityTypes.CAVE_SPIDER, new EntityXpReward(SkillType.COMBAT, 6));
        REWARDS.put(EntityTypes.SILVERFISH, new EntityXpReward(SkillType.COMBAT, 2));
        REWARDS.put(EntityTypes.ENDERMITE, new EntityXpReward(SkillType.COMBAT, 3));
        REWARDS.put(EntityTypes.SLIME, new EntityXpReward(SkillType.COMBAT, 3));
        REWARDS.put(EntityTypes.MAGMA_CUBE, new EntityXpReward(SkillType.COMBAT, 4));
        REWARDS.put(EntityTypes.CREEPER, new EntityXpReward(SkillType.COMBAT, 8));
        REWARDS.put(EntityTypes.ZOMBIFIED_PIGLIN, new EntityXpReward(SkillType.COMBAT, 6));
        REWARDS.put(EntityTypes.PIGLIN, new EntityXpReward(SkillType.COMBAT, 6));

        // Combat — moderate threats
        REWARDS.put(EntityTypes.ENDERMAN, new EntityXpReward(SkillType.COMBAT, 12));
        REWARDS.put(EntityTypes.WITCH, new EntityXpReward(SkillType.COMBAT, 10));
        REWARDS.put(EntityTypes.BLAZE, new EntityXpReward(SkillType.COMBAT, 12));
        REWARDS.put(EntityTypes.GHAST, new EntityXpReward(SkillType.COMBAT, 15));
        REWARDS.put(EntityTypes.PHANTOM, new EntityXpReward(SkillType.COMBAT, 8));
        REWARDS.put(EntityTypes.GUARDIAN, new EntityXpReward(SkillType.COMBAT, 12));
        REWARDS.put(EntityTypes.SHULKER, new EntityXpReward(SkillType.COMBAT, 15));
        REWARDS.put(EntityTypes.VEX, new EntityXpReward(SkillType.COMBAT, 6));
        REWARDS.put(EntityTypes.VINDICATOR, new EntityXpReward(SkillType.COMBAT, 12));
        REWARDS.put(EntityTypes.PILLAGER, new EntityXpReward(SkillType.COMBAT, 8));
        REWARDS.put(EntityTypes.HOGLIN, new EntityXpReward(SkillType.COMBAT, 12));
        REWARDS.put(EntityTypes.ZOGLIN, new EntityXpReward(SkillType.COMBAT, 12));

        // Combat — dangerous / rare
        REWARDS.put(EntityTypes.WITHER_SKELETON, new EntityXpReward(SkillType.COMBAT, 20));
        REWARDS.put(EntityTypes.EVOKER, new EntityXpReward(SkillType.COMBAT, 18));
        REWARDS.put(EntityTypes.ILLUSIONER, new EntityXpReward(SkillType.COMBAT, 20));
        REWARDS.put(EntityTypes.RAVAGER, new EntityXpReward(SkillType.COMBAT, 25));
        REWARDS.put(EntityTypes.PIGLIN_BRUTE, new EntityXpReward(SkillType.COMBAT, 15));
        REWARDS.put(EntityTypes.ELDER_GUARDIAN, new EntityXpReward(SkillType.COMBAT, 30));

        // Combat — bosses
        REWARDS.put(EntityTypes.WARDEN, new EntityXpReward(SkillType.COMBAT, 60));
        REWARDS.put(EntityTypes.WITHER, new EntityXpReward(SkillType.COMBAT, 100));
        REWARDS.put(EntityTypes.ENDER_DRAGON, new EntityXpReward(SkillType.COMBAT, 150));

        // Farming

        REWARDS.put(EntityTypes.COW, new EntityXpReward(SkillType.FARMING, 10));
        REWARDS.put(EntityTypes.CHICKEN, new EntityXpReward(SkillType.FARMING, 10));
        REWARDS.put(EntityTypes.PIG, new EntityXpReward(SkillType.FARMING, 10));
        REWARDS.put(EntityTypes.SHEEP, new EntityXpReward(SkillType.FARMING, 10));

        // Fishing

        REWARDS.put(EntityTypes.COD, new EntityXpReward(SkillType.FISHING, 10));
        REWARDS.put(EntityTypes.SALMON, new EntityXpReward(SkillType.FISHING, 10));

    }
}
