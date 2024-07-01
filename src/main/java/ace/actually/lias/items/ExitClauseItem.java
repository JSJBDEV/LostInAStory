package ace.actually.lias.items;

import ace.actually.lias.LIAS;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.List;

public class ExitClauseItem extends Item {
    public ExitClauseItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(user instanceof ServerPlayerEntity spe && user.getStackInHand(hand).isOf(LIAS.EXIT_CLAUSE_ITEM))
        {
            if(spe.getWorld().getRegistryKey()== LIAS.STORYTELLERS_DIMENSION)
            {
                BlockPos v  = spe.getServer().getOverworld().getSpawnPos();
                spe.teleport(spe.getServer().getOverworld(),v.getX(),v.getY(),v.getZ(),spe.getYaw(),spe.getPitch());
                spe.changeGameMode(GameMode.SURVIVAL);
                user.getStackInHand(hand).decrement(1);
            }
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.translatable("text.lias.exit_clause"));
    }
}
