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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

import java.util.List;

public class DustyBookItem extends Item {
    public DustyBookItem(Settings settings) {
        super(settings);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(user instanceof ServerPlayerEntity spe)
        {
            spe.giveItemStack(makeRandomStoryBook(spe));
            if(spe.getStackInHand(hand).isOf(LIAS.DUSTY_BOOK_ITEM))
            {
                spe.getStackInHand(hand).decrement(1);
            }

        }
        return super.use(world, user, hand);
    }

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
        tooltip.add(Text.translatable("text.lias.dusty_book"));
    }
}
