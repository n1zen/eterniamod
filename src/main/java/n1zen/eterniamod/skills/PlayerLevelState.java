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

public class PlayerLevelState extends SavedData {

    private Map<UUID, Map<SkillType, Integer>> playerSkillXp;

    public PlayerLevelState() {
        this.playerSkillXp = new HashMap<>();
    }

    public PlayerLevelState(Map<UUID, Map<SkillType, Integer>> playerSkillXp) {
        this.playerSkillXp = new HashMap<>();
        for (Map.Entry<UUID, Map<SkillType,  Integer>> entry : playerSkillXp.entrySet()) {
            this.playerSkillXp.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }

    }

    public Map<UUID, Map<SkillType, Integer>> getPlayerSkillXp() {
        return playerSkillXp;
    }

    public int getSkillExp(UUID playerId, SkillType skillType) {
        Map<SkillType, Integer> skillXp = this.playerSkillXp.getOrDefault(playerId, new HashMap<>());
        return skillXp.getOrDefault(skillType, 0);
    }

    public void addSkillExp(UUID playerId, SkillType skillType, int amount) {
        Map<SkillType, Integer> skillXp = this.playerSkillXp.computeIfAbsent(playerId, id -> new HashMap<>());
        skillXp.merge(skillType, amount, Integer::sum);
        setDirty();
    }

    public void removeSkillExp(UUID playerId, SkillType skillType) {
        Map<SkillType, Integer> skillXp = this.playerSkillXp.get(playerId);
        skillXp.put(skillType, 0);
        setDirty();
    }

    private static final Codec<SkillType> SKILL_TYPE_CODEC =
            Codec.STRING.xmap(SkillType::valueOf, Enum::name);

    private static final Codec<PlayerLevelState> CODEC = Codec.unboundedMap(
                    UUIDUtil.STRING_CODEC,
                    Codec.unboundedMap(SKILL_TYPE_CODEC, Codec.INT)
            )
            .fieldOf("playerSkillXp")
            .codec()
            .xmap(PlayerLevelState::new, PlayerLevelState::getPlayerSkillXp);

    public static final SavedDataType<PlayerLevelState> TYPE = new SavedDataType<PlayerLevelState>(
            Identifier.fromNamespaceAndPath(MOD_ID, "player_level_state"),
            PlayerLevelState::new,
            CODEC,
            null
    );

    public static PlayerLevelState get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }
}