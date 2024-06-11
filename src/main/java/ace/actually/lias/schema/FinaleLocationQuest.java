package ace.actually.lias.schema;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.gen.structure.Structure;

public class FinaleLocationQuest extends LocationQuest{
    public FinaleLocationQuest(ServerPlayerEntity spe, TagKey<Structure> structureTagKey, String additionalEvent, String descriptor) {
        super(spe, structureTagKey, additionalEvent, descriptor);
    }

    @Override
    public MutableText getPrefixPhrase() {
        return Text.translatable("plotpoint.lias.finale");
    }

    @Override
    public NbtCompound toNbt() {
        NbtCompound compound = new NbtCompound();
        compound.put("location", NbtHelper.fromBlockPos(blockPos));
        compound.putString("questType","finale");
        compound.putString("text",getText().getString());
        compound.putString("additionalEvent",additionalEvent);
        compound.putString("descriptor",descriptor);
        return compound;
    }
}
