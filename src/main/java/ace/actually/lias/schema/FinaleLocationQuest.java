package ace.actually.lias.schema;

import ace.actually.lias.LIAS;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.structure.Structure;

public class FinaleLocationQuest{
    BlockPos blockPos;
    MutableText structureName;
    String additionalEvent;

    String descriptor;

    public FinaleLocationQuest(ServerPlayerEntity spe, String endstructure,String additionalEvent,String descriptor)
    {
        ServerWorld dim = spe.getServer().getWorld(LIAS.STORYTELLERS_DIMENSION);
        BlockPos pos = new BlockPos(spe.getRandom().nextBetween(-500,500),0,spe.getRandom().nextBetween(-500,500));
        int y = dim.getChunk(pos).sampleHeightmap(Heightmap.Type.WORLD_SURFACE_WG, pos.getX() & 15, pos.getZ() & 15);
        pos=pos.up(y);
        Quests.spawnStructure(dim,pos, Identifier.of("lias","bamboo_arena"));
        this.structureName=Text.of(endstructure).copy();
        this.blockPos=pos;
        this.additionalEvent=additionalEvent;
        this.descriptor=descriptor;
        System.out.println(pos.toShortString());
    }

    public MutableText getPrefixPhrase()
    {
        return Text.translatable("plotpoint.lias.finale");
    }

    public MutableText getText()
    {
        MutableText t = Text.empty().append(getPrefixPhrase()).append(" ").append(structureName);

        if(!descriptor.equals("nil"))
        {
            t=t.append(", ").append(Text.translatable(descriptor));
        }
        if(!additionalEvent.equals("nil"))
        {
            t=t.append(", ").append(Text.translatable(additionalEvent));
        }
        return t;
    }

    public NbtCompound toNbt()
    {
        NbtCompound compound = new NbtCompound();
        compound.put("location", NbtHelper.fromBlockPos(blockPos));
        compound.putString("questType","finale");
        compound.putString("text",getText().getString());
        compound.putString("additionalEvent",additionalEvent);
        compound.putString("descriptor",descriptor);
        return compound;
    }
}
