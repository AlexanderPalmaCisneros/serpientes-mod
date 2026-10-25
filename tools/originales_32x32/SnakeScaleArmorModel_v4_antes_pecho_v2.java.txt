package com.snakemod.client;

import com.snakemod.SnakeMod;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

/**
 * Chestplate model: the normal armor shape plus 3D relief.
 * - Scaled shoulder pads with three fangs hanging from the outer edge.
 * - A netherite collar, a column of overlapping scales down the chest and two scales from the shoulders.
 * - Small dorsal spines down the back.
 * The extra pieces use the top half of snake_scale_layer_1.png (free, because the set has no helmet):
 * pad 0,0; collar 26,0; chest scales 0,10 / 8,10 / 26,9; spines 36,9. Fangs use x 56-60, y 16-23.
 */
public final class SnakeScaleArmorModel {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SnakeMod.MODID, "snake_scale_armor"), "main");

    // Same "inflation" vanilla uses for the outer armor layer, so it sits over the skin
    private static final CubeDeformation OUTER = new CubeDeformation(1.0F);

    // Shoulder pad bottom edge (arm space); the fangs hang from here
    private static final float PAD_BOTTOM = 0.4F;
    // Fangs: z position (front to back), how far they lean outward (radians)
    private static final float[][] FANGS = {
            {-2.0F, 0.35F},
            {0.0F, 0.25F},
            {2.0F, 0.35F},
    };
    private static final float TIP_BEND = 0.6F; // the tip curves back toward the arm, like a hook

    // Chest scales from the collar down: {width, top y}. Each is 3 tall and the next starts 2 lower,
    // so they overlap like real scales
    private static final int[][] CHEST_SCALES = {
            {4, 0}, {4, 2}, {4, 4}, {3, 6}, {3, 8}, {2, 10},
    };
    // Texture spot of each scale width (rounded scale drawn with transparent bottom corners)
    private static final int[][] SCALE_TEX = {{8, 10}, {0, 10}, {26, 9}}; // width 2, 3, 4
    // Upper chest: scales coming down from each shoulder toward the center column {x, y, tilt}
    private static final float[] MANTLE = {2.6F, 0.3F, 0.45F};
    private static final float[] SPINE_Y = {2.0F, 5.0F, 8.0F};

    private SnakeScaleArmorModel() {
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = HumanoidModel.createMesh(OUTER, 0.0F);
        PartDefinition root = mesh.getRoot();
        addShoulder(root.getChild("right_arm"), -1);
        addShoulder(root.getChild("left_arm"), 1);
        addChest(root.getChild("body"));
        return LayerDefinition.create(mesh, 64, 32);
    }

    // side = -1 for the right arm (outward is -X), +1 for the left arm
    private static void addShoulder(PartDefinition arm, int side) {
        // Small pad over the shoulder, a little wider than the arm on the outer side
        float padX = side == -1 ? -4.4F : -0.6F;
        CubeListBuilder pad = CubeListBuilder.create().texOffs(0, 0);
        if (side == 1) pad.mirror();
        // The pad grows 0.3 to the front and back: if its faces were in the same place as the sleeve's,
        // both would fight to be drawn and the shoulder would flicker (z-fighting)
        arm.addOrReplaceChild("shoulder_pad",
                pad.addBox(padX, -3.6F, -3.0F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.3F)), PartPose.ZERO);

        // Fangs hang down from the outer bottom edge of the pad, leaning out, tips curving back in
        float x = 3.6F * side;
        for (int i = 0; i < FANGS.length; i++) {
            float z = FANGS[i][0], lean = -FANGS[i][1] * side;
            // Three segments, each thinner and bent a bit more: a pointed, curved fang
            PartDefinition base = arm.addOrReplaceChild("fang_" + i,
                    CubeListBuilder.create().texOffs(56, 16).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.2F)),
                    PartPose.offsetAndRotation(x, PAD_BOTTOM - 0.3F, z, 0.0F, 0.0F, lean));
            PartDefinition middle = base.addOrReplaceChild("middle",
                    CubeListBuilder.create().texOffs(56, 20).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.05F)),
                    PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, TIP_BEND * 0.5F * side));
            middle.addOrReplaceChild("tip",
                    CubeListBuilder.create().texOffs(56, 20).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.3F)),
                    PartPose.offsetAndRotation(0.0F, 1.8F, 0.0F, 0.0F, 0.0F, TIP_BEND * 0.5F * side));
        }
    }

    private static void addChest(PartDefinition body) {
        // Netherite collar around the neck
        body.addOrReplaceChild("collar",
                CubeListBuilder.create().texOffs(26, 0).addBox(-4.5F, -1.6F, -3.5F, 9.0F, 2.0F, 7.0F),
                PartPose.ZERO);

        // Column of overlapping scales down the chest, each with its lower edge lifted
        for (int i = 0; i < CHEST_SCALES.length; i++) {
            int w = CHEST_SCALES[i][0], y = CHEST_SCALES[i][1];
            int[] tex = SCALE_TEX[w - 2];
            body.addOrReplaceChild("chest_scale_" + i,
                    CubeListBuilder.create().texOffs(tex[0], tex[1]).addBox(-w / 2.0F, 0.0F, -1.0F, w, 3.0F, 1.0F),
                    PartPose.offsetAndRotation(0.0F, y, -2.7F - i * 0.05F, -0.2F, 0.0F, 0.0F));
        }

        // Two scales on the upper chest, tilted toward the column, like a mantle coming from the shoulders
        for (int s = -1; s <= 1; s += 2) {
            body.addOrReplaceChild("mantle_" + (s < 0 ? "r" : "l"),
                    CubeListBuilder.create().texOffs(0, 10).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 3.0F, 1.0F),
                    PartPose.offsetAndRotation(MANTLE[0] * s, MANTLE[1], -2.6F, -0.2F, 0.0F, MANTLE[2] * s));
        }

        // Dorsal spines down the back, leaning down like scales
        for (int i = 0; i < SPINE_Y.length; i++) {
            body.addOrReplaceChild("spine_" + i,
                    CubeListBuilder.create().texOffs(36, 9).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 2.0F),
                    PartPose.offsetAndRotation(0.0F, SPINE_Y[i], 2.8F, -0.5F, 0.0F, 0.0F));
        }
    }
}
