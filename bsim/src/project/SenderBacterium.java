package project;

import java.util.Vector;

import javax.vecmath.Vector3d;

import bsim.BSim;
import bsim.particle.BSimBacterium;

public class SenderBacterium extends BSimBacterium {
	protected Vector<CleanerVesicle> cleanerList;
	protected Vector<SignalVesicle> signalList;
	protected String transmission;

	public SenderBacterium(BSim sim, Vector3d position, String transmission) {
		super(sim, position);
		cleanerList = new Vector<CleanerVesicle>();
		signalList = new Vector<SignalVesicle>();
		this.transmission = transmission;
	}

	public Vector<CleanerVesicle> getCleanerList() {
		return cleanerList;
	}

	public Vector<SignalVesicle> getSignalList() {
		return signalList;
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
		if (sim.getTime() % 12 < 3)
			signalList.add(new SignalVesicle(this.sim, new Vector3d(this.position), r));
		else
			cleanerList.add(new CleanerVesicle(this.sim, new Vector3d(this.position), r));
		setRadiusFromSurfaceArea(getSurfaceArea() - surfaceArea(r));
	}
}
