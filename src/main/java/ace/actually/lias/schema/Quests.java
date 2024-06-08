package ace.actually.lias.schema;

import ace.actually.lias.interfaces.IStoryCharacter;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.Structure;

import java.util.HashMap;
import java.util.Map;

public class Quests {


    public static Map<TagKey<Structure>,String[]> additionalEvents;
    static
    {
        additionalEvents=new HashMap<>();
        additionalEvents.put(StructureTags.VILLAGE,new String[]{"event.plotpoint.lias.skeletons_attack","event.plotpoint.lias.travelling_merchant"});
        additionalEvents.put(StructureTags.OCEAN_RUIN,new String[]{"event.plotpoint.lias.sea_monster"});
    }
    
    public static ContinueLocationQuest randomContinueQuest(ServerPlayerEntity spe, TagKey<Structure> structureTagKey)
    {
        String[] possibleEvents = additionalEvents.get(structureTagKey);
        return new ContinueLocationQuest(spe,structureTagKey,possibleEvents[spe.getRandom().nextInt(possibleEvents.length)]);
    }
    public static LocationQuest randomStartQuest(ServerPlayerEntity spe, TagKey<Structure> structureTagKey)
    {
        String[] possibleEvents = additionalEvents.get(structureTagKey);
        return new LocationQuest(spe,structureTagKey,possibleEvents[spe.getRandom().nextInt(possibleEvents.length)]);
    }

    public static String getNextQuestText(IStoryCharacter character)
    {
        NbtList quests = getQuestList(character);
        if(!quests.isEmpty())
        {
            NbtCompound quest = quests.getCompound(0);
            return quest.getString("text");
        }
        return "Nothing";
    }

    public static BlockPos getNextQuestLocation(IStoryCharacter character)
    {
        NbtList quests = getQuestList(character);
        if(!quests.isEmpty())
        {
            NbtCompound quest = quests.getCompound(0);
            int[] v = quest.getIntArray("location");
            return new BlockPos(v[0],v[1],v[2]);
        }
        return null;
    }

    public static String getNextQuestEvent(IStoryCharacter character)
    {
        NbtList quests = getQuestList(character);
        if(!quests.isEmpty())
        {
            NbtCompound quest = quests.getCompound(0);
            return quest.getString("additionalEvent");
        }
        return "Nothing";
    }

    public static NbtList getQuestList(IStoryCharacter character)
    {
        return (NbtList) character.getStory().get("quests");
    }
}
