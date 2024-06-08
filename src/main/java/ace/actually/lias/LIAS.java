package ace.actually.lias;

import ace.actually.lias.blocks.WornParchmentBlock;
import ace.actually.lias.interfaces.IStoryCharacter;
import ace.actually.lias.items.StoryBookItem;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.impl.networking.CustomPayloadTypeProvider;
import net.fabricmc.fabric.impl.networking.server.ServerNetworkingImpl;
import net.fabricmc.fabric.impl.registry.sync.packet.DirectRegistryPacketHandler;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LIAS implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("lias");
	public static final RegistryKey<World> STORYTELLERS_DIMENSION = RegistryKey.of(RegistryKeys.WORLD,Identifier.of("lias","storyteller"));

	public static final Identifier CONSIDER_PACKET = Identifier.of("lias","consider_packet");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		registerItems();
		registerBlocks();
		PayloadTypeRegistry.playC2S().register(KeyPayload.ID,KeyPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(KeyPayload.ID,((payload, context) ->
		{
			context.player().server.execute(()->
			{
				IStoryCharacter character = (IStoryCharacter) context.player();
				context.player().sendMessage(Text.translatable("text.lias.consider1").append("\"").append(getNextQuestText(character)).append("\"").append(Text.translatable("text.lias.consider2")));
			});
		}));
		LOGGER.info("Hello Fabric world!");
	}

	public static final StoryBookItem STORY_BOOK_ITEM = new StoryBookItem(new Item.Settings());
	private void registerItems()
	{
		Registry.register(Registries.ITEM,Identifier.of("lias","storybook"),STORY_BOOK_ITEM);
	}

	public static final WornParchmentBlock WORN_PARCHMENT_BLOCK = new WornParchmentBlock(AbstractBlock.Settings.copy(Blocks.CYAN_WOOL));
	private void registerBlocks()
	{
		Registry.register(Registries.BLOCK, Identifier.of("lias","worn_parchment_block"),WORN_PARCHMENT_BLOCK);
	}


	public record KeyPayload() implements CustomPayload
	{
		public static final PacketCodec<PacketByteBuf, KeyPayload> CODEC = PacketCodec.unit(new KeyPayload());
		public static final Id<KeyPayload> ID = new Id<>(Identifier.of("lias","key_payload"));
		@Override
		public Id<? extends CustomPayload> getId() {
			return ID;
		}
	}

	public static String getNextQuestText(IStoryCharacter character)
	{
		NbtList quests = getQuestList(character);
		if(!quests.isEmpty())
		{
			NbtCompound quest = quests.getCompound(0);
			return quest.getString("text");
		}
		return "Nothing";
	}
	public static BlockPos getNextQuestLocation(IStoryCharacter character)
	{
		NbtList quests = getQuestList(character);
		if(!quests.isEmpty())
		{
			NbtCompound quest = quests.getCompound(0);
			int[] v = quest.getIntArray("location");
			return new BlockPos(v[0],v[1],v[2]);
		}
		return null;
	}

	public static String getNextQuestEvent(IStoryCharacter character)
	{
		NbtList quests = getQuestList(character);
		if(!quests.isEmpty())
		{
			NbtCompound quest = quests.getCompound(0);
			return quest.getString("additionalEvent");
		}
		return "Nothing";
	}
	public static NbtList getQuestList(IStoryCharacter character)
	{
		return (NbtList) character.getStory().get("quests");
	}
}