package ace.actually.lias.mixin;


import ace.actually.lias.LIAS;
import ace.actually.lias.interfaces.IStoryCharacter;
import ace.actually.lias.schema.Quests;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockTypes;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.LootableInventory;


import net.minecraft.item.BlockPredicatesChecker;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.GlobalPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Mixin(LootableInventory.class)
public interface LootMixin {

    @Inject(method = "generateLoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/loot/LootTable;supplyInventory(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/loot/context/LootContextParameterSet;J)V", shift = At.Shift.AFTER))
    private void loot(PlayerEntity player, CallbackInfo ci)
    {
        LootableContainerBlockEntity entity = (LootableContainerBlockEntity) this;
        if(player.getRandom().nextInt(5)==0 && player instanceof ServerPlayerEntity spe)
        {
            IStoryCharacter character = (IStoryCharacter) player;
            if(!Quests.getQuestList(character).isEmpty())
            {
                ItemStack stack = new ItemStack(Items.COMPASS);

                ServerWorld dim = spe.getServer().getWorld(LIAS.STORYTELLERS_DIMENSION);
                LodestoneTrackerComponent component = new LodestoneTrackerComponent(Optional.of(GlobalPos.create(dim.getRegistryKey(), Quests.getNextQuestLocation(character))),false);
                stack.set(DataComponentTypes.LODESTONE_TRACKER,component);
                stack.set(DataComponentTypes.ITEM_NAME, Text.translatable("item.lias.mysterious_compass"));
                entity.setStack(0,stack);
            }

        }
        if(player.getRandom().nextInt(5)==0)
        {
            ItemStack axe = new ItemStack(Items.IRON_AXE);
            BlockPredicate logs = BlockPredicate.Builder.create().tag(BlockTags.LOGS).build();
            BlockPredicate leaves = BlockPredicate.Builder.create().tag(BlockTags.LEAVES).build();
            BlockPredicatesChecker checker = new BlockPredicatesChecker(Arrays.asList(logs,leaves),false);
            LoreComponent component = new LoreComponent(Arrays.asList(Text.translatable("text.lias.breaks_wood")));

            axe.set(DataComponentTypes.CAN_BREAK,checker);
            axe.set(DataComponentTypes.LORE,component);
            axe.set(DataComponentTypes.ITEM_NAME,Text.translatable("item.lias.woodcutters_axe"));
            entity.setStack(1,axe);
        }
        if(player.getRandom().nextInt(5)==0)
        {
            ItemStack axe = new ItemStack(Items.IRON_HOE);
            BlockPredicate logs = BlockPredicate.Builder.create().tag(BlockTags.CROPS).build();
            BlockPredicate leaves = BlockPredicate.Builder.create().blocks(Blocks.HAY_BLOCK).build();
            BlockPredicatesChecker checker = new BlockPredicatesChecker(Arrays.asList(logs,leaves),false);
            LoreComponent component = new LoreComponent(Arrays.asList(Text.translatable("text.lias.breaks_crops")));

            axe.set(DataComponentTypes.CAN_BREAK,checker);
            axe.set(DataComponentTypes.LORE,component);
            axe.set(DataComponentTypes.ITEM_NAME,Text.translatable("item.lias.farmers_scythe"));
            entity.setStack(2,axe);
        }
        if(player.getRandom().nextInt(1)==0)
        {
            ItemStack sack = new ItemStack(LIAS.CARRYING_SACK_ITEM);

            BlockPredicate pick = BlockPredicate.Builder.create().tag(BlockTags.PICKAXE_MINEABLE).build();
            BlockPredicatesChecker checker = new BlockPredicatesChecker(Arrays.asList(pick),false);
            sack.set(DataComponentTypes.CAN_PLACE_ON,checker);
            entity.setStack(3,sack);
        }


    }
}