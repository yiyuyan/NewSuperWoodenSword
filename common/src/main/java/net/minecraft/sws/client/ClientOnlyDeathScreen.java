package net.minecraft.sws.client;

import net.minecraft.sws.CommonClass;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.*;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.util.Iterator;
import java.util.List;

public class ClientOnlyDeathScreen extends Screen {
    private int delayTicker;
    private final Component causeOfDeath;
    private final boolean hardcore;
    private Component deathScore;
    private final List<Button> exitButtons = Lists.newArrayList();
    private Button exitToTitleButton;

    public ClientOnlyDeathScreen(Component causeOfDeath, boolean hardcore) {
        super(Component.translatable(hardcore ? "deathScreen.title.hardcore" : "deathScreen.title"));
        this.causeOfDeath = causeOfDeath;
        this.hardcore = hardcore;
        if(this.minecraft==null)this.minecraft= Minecraft.getInstance();
    }

    protected void init() {
        if(this.minecraft==null)this.minecraft= Minecraft.getInstance();
        this.delayTicker = 0;
        this.exitButtons.clear();
        Component component = this.hardcore ? Component.translatable("deathScreen.spectate") : Component.translatable("deathScreen.respawn");
        this.exitButtons.add((Button)this.addRenderableWidget(Button.builder(component, (p_280794_) -> {
            if (this.minecraft.player != null) {
                ((ILivingEntity) this.minecraft.player).playerUnZero();
                this.minecraft.player.respawn();
                p_280794_.active = false;
            }
        }).bounds(this.width / 2 - 100, this.height / 4 + 72, 200, 20).build()));
        this.exitToTitleButton = (Button)this.addRenderableWidget(Button.builder(Component.translatable("deathScreen.titleScreen"), (p_280796_) -> {
            this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, this::handleExitToTitleScreen, true);
        }).bounds(this.width / 2 - 100, this.height / 4 + 96, 200, 20).build());
        this.exitButtons.add(this.exitToTitleButton);
        this.setButtonsActive(false);
        this.deathScore = Component.translatable("deathScreen.score").append(": ").append(Component.literal(Integer.toString(this.minecraft.player.getScore())).withStyle(ChatFormatting.YELLOW));
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    private void handleExitToTitleScreen() {
        if (this.hardcore) {
            this.exitToTitleScreen();
        } else {
            if(this.minecraft==null)this.minecraft= Minecraft.getInstance();
            ConfirmScreen confirmscreen = new TitleConfirmScreenD((p_280795_) -> {
                if(this.minecraft==null)this.minecraft= Minecraft.getInstance();
                if (p_280795_) {
                    this.exitToTitleScreen();
                } else {
                    if (this.minecraft.player != null) {
                        this.minecraft.player.respawn();
                        this.minecraft.setScreen((Screen)null);
                    }
                }

            }, Component.translatable("deathScreen.quit.confirm"), CommonComponents.EMPTY, Component.translatable("deathScreen.titleScreen"), Component.translatable("deathScreen.respawn"));
            this.minecraft.setScreen(confirmscreen);
            confirmscreen.setDelay(20);
        }

    }

    private void exitToTitleScreen() {
        if(this.minecraft==null)this.minecraft= Minecraft.getInstance();
        if (this.minecraft.level != null) {
            this.minecraft.level.disconnect();
        }

        this.minecraft.clearLevel(new GenericDirtMessageScreen(Component.translatable("menu.savingLevel")));
        this.minecraft.setScreen(new TitleScreen());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public void afterKeyboardAction() {

    }

    @Override
    public void onClose() {

    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        try {
            if(this.minecraft==null)this.minecraft = Minecraft.getInstance();
            if(this.font==null)this.font = Minecraft.getInstance().font;
            if(this.minecraft.font==null)return;
            guiGraphics.fillGradient(0, 0, this.width, this.height, 1615855616, -1602211792);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(2.0F, 2.0F, 2.0F);
            guiGraphics.drawCenteredString(this.font, this.title, this.width / 2 / 2, 30, 16777215);
            guiGraphics.pose().popPose();
            if (this.causeOfDeath != null) {
                guiGraphics.drawCenteredString(this.font, this.causeOfDeath, this.width / 2, 85, 16777215);
            }

            guiGraphics.drawCenteredString(this.font, this.deathScore, this.width / 2, 100, 16777215);
            if (this.causeOfDeath != null && mouseY > 85 && mouseY < 94) {
                Style style = this.getClickedComponentStyleAt(mouseX);
                guiGraphics.renderComponentHoverEffect(this.font, style, mouseX, mouseY);
            }
            super.render(guiGraphics, mouseX, mouseY, partialTick);
            if (this.exitToTitleButton != null && this.minecraft.getReportingContext().hasDraftReport()) {
                guiGraphics.blit(AbstractWidget.WIDGETS_LOCATION, this.exitToTitleButton.getX() + this.exitToTitleButton.getWidth() - 17, this.exitToTitleButton.getY() + 3, 182, 24, 15, 15);
            }
        } catch (Throwable e) {
            Minecraft.getInstance().setScreen(new ClientOnlyDeathScreen(this.causeOfDeath,this.hardcore));
        }

    }

    private Style getClickedComponentStyleAt(int p_95918_) {
        if(this.minecraft==null)this.minecraft= Minecraft.getInstance();
        if (this.causeOfDeath == null) {
            return null;
        } else {
            int i = this.minecraft.font.width(this.causeOfDeath);
            int j = this.width / 2 - i / 2;
            int k = this.width / 2 + i / 2;
            return p_95918_ >= j && p_95918_ <= k ? this.minecraft.font.getSplitter().componentStyleAtWidth(this.causeOfDeath, p_95918_ - j) : null;
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.causeOfDeath != null && mouseY > 85.0 && mouseY < 94.0) {
            Style style = this.getClickedComponentStyleAt((int)mouseX);
            if (style != null && style.getClickEvent() != null && style.getClickEvent().getAction() == ClickEvent.Action.OPEN_URL) {
                this.handleComponentClicked(style);
                return false;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean isPauseScreen() {
        return false;
    }

    public void tick() {
        super.tick();
        ++this.delayTicker;
        if (this.delayTicker == 20) {
            this.setButtonsActive(true);
        }
        if(this.minecraft==null)this.minecraft= Minecraft.getInstance();
        if(this.minecraft.player!=null && ((ILivingEntity)this.minecraft.player).zero() && !CommonClass.has(this.minecraft.player)){
            CommonClass.attack(this.minecraft.player,false,true);
        }
    }

    private void setButtonsActive(boolean active) {
        Button button;
        for(Iterator var2 = this.exitButtons.iterator(); var2.hasNext(); button.active = active) {
            button = (Button)var2.next();
        }

    }

    public static class TitleConfirmScreenD extends ConfirmScreen {
        public TitleConfirmScreenD(BooleanConsumer p_273707_, Component p_273255_, Component p_273747_, Component p_273434_, Component p_273416_) {
            super(p_273707_, p_273255_, p_273747_, p_273434_, p_273416_);
        }
    }
}
