package project;

import javax.vecmath.Vector3d;
import bsim.BSim;
import bsim.particle.BSimVesicle;

public class CleanerVesicle extends BSimVesicle {
    public boolean collided;

    public CleanerVesicle(BSim sim, Vector3d position, double radius) {
        super(sim, position, radius);
        collided = false;
    }

    public boolean getCollided() {
        return collided;
    }

    @SuppressWarnings("unchecked")
    public boolean interaction(SignalVesicle v) {
        // If the cleaner is touching a signal, return true
        boolean intersect = outerDistance(v) <= 0;
        if (!collided) {
            collided = intersect;
        }
        if (!v.collided) {
            v.collided = intersect;
        }
        return intersect;
    }
}
