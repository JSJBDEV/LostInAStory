package ace.actually.lias.items;

import ace.actually.lias.LIAS;
import ace.actually.lias.interfaces.IStoryCharacter;
import ace.actually.lias.schema.Quests;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.Structure;

import java.util.List;

public class StoryBookItem extends Item {
    public StoryBookItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(user instanceof ServerPlayerEntity spe)
        {
            if(spe.isSneaking() && spe.getMainHandStack().get(DataComponentTypes.CUSTOM_DATA)!=null)
            {
                IStoryCharacter character = (IStoryCharacter) user;
                character.setStory(spe.getMainHandStack().get(DataComponentTypes.CUSTOM_DATA).copyNbt());
                spe.dropSelectedItem(true);

                ServerWorld dim = spe.getServer().getWorld(LIAS.STORYTELLERS_DIMENSION);
                spe.changeGameMode(GameMode.ADVENTURE);

                NbtList quests = (NbtList) character.getStory().get("quests");
                NbtCompound quest1 = quests.getCompound(0);
                int[] il = quest1.getIntArray("location");
                BlockPos bottom = new BlockPos(il[0],il[1],il[2]);

                int y = dim.getChunk(bottom).sampleHeightmap(Heightmap.Type.WORLD_SURFACE_WG, bottom.getX() & 15, bottom.getZ() & 15);
                spe.teleport(dim, il[0],y+2,il[2],0,0);

                character.onLocationArrived();


            }
            else
            {
                spe.giveItemStack(makeRandomStoryBook(spe));
            }

        }
        return super.use(world, user, hand);
    }

    private static final TagKey<Structure>[] CONTINUE_STRUCTURES = new TagKey[]{StructureTags.OCEAN_RUIN,LIAS.DESERT_PYRAMIDS,LIAS.JUNGLE_PYRAMIDS};
    public static ItemStack makeRandomStoryBook(ServerPlayerEntity spe)
    {
        NbtCompound compound = new NbtCompound();
        NbtList orderedQuests = new NbtList();
        NbtCompound quest1 = Quests.randomStartQuestToNbt(spe);
        NbtCompound quest2 = Quests.randomContinueQuestToNbt(spe);
        NbtCompound quest3 = Quests.randomFinalQuestToNbt(spe);
        orderedQuests.add(quest1);
        orderedQuests.add(quest2);
        orderedQuests.add(quest3);

        compound.put("quests",orderedQuests);
        ItemStack stack = new ItemStack(LIAS.STORY_BOOK_ITEM);

        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(compound));

        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        if(stack.get(DataComponentTypes.CUSTOM_DATA)!=null)
        {
            NbtCompound compound = stack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
            NbtList q = (NbtList) compound.get("quests");
            for (int i = 0; i < q.size(); i++) {
                NbtCompound quest = q.getCompound(i);
                tooltip.add(Text.of(quest.getString("text")));
            }
        }
    }
}
