package agh.ics.oop.presenter;

import agh.ics.oop.model.animals.Animal;
import agh.ics.oop.model.genes.Genotype;

import java.util.List;

public record WatchedAnimalInfo(
        boolean isDead,
        String genotype,
        int energy,
        int plantsEaten,
        int childrenCount,
        int descendantsCount,
        int deathDay,
        int lifeLength
        ){

    public static WatchedAnimalInfo fromAnimal(Animal watchedAnimal, int currentDay) {
        if(watchedAnimal == null) {
            return null;
        }

        return new WatchedAnimalInfo(
                watchedAnimal.isDead(),
                prettyPrintGenotype(watchedAnimal.getGenotype()),
                watchedAnimal.getEnergy(),
                watchedAnimal.getPlantsEaten(),
                watchedAnimal.getChildrenCount(),
                watchedAnimal.getDescendantsCount(),
                watchedAnimal.getDeathDay(),
                calculateLifeLength(watchedAnimal, currentDay)
        );
    }


    private static String prettyPrintGenotype(Genotype genotype) {
        List<Integer> genes = genotype.getGenes();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < genes.size(); i++) {
            if(i == genotype.getCurrentGeneIndex()) {
                sb.append("<").append(genes.get(i)).append(">");
            } else {
                sb.append(genes.get(i)).append(" ");
            }
        }
        return sb.toString();
    }

    private static int calculateLifeLength(Animal watchedAnimal, int currentDay) {
        if(watchedAnimal.isDead()) {
            return watchedAnimal.getDeathDay() - watchedAnimal.getBirthDay();
        } else {
            return currentDay - watchedAnimal.getBirthDay();
        }
    }
}