package ace.actually.lias.mixin;

import ace.actually.lias.LIAS;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin {


    @Shadow public abstract World getWorld();

    @ModifyVariable(at = @At(value = "HEAD"), method = "setBlockState", argsOnly = true)
    private BlockState placed(BlockState state) {
        if(getWorld().getRegistryKey()== LIAS.STORYTELLERS_DIMENSION)
        {
            if(state.isIn(BlockTags.AZALEA_GROWS_ON))
            {
                state  = Blocks.AMETHYST_BLOCK.getDefaultState();
            }

        }

        return state;
    }

}
