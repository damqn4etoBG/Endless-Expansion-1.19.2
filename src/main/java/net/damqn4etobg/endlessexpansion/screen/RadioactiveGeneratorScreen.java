package net.damqn4etobg.endlessexpansion.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.damqn4etobg.endlessexpansion.EndlessExpansion;
import net.damqn4etobg.endlessexpansion.screen.renderer.EnergyInfoArea;
import net.damqn4etobg.endlessexpansion.screen.renderer.FluidTankRenderer;
import net.damqn4etobg.endlessexpansion.screen.renderer.TemperatureInfoArea;
import net.damqn4etobg.endlessexpansion.util.MouseUtil;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;

import java.util.Optional;

public class RadioactiveGeneratorScreen extends AbstractContainerScreen<RadioactiveGeneratorMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(EndlessExpansion.MOD_ID,"textures/gui/radioactive_generator_gui.png");
    private EnergyInfoArea energyInfoArea;
    private FluidTankRenderer renderer;
    private FluidTankRenderer wasteRenderer;
    private TemperatureInfoArea temperatureInfoArea;

    public RadioactiveGeneratorScreen(RadioactiveGeneratorMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        assignEnergyInfoArea();
        assignFluidRenderer();
        assignFluidWasteRenderer();
        assignTemperatureInfoArea();
    }

    private void assignFluidRenderer() {
        renderer = new FluidTankRenderer(128000, true, 16, 54);
    }

    private void assignFluidWasteRenderer() {
        wasteRenderer = new FluidTankRenderer(128000, true, 4, 54);
    }

    private void assignEnergyInfoArea() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        energyInfoArea = new EnergyInfoArea(x + 154, y + 16, menu.blockEntity.getEnergyStorage());
    }

    private void assignTemperatureInfoArea() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        temperatureInfoArea = new TemperatureInfoArea(x + 17, y + 16, menu.blockEntity.getTemperature());
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderEnergyAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);
        renderFluidAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);
        renderFluidWasteAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);
        renderTemperatureAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);
    }

    private void renderFluidAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 135, 16, 16, 54)) {
            renderTooltip(pPoseStack, renderer.getTooltip(menu.getFluidStack(), TooltipFlag.Default.NORMAL),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    private void renderFluidWasteAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 30, 16, 4, 54)) {
            renderTooltip(pPoseStack, wasteRenderer.getTooltip(menu.getFluidStackWaste(), TooltipFlag.Default.NORMAL),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    private void renderEnergyAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 154, 16, 4, 54)) {
            renderTooltip(pPoseStack, energyInfoArea.getTooltips(),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    private void renderTemperatureAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 17, 16, 4, 54)) {
            renderTooltip(pPoseStack, temperatureInfoArea.getTooltips(),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(pPoseStack, x, y);
        energyInfoArea.draw(pPoseStack);
        renderer.render(pPoseStack, x + 134, y + 16, menu.getFluidStack());
        wasteRenderer.render(pPoseStack, x + 29, y + 16, menu.getFluidStackWaste());
        temperatureInfoArea.draw(pPoseStack);
    }

    private void renderProgressArrow(PoseStack pPoseStack, int x, int y) {
        if(menu.isCrafting()) {
            blit(pPoseStack, x + 104, y + 44, 177, 0, 23, menu.getScaledProgress());
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}
