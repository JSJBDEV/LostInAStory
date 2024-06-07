package ace.actually.lias.mixin;

import ace.actually.lias.interfaces.IStoryCharacter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerStoryCharacterMixin extends LivingEntity implements IStoryCharacter {

    private static final TrackedData<NbtCompound> CHARACTER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);
    protected PlayerStoryCharacterMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void setStory(NbtCompound compound) {
        NbtCompound tag = dataTracker.get(CHARACTER);
        tag.put("story", compound);
        dataTracker.set(CHARACTER,tag);
    }

    @Override
    public NbtCompound getStory() {
        NbtCompound tag = dataTracker.get(CHARACTER);
        if(tag.contains("story"))
        {
            return tag.getCompound("story");
        }
        return null;
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        dataTracker.set(CHARACTER,nbt.getCompound("lias_character"));

    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.put("lias_character",dataTracker.get(CHARACTER));

    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    protected void initDataTracker(DataTracker.Builder builder,CallbackInfo ci) {
        NbtCompound a = new NbtCompound();

        builder.add(CHARACTER,a.copy());


    }
}
