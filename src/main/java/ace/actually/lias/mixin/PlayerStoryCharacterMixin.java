package ace.actually.lias.mixin;

import ace.actually.lias.interfaces.IStoryCharacter;
import ace.actually.lias.schema.Quests;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerStoryCharacterMixin extends LivingEntity implements IStoryCharacter {

    @Shadow public abstract void sendMessage(Text message, boolean overlay);

    @Shadow public double capeX;
    @Shadow public double capeY;

    @Shadow @NotNull public abstract ItemStack getWeaponStack();

    @Shadow public int experienceLevel;
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

    @Inject(method = "tickMovement", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if(getStory()!=null && Quests.getNextQuestLocation(this)!=null)
        {
            if(getBlockPos().getSquaredDistance(Quests.getNextQuestLocation(this).up(getBlockY()))<20)
            {
                onLocationArrived();
            }
        }

    }

    @Override
    public void onLocationArrived()
    {
        sendMessage(Text.translatable("text.lias.location_found"));
        String add = Quests.getNextQuestEvent(this);
        Quests.getQuestList(this).remove(0);
        switch (add)
        {
            case "event.plotpoint.lias.skeletons_attack"->
            {
                SkeletonEntity entity = new SkeletonEntity(EntityType.SKELETON,getWorld());
                entity.setPos(getX()+10,getY(),getZ()+10);
                //entity.setCustomName(Text.of("Craig"));
                getWorld().spawnEntity(entity);

                SkeletonEntity entity2 = new SkeletonEntity(EntityType.SKELETON,getWorld());
                entity2.setPos(getX()-10,getY(),getZ()-10);
                //entity.setCustomName(Text.of("Craig"));
                getWorld().spawnEntity(entity2);
            }
            case "event.plotpoint.lias.sea_monster" ->
            {
                GuardianEntity entity = new GuardianEntity(EntityType.GUARDIAN,getWorld());
                entity.setPos(getX(),getY(),getZ());
                entity.setCustomName(Text.of("Craig"));
                getWorld().spawnEntity(entity);
            }
            case "event.plotpoint.lias.travelling_merchant" ->
            {
                WanderingTraderEntity entity = new WanderingTraderEntity(EntityType.WANDERING_TRADER,getWorld());
                entity.setPos(getX(),getY(),getZ());
                //entity.setCustomName(Text.of("Craig"));
                getWorld().spawnEntity(entity);
            }
        }
    }
}
