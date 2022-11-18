package project;

import javax.vecmath.Vector3d;
import bsim.BSim;
import bsim.particle.BSimVesicle;

public class SignalVesicle extends BSimVesicle {

    public SignalVesicle(BSim sim, Vector3d position, double radius) {
        super(sim, position, radius);
    }
}
