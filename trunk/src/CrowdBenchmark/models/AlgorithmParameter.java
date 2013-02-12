package CrowdBenchmark.models;


public class AlgorithmParameter extends AbstractModel {
	private boolean majorityDecision;
	private boolean honeyPot;
	private boolean elice;
	private boolean em;
	private boolean slme;
	private boolean glad;
	private boolean iterativeLearning;

	public boolean isMajorityDecision() {
		return majorityDecision;
	}

	public void setMajorityDecision(boolean majorityDecision) {
		propertyChangeSupport
				.firePropertyChange("majorityDecision", this.majorityDecision,
						this.majorityDecision = majorityDecision);
	}

	public boolean isHoneyPot() {
		return honeyPot;
	}

	public void setHoneyPot(boolean honeyPot) {
		propertyChangeSupport.firePropertyChange("honeyPot", this.honeyPot,
				this.honeyPot = honeyPot);
	}

	public boolean isElice() {
		return elice;
	}

	public void setElice(boolean elice) {
		propertyChangeSupport.firePropertyChange("elice", this.elice,
				this.elice = elice);
	}

	public boolean isEm() {
		return em;
	}

	public void setEm(boolean em) {
		propertyChangeSupport.firePropertyChange("em", this.em, this.em = em);
	}

	public boolean isSlme() {
		return slme;
	}

	public void setSlme(boolean slme) {
		propertyChangeSupport.firePropertyChange("slme", this.slme,
				this.slme = slme);
	}

	public boolean isGlad() {
		return glad;
	}

	public void setGlad(boolean glad) {
		propertyChangeSupport.firePropertyChange("glad", this.glad,
				this.glad = glad);
	}

	public boolean isIterativeLearning() {
		return iterativeLearning;

	}

	public void setIterativeLearning(boolean iterativeLearning) {
		propertyChangeSupport.firePropertyChange("iterativeLearning",
				this.iterativeLearning,
				this.iterativeLearning = iterativeLearning);
	}
}
