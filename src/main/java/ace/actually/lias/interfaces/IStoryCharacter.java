package ace.actually.lias.interfaces;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public interface IStoryCharacter {


    NbtCompound getStory();
    void setStory(NbtCompound compound);
}
