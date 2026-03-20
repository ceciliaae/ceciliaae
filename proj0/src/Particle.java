import edu.princeton.cs.algs4.StdRandom;

import java.awt.*;
import java.util.Map;

public class Particle {
    public ParticleFlavor flavor;
    public int lifespan;

    public static final int PLANT_LIFESPAN = 150;
    public static final int FLOWER_LIFESPAN = 75;
    public static final int FIRE_LIFESPAN = 10;
    public static final Map<ParticleFlavor, Integer> LIFESPANS =
            Map.of(ParticleFlavor.FLOWER, FLOWER_LIFESPAN,
                   ParticleFlavor.PLANT, PLANT_LIFESPAN,
                   ParticleFlavor.FIRE, FIRE_LIFESPAN);

    public Particle(ParticleFlavor flavor) {
        this.flavor = flavor;
        if (LIFESPANS.containsKey(flavor)){
            this.lifespan = LIFESPANS.get(flavor);
        }
        else {
            lifespan = -1;
        }
    }

    public Color color() {
        if (flavor == ParticleFlavor.EMPTY) {
            return Color.BLACK;
        }
        if (flavor == ParticleFlavor.SAND) {
            return Color.YELLOW;
        }
        if (flavor == ParticleFlavor.BARRIER) {
            return Color.GRAY;
        }
        if (flavor == ParticleFlavor.WATER) {
            return Color.BLUE;
        }
        if (flavor == ParticleFlavor.FOUNTAIN) {
            return Color.CYAN;
        }
        if (flavor == ParticleFlavor.PLANT) {
            double ratio = (double) Math.max(0, Math.min(lifespan, PLANT_LIFESPAN)) / PLANT_LIFESPAN;
            int g = 120 + (int) Math.round((255 - 120) * ratio);
            return new Color(0, g, 0);
        }
        if (flavor == ParticleFlavor.FIRE) {
            double ratio = (double) Math.max(0, Math.min(lifespan, FIRE_LIFESPAN)) / FIRE_LIFESPAN;
            int r = (int) Math.round(255 * ratio);
            return new Color(r, 0, 0);
        }
        if (flavor == ParticleFlavor.FLOWER) {
            double ratio = (double) Math.max(0, Math.min(lifespan, FLOWER_LIFESPAN)) / FLOWER_LIFESPAN;
            int r = 120 + (int) Math.round((255 - 120) * ratio);
            int g = 70 + (int) Math.round((141 - 70) * ratio);
            int b = 80 + (int) Math.round((161 - 80) * ratio);
            return new Color(r, g, b);
        }
        return Color.GRAY;
    }

    public void moveInto(Particle other) {
        other.flavor = flavor;
        other.lifespan = lifespan;
        flavor = ParticleFlavor.EMPTY;
        lifespan = -1;
    }

    public void fall(Map<Direction, Particle> neighbors) {
        Particle p = neighbors.get(Direction.DOWN);
        if (p.flavor == ParticleFlavor.EMPTY) {
            this.moveInto(p);
        }
    }

    public void flow(Map<Direction, Particle> neighbors) {
        int x = StdRandom.uniformInt(3);
        if (x==1) {
            Particle p = neighbors.get(Direction.LEFT);
            if (p.flavor == ParticleFlavor.EMPTY) {
                this.moveInto(p);
            }
        }
        if (x==2) {
            Particle p = neighbors.get(Direction.RIGHT);
            if (p.flavor == ParticleFlavor.EMPTY) {
                this.moveInto(p);
            }
        }
    }

    public void grow(Map<Direction, Particle> neighbors) {
        int x = StdRandom.uniformInt(10);
        if (x == 0) {
            Particle p = neighbors.get(Direction.UP);
            if (p.flavor == ParticleFlavor.EMPTY) {
                p.flavor = flavor;
                p.lifespan = PLANT_LIFESPAN;
            }
        }
        if (x == 1) {
            Particle p = neighbors.get(Direction.LEFT);
            if (p.flavor == ParticleFlavor.EMPTY) {
                p.flavor = flavor;
                p.lifespan = PLANT_LIFESPAN;
            }
        }
        if (x == 2) {
            Particle p = neighbors.get(Direction.RIGHT);
            if (p.flavor == ParticleFlavor.EMPTY) {
                p.flavor = flavor;
                p.lifespan = PLANT_LIFESPAN;
            }
        }

    }

    public void burn(Map<Direction, Particle> neighbors) {
        for (Direction d : Direction.values()) {
            Particle p = neighbors.get(d);
            int x = StdRandom.uniformInt(10);
            if (x <= 3) {
                if (p.flavor == ParticleFlavor.PLANT | p.flavor == ParticleFlavor.FLOWER) {
                    p.flavor = ParticleFlavor.FIRE;
                    p.lifespan = FIRE_LIFESPAN;
                }
            }
        }
    }

    public void action(Map<Direction, Particle> neighbors) {
        if (flavor == ParticleFlavor.EMPTY) {
            return;
        }
        if (flavor != ParticleFlavor.BARRIER) {
            this.fall(neighbors);
        }
        if (flavor == ParticleFlavor.WATER) {
            this.flow(neighbors);
        }
        if (flavor == ParticleFlavor.PLANT | flavor == ParticleFlavor.FLOWER) {
            this.grow(neighbors);
        }
        if (flavor == ParticleFlavor.FIRE) {
            this.burn(neighbors);
        }
    }

    public void decrementLifespan(){
        if (lifespan > 0) {
            lifespan -= 1;
        }
        if (lifespan == 0) {
            flavor = ParticleFlavor.EMPTY;
            lifespan = -1;
        }
    }
}