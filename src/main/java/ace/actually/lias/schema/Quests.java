package ace.actually.lias.schema;

import ace.actually.lias.interfaces.IStoryCharacter;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.structure.Structure;

import java.util.HashMap;
import java.util.Map;

public class Quests {

    private static final String[] NIL = new String[]{"nil"};

    public static final Map<TagKey<Structure>,String[]> additionalStructureEvents;
    public static final  Map<TagKey<Biome>,String[]> additionalBiomeEvents;

    public static final Map<TagKey<Structure>,String[]> structureDescriptors;
    public static final  Map<TagKey<Biome>,String[]> biomeDescriptor;




    static
    {
        additionalStructureEvents =new HashMap<>();
        additionalStructureEvents.put(StructureTags.VILLAGE,new String[]{"event.plotpoint.lias.skeletons_attack","event.plotpoint.lias.travelling_merchant"});
        additionalStructureEvents.put(StructureTags.OCEAN_RUIN,new String[]{"event.plotpoint.lias.sea_monster"});

        structureDescriptors=new HashMap<>();


        additionalBiomeEvents = new HashMap<>();
        additionalBiomeEvents.put(BiomeTags.IS_FOREST,new String[]{"event.plotpoint.lias.skeletons_attack","event.plotpoint.lias.travelling_merchant"});
        additionalBiomeEvents.put(BiomeTags.IGLOO_HAS_STRUCTURE,new String[]{"event.plotpoint.lias.skeletons_attack","event.plotpoint.lias.travelling_merchant"});

        biomeDescriptor=new HashMap<>();
        biomeDescriptor.put(BiomeTags.IS_FOREST,new String[]{"descriptor.plotpoint.lias.trees"});
    }
    public static final TagKey<Structure>[] STRUCTURE_TAGS = new TagKey[]{StructureTags.VILLAGE,StructureTags.SHIPWRECK};
    public static final TagKey<Biome>[] BIOME_TAGS = new TagKey[]{BiomeTags.IS_FOREST};
    
    private static ContinueLocationQuest continueStructureQuest(ServerPlayerEntity spe, TagKey<Structure> structureTagKey)
    {
        String[] possibleEvents = additionalStructureEvents.getOrDefault(structureTagKey,NIL);
        String[] possibleDescriptors = structureDescriptors.getOrDefault(structureTagKey,NIL);
        return new ContinueLocationQuest(spe,structureTagKey,possibleEvents[spe.getRandom().nextInt(possibleEvents.length)],possibleDescriptors[spe.getRandom().nextInt(possibleDescriptors.length)]);
    }

    private static FinaleLocationQuest finaleStructureQuest(ServerPlayerEntity spe, TagKey<Structure> structureTagKey)
    {
        String[] possibleEvents = additionalStructureEvents.getOrDefault(structureTagKey,NIL);
        String[] possibleDescriptors = structureDescriptors.getOrDefault(structureTagKey,NIL);
        return new FinaleLocationQuest(spe,"Bamboo Arena",possibleEvents[spe.getRandom().nextInt(possibleEvents.length)],possibleDescriptors[spe.getRandom().nextInt(possibleDescriptors.length)]);
    }
    public static NbtCompound randomFinalQuestToNbt(ServerPlayerEntity spe)
    {
        return finaleStructureQuest(spe,STRUCTURE_TAGS[spe.getRandom().nextInt(STRUCTURE_TAGS.length)]).toNbt();
    }

    public static NbtCompound randomContinueQuestToNbt(ServerPlayerEntity spe)
    {
        return continueStructureQuest(spe,STRUCTURE_TAGS[spe.getRandom().nextInt(STRUCTURE_TAGS.length)]).toNbt();
    }

    public static NbtCompound randomStartQuestToNbt(ServerPlayerEntity spe)
    {
        if(spe.getRandom().nextBoolean())
        {
            return biomeStartQuest(spe,BIOME_TAGS[spe.getRandom().nextInt(BIOME_TAGS.length)]).toNbt();
        }
        else
        {
            return structureStartQuest(spe,STRUCTURE_TAGS[spe.getRandom().nextInt(STRUCTURE_TAGS.length)]).toNbt();
        }
    }
    private static LocationQuest structureStartQuest(ServerPlayerEntity spe, TagKey<Structure> structureTagKey)
    {
        String[] possibleEvents = additionalStructureEvents.getOrDefault(structureTagKey,NIL);
        String[] possibleDescriptors = structureDescriptors.getOrDefault(structureTagKey,NIL);
        return new LocationQuest(spe,structureTagKey,possibleEvents[spe.getRandom().nextInt(possibleEvents.length)],possibleDescriptors[spe.getRandom().nextInt(possibleDescriptors.length)]);
    }

    private static BiomeLocationQuest biomeStartQuest(ServerPlayerEntity spe, TagKey<Biome> biomeTagKey)
    {
        String[] possibleEvents = additionalBiomeEvents.getOrDefault(biomeTagKey,NIL);
        String[] possibleDescriptors = biomeDescriptor.getOrDefault(biomeTagKey,NIL);
        return new BiomeLocationQuest(spe,biomeTagKey,possibleEvents[spe.getRandom().nextInt(possibleEvents.length)],possibleDescriptors[spe.getRandom().nextInt(possibleDescriptors.length)]+"/minecraft:gold_block");
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
    public static String getNextQuestDescriptor(IStoryCharacter character)
    {
        NbtList quests = getQuestList(character);
        if(!quests.isEmpty())
        {
            NbtCompound quest = quests.getCompound(0);
            return quest.getString("descriptor");
        }
        return "Nothing";
    }
    public static String getNextQuestType(IStoryCharacter character)
    {
        NbtList quests = getQuestList(character);
        if(!quests.isEmpty())
        {
            NbtCompound quest = quests.getCompound(0);
            return quest.getString("questType");
        }
        return "nil";
    }

    public static NbtList getQuestList(IStoryCharacter character)
    {
        return (NbtList) character.getStory().get("quests");
    }

    public static void spawnStructure(ServerWorld world, BlockPos pos, Identifier name)
    {
        StructureTemplateManager manager = world.getStructureTemplateManager();

        StructureTemplate structureTemplate = manager.getTemplate(name).get();

        StructurePlacementData data = new StructurePlacementData().setIgnoreEntities(false);
        structureTemplate.place(world,pos,pos,data,world.getRandom(),4);

    }


}
