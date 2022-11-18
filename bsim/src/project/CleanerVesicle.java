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

    public boolean interaction(SignalVesicle v) {
        // If the cleaner is touching a signal, return true
        if (outerDistance(v) < 0)
            System.out.println("Collision occurred");
        collided = outerDistance(v) < 0;
        return outerDistance(v) < 0;
    }
}
