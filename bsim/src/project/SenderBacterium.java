package project;
import java.util.Vector;

import javax.vecmath.Vector3d;

import bsim.BSim;
import bsim.particle.BSimBacterium;

public class SenderBacterium extends BSimBacterium {
    protected Vector<CleanerVesicle> cleanerList;
			protected Vector<SignalVesicle> signalList;

			public SenderBacterium(BSim sim, Vector3d position) {
				super(sim, position);
				cleanerList = new Vector<CleanerVesicle>();
				signalList = new Vector<SignalVesicle>();
			}

			public void setCleanerList(Vector<CleanerVesicle> v) {
				cleanerList = v;
			}

			public void setSignalList(Vector<SignalVesicle> v) {
				signalList = v;
			}

			@Override
			public void vesiculate() {
				double r = vesicleRadius();
				signalList.add(new SignalVesicle(this.sim, new Vector3d(this.position), r));
				if (sim.getTime() > 5)
					cleanerList.add(new CleanerVesicle(this.sim, new Vector3d(this.position), r));
				setRadiusFromSurfaceArea(getSurfaceArea() - surfaceArea(r));
			}
}
