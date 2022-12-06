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
