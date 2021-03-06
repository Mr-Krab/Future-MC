package com.herobrine.future.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLantern extends BlockBase {
    private static final PropertyBool HANGING = PropertyBool.create("hanging");
    private static final AxisAlignedBB SITTING_AABB = new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 0.5625, 0.6875);
    private static final AxisAlignedBB HANGING_AABB = new AxisAlignedBB(0.3125,0.0625,0.3125,0.6875,0.625,0.6875);

    public BlockLantern() {
        super(new BlockProperties("Lantern", Material.IRON));
        setLightLevel(1);
        setHardness(5);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 1);
        setDefaultState(this.blockState.getBaseState().withProperty(HANGING, false));
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (!isBlockInvalid(worldIn, pos.down()) && !isBlockInvalid(worldIn, pos.up())) { // don't touch
            if (facing == EnumFacing.DOWN) {
                return this.getDefaultState().withProperty(HANGING, true);
            }
            else {
                return this.getDefaultState().withProperty(HANGING, false);
            }
        }

        if (isBlockInvalid(worldIn, pos.down())) {
            return this.getDefaultState().withProperty(HANGING, true);
        }
        else {
            return this.getDefaultState().withProperty(HANGING, false);
        }
    }

    private boolean isBlockInvalid(World world, BlockPos blockPos) {
        IBlockState state = world.getBlockState(blockPos);
        Block block = state.getBlock();
        return (block instanceof BlockBush) || world.isAirBlock(blockPos) || isPiston(block);
    }

    private boolean isPiston(Block block) {
        return (block == Blocks.PISTON || block == Blocks.PISTON_EXTENSION || block == Blocks.PISTON_HEAD || block == Blocks.STICKY_PISTON);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }

    private boolean canBlockStay(World worldIn, BlockPos pos) {
        return (!isBlockInvalid(worldIn, pos.down())
                || (!isBlockInvalid(worldIn, pos.up())));
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!canBlockStay(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        else if (isBlockInvalid(worldIn, pos.down())) {
            if(worldIn.getBlockState(pos.up()).getBlock() == this) {

            }
            else {
                worldIn.setBlockState(pos, state.withProperty(HANGING, true));
            }
        }
        else if (isBlockInvalid(worldIn, pos.up())) {
            worldIn.setBlockState(pos, state.withProperty(HANGING, false));
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HANGING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (meta != 1) {
            return this.getDefaultState().withProperty(HANGING, true);
        }
        else {
            return this.getDefaultState().withProperty(HANGING, false);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getValue(HANGING)) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return blockState.getValue(HANGING) ? 15 : 0;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return state.getValue(HANGING) ? HANGING_AABB : SITTING_AABB;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess source, BlockPos pos) {
        return false;
    }


    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }


    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }


    @Override
    public boolean isTopSolid(IBlockState state) {
        return false;
    }
}