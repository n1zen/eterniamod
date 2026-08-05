package n1zen.eterniamod.skills;

import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static n1zen.eterniamod.Eterniamod.MOD_ID;

public class PlayerSkillXpState extends SavedData {

    private Map<UUID, Map<SkillType, Double>> playerSkillXp;

    public PlayerSkillXpState() {
        this.playerSkillXp = new HashMap<>();
    }

    public PlayerSkillXpState(Map<UUID, Map<SkillType, Double>> playerSkillXp) {
        this.playerSkillXp = new HashMap<>();
        for (Map.Entry<UUID, Map<SkillType,  Double>> entry : playerSkillXp.entrySet()) {
            this.playerSkillXp.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }

    }

    public Map<UUID, Map<SkillType, Double>> getPlayerSkillXp() {
        return playerSkillXp;
    }

    public Double getSkillExp(UUID playerId, SkillType skillType) {
        Map<SkillType, Double> skillXp = this.playerSkillXp.getOrDefault(playerId, new HashMap<>());
        return skillXp.getOrDefault(skillType, (double) 0);
    }

    public void addSkillExp(UUID playerId, SkillType skillType, double amount) {
        Map<SkillType, Double> skillXp = this.playerSkillXp.computeIfAbsent(playerId, id -> new HashMap<>());
        skillXp.merge(skillType, amount, Double::sum);
        setDirty();
    }

    public void removeSkillExp(UUID playerId, SkillType skillType) {
        Map<SkillType, Double> skillXp = this.playerSkillXp.get(playerId);
        skillXp.put(skillType, (double) 0);
        setDirty();
    }

    private static final Codec<SkillType> SKILL_TYPE_CODEC =
            Codec.STRING.xmap(SkillType::valueOf, Enum::name);

    private static final Codec<PlayerSkillXpState> CODEC = Codec.unboundedMap(
                    UUIDUtil.STRING_CODEC,
                    Codec.unboundedMap(SKILL_TYPE_CODEC, Codec.DOUBLE)
            )
            .fieldOf("playerSkillXp")
            .codec()
            .xmap(PlayerSkillXpState::new, PlayerSkillXpState::getPlayerSkillXp);

    public static final SavedDataType<PlayerSkillXpState> TYPE = new SavedDataType<PlayerSkillXpState>(
            Identifier.fromNamespaceAndPath(MOD_ID, "player_skill_xp_state"),
            PlayerSkillXpState::new,
            CODEC,
            null
    );

    public static PlayerSkillXpState get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }
}