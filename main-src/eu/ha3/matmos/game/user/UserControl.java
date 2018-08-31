package eu.ha3.matmos.game.user;

import eu.ha3.easy.TimeStatistic;
import eu.ha3.matmos.game.gui.MAtGuiMenu;
import eu.ha3.matmos.game.mod.MAtMod;
import eu.ha3.matmos.game.system.MAtmosUtility;
import eu.ha3.mc.convenience.Ha3HoldActions;
import eu.ha3.mc.convenience.Ha3KeyHolding;
import eu.ha3.mc.convenience.Ha3KeyManager_2;
import eu.ha3.mc.haddon.supporting.SupportsFrameEvents;
import eu.ha3.mc.haddon.supporting.SupportsTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

/* x-placeholder */

public class UserControl implements Ha3HoldActions, SupportsTickEvents, SupportsFrameEvents {
    private final MAtMod mod;
    private final Ha3KeyManager_2 keyManager = new Ha3KeyManager_2();

    private KeyBinding keyBindingMain;
    private VolumeScroller scroller;
    private int tickRound;

    private int loadingCount;

    public UserControl(MAtMod mod) {
        this.mod = mod;
    }

    public void load() {
        // new KeyBinding registers it rightaway to the list of keys
        this.keyBindingMain = new KeyBinding("MAtmos", 65, "key.categories.misc");
        Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils.addAll(Minecraft.getMinecraft().gameSettings.keyBindings, this.keyBindingMain);
        this.keyBindingMain.setKeyCode(this.mod.getConfig().getInteger("key.code"));
        KeyBinding.resetKeyBindingArrayAndHash();

        this.keyManager.addKeyBinding(this.keyBindingMain, new Ha3KeyHolding(this, 7));

        this.scroller = new VolumeScroller(this.mod);
    }

    private String getKeyBindingMainFriendlyName() {
        if (this.keyBindingMain == null) return "undefined";

        return Keyboard.getKeyName(this.keyBindingMain.getKeyCode()); // OBF getKeyCode(), or .keyCode
    }

    @Override
    public void onTick() {
        this.keyManager.onTick();

        // Copied from Sollace's Presence Footsteps solution to bindings disappearing
        if (tickRound == 0) {
            int keyCode = keyBindingMain.getKeyCode();
            if (keyCode != mod.getConfig().getInteger("key.code")) {
                //PFLog.log("Key binding changed. Saving...");
                mod.getConfig().setProperty("key.code", keyCode);
                mod.saveConfig();
            }
        }
        tickRound = (tickRound + 1) % 100;

        this.scroller.routine();
        if (this.scroller.isRunning()) {
            this.mod.getGlobalVolumeControl().setVolumeAndUpdate(this.scroller.getValue());
        }
    }

    @Override
    public void onFrame(float fspan) {
        this.scroller.draw(fspan);
    }

    private void printUnusualMessages() {
        if (!this.mod.isInitialized()) {
            this.mod.getChatter().printChat(TextFormatting.RED, "Unknown error: MAtmos isn't initialized");
        } else {
            if (!MAtmosUtility.isSoundMasterEnabled()) {
                this.mod.getChatter().printChat(
                        TextFormatting.RED, "Warning: ", TextFormatting.WHITE,
                        "Sounds are turned off in your game settings!");
            }
            if (!MAtmosUtility.isSoundAmbientEnabled()) {
                this.mod.getChatter().printChat(
                        TextFormatting.RED, "Warning: ", TextFormatting.WHITE,
                        "Ambient sounds are at 0% volume in the advanced MAtmos options menu!");
            }
        }
    }

    @Override
    public void beginHold() {
        /*if (this.mod.getConfig().getBoolean("reversed.controls") && false)
        {
        	//TODO: Disabled for now
        	displayMenu();
        }
        else*/ if (this.mod.isActivated()) {
            this.scroller.start();
        }
    }

    @Override
    public void shortPress() {
        /*if (this.mod.getConfig().getBoolean("reversed.controls") && false)
        {
        	//TODO: Disabled for now
        	whenWantsToggle();
        }
        else
        {*/
        if (!this.mod.isActivated()) {
            whenWantsToggle();
        } else {
            displayMenu();
        }
        //}

        printUnusualMessages();
    }

    @Override
    public void endHold() {
        if (this.scroller.isRunning()) {
            this.scroller.stop();
            this.mod.getConfig().setProperty("globalvolume.scale", this.mod.getGlobalVolumeControl().getVolume());
            this.mod.saveConfig();
        }

        whenWantsForcing();
        printUnusualMessages();

    }

    private void whenWantsToggle() {
        if (this.mod.isActivated()) {
            this.mod.deactivate();
            this.mod.getChatter().printChat(
                    TextFormatting.YELLOW, "Stopped. Press ", TextFormatting.WHITE,
                    getKeyBindingMainFriendlyName(), TextFormatting.YELLOW, " to re-enable.");

        } else if (this.mod.isInitialized()) {
            if (this.loadingCount != 0) {
                this.mod.getChatter().printChat(TextFormatting.GREEN, "Loading...");
            } else {
                this.mod.getChatter().printChat(
                        TextFormatting.GREEN, "Loading...", TextFormatting.YELLOW, " (Hold ",
                        TextFormatting.WHITE, getKeyBindingMainFriendlyName() + " down",
                        TextFormatting.YELLOW, " to tweak the volume)");
            }

            this.loadingCount++;
            this.mod.activate();

        } else if (!this.mod.isInitialized()) {
            whenUninitializedAction();
        }

    }

    private void whenUninitializedAction() {
        if (this.mod.isInitialized()) return;

        TimeStatistic stat = new TimeStatistic();
        this.mod.start();
        this.mod.getChatter().printChat(
                TextFormatting.GREEN, "Loading for the first time (" + stat.getSecondsAsString(2) + "s)");
    }

    private void whenWantsForcing() {
        if (!this.mod.isActivated() && this.mod.isInitialized()) {
            TimeStatistic stat = new TimeStatistic();
            this.mod.refresh();
            this.mod.activate();
            this.mod.getChatter().printChat(
                    TextFormatting.GREEN, "Reloading expansions (" + stat.getSecondsAsString(2) + "s)");
        } else if (!this.mod.isInitialized()) {
            whenUninitializedAction();
        }
    }

    private void displayMenu() {
        if (this.mod.isActivated() && this.mod.util().isCurrentScreen(null)) {
            // OBF displayGuiScreen
            Minecraft.getMinecraft().displayGuiScreen(
                    new MAtGuiMenu((GuiScreen)this.mod.util().getCurrentScreen(), this.mod));
        }
    }

    @Override
    public void beginPress() {
    }

    @Override
    public void endPress() {
    }

}
