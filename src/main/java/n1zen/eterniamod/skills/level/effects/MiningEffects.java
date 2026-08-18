package n1zen.eterniamod.skills.level.effects;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;


import static n1zen.eterniamod.Eterniamod.MOD_ID;

public class MiningEffects {

    private static final Identifier MINING_SPEED_MODIFIER_ID =
            Identifier.fromNamespaceAndPath(MOD_ID, "mining_skill_speed_bonus");

    public static void applyMiningSpeedBonus(ServerPlayer player, int miningLevel) {
        AttributeInstance playerAttribute = player.getAttribute(Attributes.MINING_EFFICIENCY);

        if(playerAttribute != null) {
            playerAttribute.removeModifier(MINING_SPEED_MODIFIER_ID);

            double miningSpeedCap = 79;

            double modifier = Math.pow(miningLevel / 50.0, 2) * miningSpeedCap;
            AttributeModifier miningSpeedModifier = new AttributeModifier(MINING_SPEED_MODIFIER_ID, modifier, AttributeModifier.Operation.ADD_VALUE);

            playerAttribute.addPermanentModifier(miningSpeedModifier);
        }
    }

}
