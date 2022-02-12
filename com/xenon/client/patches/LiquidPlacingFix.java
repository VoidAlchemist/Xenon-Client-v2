package com.xenon.client.patches;

import com.xenon.util.readability.Hook;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBucket;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
/**
 * Ghost liquid blocks fixed.
 * @author VoidAlchemist
 *
 */
public class LiquidPlacingFix {

	/**
	 * {@link net.minecraft.item.ItemBucket#tryPlaceContainedLiquid(World, BlockPos)} does <code>world.setBlockState()</code> without checking if
	 * the world is remote, thus resulting in client side ghost blocks. Simply wrapped the setBlockState call with a <code>!isRemote</code> check.
	 * @param bucket
	 * @param world
	 * @param pos
	 * @return
	 */
	@Hook("net.minecraft.item.ItemBucket#onItemRightClick -> line 92")
	public static boolean tryPlaceLiquidFixed(ItemBucket bucket, World world, BlockPos pos) {
		if (bucket.isFull == Blocks.air)
        {
            return false;
        }
        else
        {
            Material material = world.getBlockState(pos).getBlock().getMaterial();
            boolean flag = !material.isSolid();

            if (!world.isAirBlock(pos) && !flag)
            {
                return false;
            }
            else
            {
                if (world.provider.doesWaterVaporize() && bucket.isFull == Blocks.flowing_water)
                {
                    int i = pos.getX();
                    int j = pos.getY();
                    int k = pos.getZ();
                    world.playSoundEffect((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

                    for (int l = 0; l < 8; ++l)
                    {
                    	world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)i + world.rand.nextDouble(), (double)j + world.rand.nextDouble(), (double)k + world.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
                    }
                }
                else
                {
                    if (!world.isRemote) {
                    	if (flag && !material.isLiquid())
                    		world.destroyBlock(pos, true);
                    	world.setBlockState(pos, bucket.isFull.getDefaultState(), 3);
                    }
                }

                return true;
            }
        }
	}
	
}
