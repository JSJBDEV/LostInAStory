package ace.actually.lias.items;

import ace.actually.lias.LIAS;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

public class TheEndItem extends Item {
    public TheEndItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(user instanceof ServerPlayerEntity spe)
        {
            ServerWorld dim = spe.getServer().getWorld(LIAS.STORYTELLERS_DIMENSION);
            if(spe.getServerWorld()==dim)
            {
                spe.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,1000,255));
                spe.teleport(spe.getServer().getOverworld(),spe.getX(),spe.getY()+100,spe.getZ(),0,0);
                user.setStackInHand(hand,ItemStack.EMPTY);
            }
        }
        return super.use(world, user, hand);
    }
}
