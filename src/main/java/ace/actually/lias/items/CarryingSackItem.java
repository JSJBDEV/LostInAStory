package ace.actually.lias.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import java.util.List;

public class CarryingSackItem extends Item {
    public CarryingSackItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if(context.getHand()== Hand.MAIN_HAND && !context.getWorld().isClient)
        {
            if(context.getStack().get(DataComponentTypes.CUSTOM_DATA)==null || !context.getStack().get(DataComponentTypes.CUSTOM_DATA).contains("storage"))
            {
                NbtCompound compound = new NbtCompound();
                compound.put("storage", NbtHelper.fromBlockState(context.getWorld().getBlockState(context.getBlockPos())));
                context.getStack().set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(compound));
                context.getWorld().setBlockState(context.getBlockPos(), Blocks.AIR.getDefaultState());
            }
            else if(context.getPlayer().isSneaking())
            {
                NbtCompound compound = context.getStack().get(DataComponentTypes.CUSTOM_DATA).copyNbt();
                BlockState state = NbtHelper.toBlockState(Registries.BLOCK.getReadOnlyWrapper(),compound.getCompound("storage"));
                context.getWorld().setBlockState(context.getBlockPos().offset(context.getSide()),state);

                if(context.getPlayer().getRandom().nextInt(5)==0)
                {
                    context.getPlayer().sendMessage(Text.translatable("text.lias.sack_breaks"));
                    context.getStack().setCount(0);
                }
                else
                {
                    compound.remove("storage");
                    context.getStack().set(DataComponentTypes.CUSTOM_DATA,NbtComponent.of(compound));
                }

            }
        }

        return super.useOnBlock(context);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        if(stack.get(DataComponentTypes.CUSTOM_DATA)!=null && stack.get(DataComponentTypes.CUSTOM_DATA).contains("storage"))
        {
            BlockState state = NbtHelper.toBlockState(Registries.BLOCK.getReadOnlyWrapper(),stack.get(DataComponentTypes.CUSTOM_DATA).copyNbt().getCompound("storage"));
            if(!state.isAir())
            {
                tooltip.add(Text.translatable("text.lias.sack_carrying").append(" ").append(Text.translatable(state.getBlock().getTranslationKey())));
            }
            else
            {
                tooltip.add(Text.translatable("text.lias.sack_could"));
            }

        }
        else
        {
            tooltip.add(Text.translatable("text.lias.sack_could"));
        }
    }
}
