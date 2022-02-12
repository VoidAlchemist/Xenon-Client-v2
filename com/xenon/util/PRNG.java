/*
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.xenon.util;
/**
 * All I did was implementing a non thread-safe version of {@link java.util.Random}.<br>
 * Every single method is a copy-paste without thread-safe locks.
 * {@link java.util.concurrent.ThreadLocalRandom} is great but you cannot set the seed, and MC world gen needs that...
 * @author Frank Yellin
 * @see java.util.Random
 */
public class PRNG {
	
	private long seed;
	private static final long multiplier = 0x5DEECE66DL;
    private static final long addend = 0xBL;
    private static final long mask = (1L << 48) - 1;
    private static final double DOUBLE_UNIT = 0x1.0p-53; // 1.0 / (1L << 53)

    private double nextNextGaussian;
    private boolean haveNextNextGaussian = false;
    private static long seedUniquifier = 8682522807148012L;
    
    // IllegalArgumentException messages
    private static final String BadBound = "bound must be positive";
    private static final String BadRange = "bound must be greater than origin";
    private static final String BadSize  = "size must be non-negative";
    
    
	public PRNG() {
        this(seedUniquifier() ^ System.nanoTime());
    }

    private static long seedUniquifier() {
        // L'Ecuyer, "Tables of Linear Congruential Generators of
        // Different Sizes and Good Lattice Structure", 1999
    	long current = seedUniquifier;
    	seedUniquifier *= 181783497276652981L;
        return seedUniquifier;
    }
    
    private static long initialScramble(long seed) {
        return (seed ^ multiplier) & mask;
    }
    
    public PRNG(long seed) {
    	this.seed = initialScramble(seed);
    }
    public void setSeed(long seed) {
        this.seed = initialScramble(seed);
        haveNextNextGaussian = false;
    }
    
    
    protected int next(int bits) {
        this.seed = (this.seed * multiplier + addend) & mask;
        return (int)(this.seed >>> (48 - bits));
    }
    
    public void nextBytes(byte[] bytes) {
        for (int i = 0, len = bytes.length; i < len; )
            for (int rnd = nextInt(),
                     n = Math.min(len - i, Integer.SIZE/Byte.SIZE);
                 n-- > 0; rnd >>= Byte.SIZE)
                bytes[i++] = (byte)rnd;
    }
    public int nextInt() {
        return next(32);
    }
    
    public int nextInt(int bound) {
        if (bound <= 0)
            throw new IllegalArgumentException(BadBound);

        int r = next(31);
        int m = bound - 1;
        if ((bound & m) == 0)  // i.e., bound is a power of 2
            r = (int)((bound * (long)r) >> 31);
        else {
            for (int u = r;
                 u - (r = u % bound) + m < 0;
                 u = next(31))
                ;
        }
        return r;
    }
    
    public long nextLong() {
        // it's okay that the bottom word remains signed.
        return ((long)(next(32)) << 32) + next(32);
    }
    public boolean nextBoolean() {
        return next(1) != 0;
    }
    public float nextFloat() {
        return next(24) / ((float)(1 << 24));
    }
    public double nextDouble() {
        return (((long)(next(26)) << 27) + next(27)) * DOUBLE_UNIT;
    }
    public double nextGaussian() {
        // See Knuth, ACP, Section 3.4.1 Algorithm C.
        if (haveNextNextGaussian) {
            haveNextNextGaussian = false;
            return nextNextGaussian;
        } else {
            double v1, v2, s;
            do {
                v1 = 2 * nextDouble() - 1; // between -1 and 1
                v2 = 2 * nextDouble() - 1; // between -1 and 1
                s = v1 * v1 + v2 * v2;
            } while (s >= 1 || s == 0);
            double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s)/s);
            nextNextGaussian = v2 * multiplier;
            haveNextNextGaussian = true;
            return v1 * multiplier;
        }
    }

}
