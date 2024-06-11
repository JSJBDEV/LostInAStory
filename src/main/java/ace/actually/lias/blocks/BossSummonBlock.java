package ace.actually.lias.blocks;

import ace.actually.lias.LIAS;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BossSummonBlock extends Block implements BlockEntityProvider {
    public BossSummonBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BossSummonBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        BossSummonBlockEntity blockEntity = (BossSummonBlockEntity) world.getBlockEntity(pos);
        boolean onList = false;
        for (int i = 0; i < blockEntity.getUuids().size(); i++) {
            UUID uuid = NbtHelper.toUuid(blockEntity.getUuids().get(i));
            if(player.getUuid().equals(uuid))
            {
                onList=true;
                break;
            }
        }
        if(!onList)
        {
            blockEntity.addUuid(player.getUuid());
            SkeletonEntity skeletonEntity = new SkeletonEntity(EntityType.SKELETON,world);
            skeletonEntity.setPos(pos.getX(),pos.getY()+1,pos.getZ());
            skeletonEntity.equipStack(EquipmentSlot.CHEST, Items.DIAMOND_CHESTPLATE.getDefaultStack());
            skeletonEntity.equipStack(EquipmentSlot.HEAD, Items.DIAMOND_HELMET.getDefaultStack());
            skeletonEntity.equipStack(EquipmentSlot.MAINHAND,Items.BOW.getDefaultStack());
            skeletonEntity.equipStack(EquipmentSlot.OFFHAND, LIAS.THE_END_ITEM.getDefaultStack());
            skeletonEntity.setEquipmentDropChance(EquipmentSlot.OFFHAND,1);
            skeletonEntity.setCustomName(Text.of("Erwin, the prince of death"));
            world.spawnEntity(skeletonEntity);
        }
        return super.onUse(state, world, pos, player, hit);
    }
}
