package n1zen.eterniamod.skills.level.effects;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import static n1zen.eterniamod.Eterniamod.MOD_ID;

public class CombatEffects {
    private static final Identifier ATTACK_DAMAGE_MODIFIER_ID =
            Identifier.fromNamespaceAndPath(MOD_ID, "attack_damage_modifier");

    public static void applyAttackDamageModifier(ServerPlayer player, int combatLevel) {
        AttributeInstance playerAttribute = player.getAttribute(Attributes.ATTACK_DAMAGE);

        if(playerAttribute != null) {
            playerAttribute.removeModifier(ATTACK_DAMAGE_MODIFIER_ID);

            double attackDamageCap = 8;

            double modifier = Math.pow(combatLevel / 50.0, 2) * attackDamageCap;
            AttributeModifier attackDamageModifier = new AttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, modifier, AttributeModifier.Operation.ADD_VALUE);

            playerAttribute.addPermanentModifier(attackDamageModifier);
        }
    }
}
