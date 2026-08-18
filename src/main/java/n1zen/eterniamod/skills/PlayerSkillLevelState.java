package n1zen.eterniamod.skills;

import com.mojang.serialization.Codec;
import n1zen.eterniamod.skills.level.effects.MiningEffects;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static n1zen.eterniamod.Eterniamod.MOD_ID;

public class PlayerSkillLevelState extends SavedData {

    private Map<UUID, Map<SkillType, Integer>> skillLevelStates;

    public static final int[] xpRequirements = {
            0, 50, 140, 270, 440, 650, 900, 1190, 1520, 1890,
            2300, 2750, 3240, 3770, 4340, 4950, 5600, 6290, 7020, 7790,
            8600, 9450, 10340, 11270, 12240, 13250, 14300, 15390, 16520, 17690,
            18900, 20150, 21440, 22770, 24140, 25550, 27000, 28490, 30020, 31590,
            33200, 34850, 36540, 38270, 40040, 41850, 43700, 45590, 47520, 49490,
            51500
    };

    public PlayerSkillLevelState() { this.skillLevelStates = new HashMap<>(); }

    public PlayerSkillLevelState(Map<UUID, Map<SkillType, Integer>> skillLevelStates) {
        this.skillLevelStates = new HashMap<>();
        for(Map.Entry<UUID, Map<SkillType, Integer>> entry : skillLevelStates.entrySet()) {
            this.skillLevelStates.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }
    }

    public Map<UUID, Map<SkillType, Integer>> getSkillLevelStates() { return this.skillLevelStates; }

    public int getSkillLevel(UUID playerUUID, SkillType skill) {
        Map<SkillType, Integer> skillLevel = this.skillLevelStates.getOrDefault(playerUUID, new HashMap<>());
        return skillLevel.getOrDefault(skill, 0);
    }

    public void removeSkillLevel(UUID playerUUID, SkillType skill) {
        Map<SkillType, Integer> skillLevel = this.skillLevelStates.computeIfAbsent(playerUUID, id -> new HashMap<>());
        skillLevel.put(skill, 0);
        setDirty();
    }

    public boolean levelledUp(double xp, UUID playerUUID, SkillType skill) {
        Map<SkillType, Integer> skillLevelMap = this.skillLevelStates.computeIfAbsent(playerUUID, id -> new HashMap<>());
        int prevLevel = skillLevelMap.getOrDefault(skill, 0);
        int actualLvl = getLvlForExp(xp);

        if (actualLvl > prevLevel) {
            skillLevelMap.put(skill, actualLvl);
            setDirty();
            return true;
        }
        return false;
    }

    public int getLvlForExp(double xp) {
        int level = 0;
        for (int i = 0; i < xpRequirements.length; i++) {
            if(xp >= xpRequirements[i]) {
                level = i;
            } else {
                break;
            }
        }
        return level;
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
