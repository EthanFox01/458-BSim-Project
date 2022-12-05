package project;

import javax.vecmath.Vector3d;
import bsim.BSim;
import bsim.particle.BSimVesicle;

public class SignalVesicle extends BSimVesicle {
    protected String popId;
    public boolean collided;

    public SignalVesicle(BSim sim, Vector3d position, double radius, String popId) {
        super(sim, position, radius);
        this.popId = popId;
        this.collided = false;
    }

    @SuppressWarnings("unchecked")
    public void interaction(ReceiverBacterium b) {
        if (outerDistance(b) < 0 && !collided) {
            this.collided = true;
            if (popId == "a") {
                ReceiverBacterium.incrementReceivedInTimeslotA();
            } else {
                ReceiverBacterium.incrementReceivedInTimeslotB();
            }
        }
    }
}
