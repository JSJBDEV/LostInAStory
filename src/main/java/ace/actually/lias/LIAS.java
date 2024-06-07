package ace.actually.lias;

import ace.actually.lias.blocks.WornParchmentBlock;
import ace.actually.lias.items.StoryBookItem;
import net.fabricmc.api.ModInitializer;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LIAS implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("lias");
	public static final RegistryKey<World> STORYTELLERS_DIMENSION = RegistryKey.of(RegistryKeys.WORLD,Identifier.of("lias","storyteller"));


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		registerItems();
		registerBlocks();
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
}