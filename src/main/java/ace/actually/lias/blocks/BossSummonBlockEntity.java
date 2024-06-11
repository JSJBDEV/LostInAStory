package ace.actually.lias.blocks;

import ace.actually.lias.LIAS;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class BossSummonBlockEntity extends BlockEntity {

    NbtList uuids = new NbtList();
    public BossSummonBlockEntity(BlockPos pos, BlockState state) {
        super(LIAS.BOSS_SUMMON_BLOCK_ENTITY, pos, state);
    }

    public void setUuids(NbtList uuids) {
        this.uuids = uuids;
        markDirty();
    }

    public void addUuid(UUID uuid)
    {
        uuids.add(NbtHelper.fromUuid(uuid));
        markDirty();
    }

    public NbtList getUuids() {
        return uuids;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.put("uuids",uuids);
        super.writeNbt(nbt, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        uuids= (NbtList) nbt.get("uuids");
    }
}
