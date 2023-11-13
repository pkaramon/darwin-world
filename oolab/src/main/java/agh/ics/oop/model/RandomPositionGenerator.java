package agh.ics.oop.model;

import java.util.*;

/*
    Sparse Fisher-Yates Sampler(n,k)
    From:
    Simple, Optimal Algorithms for Random Sampling Without Replacement
    Daniel Ting
    arXiv:2104.05091v1
 */
public class RandomPositionGenerator implements Iterable<Vector2d>, Iterator<Vector2d> {
    private final int width;
    private final int sampleSize;
    private final int populationSize;
    private int processed  = 0;
    private final Random random = new Random();
    private final HashMap<Integer, Integer> pool = new HashMap<>();

    public RandomPositionGenerator(int width, int height, int sampleSize) {
        this.width = width;
        this.populationSize = height*width;
        this.sampleSize = sampleSize;
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return processed < sampleSize;
    }

    @Override
    public Vector2d next() {
        int i = processed;

        int rand = i + random.nextInt(populationSize - i);
        int sampled = pool.getOrDefault(rand,rand);
        pool.put(rand,  pool.getOrDefault(populationSize -i, populationSize -i));
        if (rand == populationSize -i) {
            pool.remove(populationSize -i) ;
        }

        processed++;
        return new Vector2d(sampled / width, sampled % width);
    }
}
