package ace.actually.lias.schema;

import ace.actually.lias.LIAS;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class BiomeLocationQuest{

    BlockPos blockPos;
    MutableText biomeName;
    String descriptor;
    String additionalEvent;
    public BiomeLocationQuest(ServerPlayerEntity spe, TagKey<Biome> biomeTagKey, String additionalEvent,String descriptor) {
        ServerWorld dim = spe.getServer().getWorld(LIAS.STORYTELLERS_DIMENSION);
        Pair<BlockPos, RegistryEntry<Biome>> pos = dim.locateBiome(a->a.isIn(biomeTagKey),spe.getBlockPos(),1000,50,100);
        String[] v = pos.getSecond().getIdAsString().split(":");
        this.biomeName = Text.translatable("biome."+v[0]+"."+v[1]);
        this.blockPos=pos.getFirst();
        this.additionalEvent=additionalEvent;
        this.descriptor=descriptor;
    }

    public MutableText getPrefixPhrase()
    {
        return Text.translatable("plotpoint.lias.wakeup");
    }

    public MutableText getText()
    {
        MutableText t = Text.empty().append(getPrefixPhrase()).append(" ").append(biomeName);

        if(!descriptor.equals("nil"))
        {
            if(descriptor.contains("descriptor.plotpoint.lias.trees"))
            {
                String[] v = descriptor.split("/");
                String[] state = v[1].split(":");
                t=t.append(", ").append(Text.translatable(v[0])).append(" ").append(Text.translatable("block."+state[0]+"."+state[1]));
            }

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

        compound.putString("text",getText().getString());
        compound.putString("additionalEvent",additionalEvent);
        compound.putString("descriptor",descriptor);
        return compound;
    }
}
