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

    @Override
    public void updatePosition() {
        Vector3d velocity = new Vector3d();
		velocity.scale(1/stokesCoefficient(), force); // pN/(micrometers*Pa sec) = micrometers/sec 
		position.scaleAdd(sim.getDt(), velocity, position);
		force.set(0,0,0); // Payable in force, yarr
        if(position.x > sim.getBound().x || position.x < 0) collided = true;
		if(position.y > sim.getBound().y || position.y < 0) collided = true;
		if(position.z > sim.getBound().z || position.z < 0) collided = true;
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
