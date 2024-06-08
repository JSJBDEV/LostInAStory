package ace.actually.lias.mixin;


import ace.actually.lias.LIAS;
import ace.actually.lias.interfaces.IStoryCharacter;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.LootableInventory;
import net.minecraft.item.CompassItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
            if(!LIAS.getQuestList(character).isEmpty())
            {
                ItemStack stack = new ItemStack(Items.COMPASS);

                ServerWorld dim = spe.getServer().getWorld(LIAS.STORYTELLERS_DIMENSION);
                LodestoneTrackerComponent component = new LodestoneTrackerComponent(Optional.of(GlobalPos.create(dim.getRegistryKey(),LIAS.getNextQuestLocation(character))),false);
                stack.set(DataComponentTypes.LODESTONE_TRACKER,component);
                stack.set(DataComponentTypes.ITEM_NAME, Text.translatable("item.lias.mysterious_compass"));
                entity.setStack(0,stack);
            }

        }

    }
}