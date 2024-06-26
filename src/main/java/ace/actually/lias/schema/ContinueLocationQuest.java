package ace.actually.lias.schema;

import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.gen.structure.Structure;

public class ContinueLocationQuest extends LocationQuest{
    public ContinueLocationQuest(ServerPlayerEntity spe, TagKey<Structure> structureTagKey, String additionalEvent,String descriptor) {
        super(spe, structureTagKey, additionalEvent,descriptor);
    }

    @Override
    public MutableText getPrefixPhrase() {
        return Text.translatable("plotpoint.lias.setout");
    }
}
