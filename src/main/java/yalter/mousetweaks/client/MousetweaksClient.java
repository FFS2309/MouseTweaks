package yalter.mousetweaks.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;
import yalter.mousetweaks.Main;
import yalter.mousetweaks.MouseButton;


import java.util.logging.Logger;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class MousetweaksClient implements ClientModInitializer {
    Logger LOGGER = Logger.getLogger(MousetweaksClient.class.getName());
    @Override
    public void onInitializeClient() {
        Main.initialize();
        ScreenEvents.AFTER_INIT.register((client, screenG, scaledWidth, scaledHeight) -> {
            //notify Main about gui opening
            Main.onGuiOpen(screenG);
            //register events for open screen
            ScreenMouseEvents.allowMouseClick(screenG).register((screen, mouseX, mouseY, button) -> {
                MouseButton mouseButton = eventButtonToMouseButton(button);
                if(mouseButton != null){
                    return !Main.onMouseClicked(mouseX, mouseY, mouseButton);
                }
                return true;
            });

            ScreenMouseEvents.allowMouseRelease(screenG).register((screen, mouseX, mouseY, button) -> {
                MouseButton mouseButton = eventButtonToMouseButton(button);
                if(mouseButton != null)
                    return !Main.onMouseReleased(mouseX, mouseY, mouseButton);
                return true;
            });

            ScreenMouseEvents.allowMouseScroll(screenG).register((screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> !Main.onMouseScrolled(mouseX, mouseY, horizontalAmount));


        });
    }

    private static MouseButton eventButtonToMouseButton(int eventButton) {
        switch (eventButton) {
            case GLFW.GLFW_MOUSE_BUTTON_LEFT:
                return MouseButton.LEFT;
            case GLFW.GLFW_MOUSE_BUTTON_RIGHT:
                return MouseButton.RIGHT;
            default:
                return null;
        }
    }
}
