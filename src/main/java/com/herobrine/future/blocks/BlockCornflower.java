package com.herobrine.future.blocks;

import com.herobrine.future.init.Init;
import net.minecraft.block.SoundType;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

import java.util.Random;

public class BlockCornflower extends BlockFlower { //Adds blue flower
    public BlockCornflower() {
        super("FlowerBlue");
        setCreativeTab(Init.futuretab);
        setSoundType(SoundType.PLANT);
    }

    @Override
    public boolean isBiomeValid(Biome biome) {
        return biome == Biomes.PLAINS || biome == Biomes.MUTATED_FOREST;
    }

    @Override
    public boolean getSpawnChance(Random random) {
        return random.nextInt(100) > 96;
    }

    @Override
    public boolean getChunkChance(Random random) {
        return random.nextInt(100) > 90;
    }
}