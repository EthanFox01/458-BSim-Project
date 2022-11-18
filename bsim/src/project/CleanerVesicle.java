package project;

import javax.vecmath.Vector3d;
import bsim.BSim;
import bsim.particle.BSimVesicle;

public class CleanerVesicle extends BSimVesicle {
    public CleanerVesicle(BSim sim, Vector3d position, double radius) {
        super(sim, position, radius);
    }
}
