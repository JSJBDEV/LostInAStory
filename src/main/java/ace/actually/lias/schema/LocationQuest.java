package ace.actually.lias.schema;

import ace.actually.lias.LIAS;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.Structure;

/**
 * A location quest, where the player must reach a structure
 */
public class LocationQuest {

    BlockPos blockPos;
    MutableText structureName;
    String additionalEvent;



    public LocationQuest(ServerPlayerEntity spe, TagKey<Structure> structureTagKey,String additionalEvent)
    {
        ServerWorld dim = spe.getServer().getWorld(LIAS.STORYTELLERS_DIMENSION);
        BlockPos pos = dim.locateStructure(structureTagKey,spe.getBlockPos(),1000,true);
        this.structureName=Text.of(structureTagKey.getName().getString().split(":")[1].replace("_"," ")).copy();
        this.blockPos=pos;
        this.additionalEvent=additionalEvent;
    }

    public MutableText getPrefixPhrase()
    {
        return Text.translatable("plotpoint.lias.wakeup");
    }

    public MutableText getText()
    {
        if(additionalEvent.equals("nil"))
        {
            return Text.empty().append(getPrefixPhrase()).append(structureName);
        }
        return Text.empty().append(getPrefixPhrase()).append(structureName).append(", ").append(Text.translatable(additionalEvent));
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public NbtCompound toNbt()
    {
        NbtCompound compound = new NbtCompound();
        compound.put("location", NbtHelper.fromBlockPos(blockPos));

        compound.putString("text",getText().getString());
        compound.putString("additionalEvent",additionalEvent);
        return compound;
    }
}
