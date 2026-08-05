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

public class PlayerSkillLevelState extends SavedData {

    private Map<UUID, Map<SkillType, Integer>> skillLevelStates;

    public PlayerSkillLevelState() { this.skillLevelStates = new HashMap<>(); }

    public PlayerSkillLevelState(Map<UUID, Map<SkillType, Integer>> skillLevelStates) {
        this.skillLevelStates = skillLevelStates;
        for(Map.Entry<UUID, Map<SkillType, Integer>> entry : skillLevelStates.entrySet()) {
            this.skillLevelStates.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }
    }

    public Map<UUID, Map<SkillType, Integer>> getSkillLevelStates() { return this.skillLevelStates; }

    public int getSkillLevel(UUID playerUUID, SkillType skill) {
        Map<SkillType, Integer> skillLevel = this.skillLevelStates.getOrDefault(playerUUID, new HashMap<>());
        return skillLevel.getOrDefault(skill, 0);
    }

    public void addSkillLevel(UUID playerUUID, SkillType skill, int level) {
        Map<SkillType, Integer> skillLevel = this.skillLevelStates.computeIfAbsent(playerUUID, id -> new HashMap<>());
        skillLevel.merge(skill, level, Integer::sum);
        setDirty();
    }

    public void removeSkillLevel(UUID playerUUID, SkillType skill) {
        Map<SkillType, Integer> skillLevel = this.skillLevelStates.get(playerUUID);
        skillLevel.put(skill, 0);
        setDirty();
    }

    private static final Codec<SkillType> SKILL_TYPE_CODEC =
            Codec.STRING.xmap(SkillType::valueOf, Enum::name);

    private static final Codec<PlayerSkillLevelState> CODEC = Codec.unboundedMap(
            UUIDUtil.STRING_CODEC,
            Codec.unboundedMap(SKILL_TYPE_CODEC, Codec.INT))
            .fieldOf("playerSkillLevel")
            .codec()
            .xmap(PlayerSkillLevelState::new, PlayerSkillLevelState::getSkillLevelStates);

    public static final SavedDataType<PlayerSkillLevelState> TYPE = new SavedDataType<PlayerSkillLevelState>(
            Identifier.fromNamespaceAndPath(MOD_ID, "player_skill_level_state"),
            PlayerSkillLevelState::new,
            CODEC,
            null
    );

    public static PlayerSkillLevelState get(ServerLevel level) { return level.getDataStorage().computeIfAbsent(TYPE); }
}
