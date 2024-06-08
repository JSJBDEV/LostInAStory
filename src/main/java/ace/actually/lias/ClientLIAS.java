package ace.actually.lias;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ClientLIAS implements ClientModInitializer {

    private KeyBinding consider;
    @Override
    public void onInitializeClient() {
        consider = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.lias.consider",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_INSERT,
                "category.lias.main" // The translation key of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (consider.wasPressed()) {
                ClientPlayNetworking.send(new LIAS.KeyPayload());
            }
        });
    }
}
