package model;

public enum Priority {
	LOWEST(0) {
		public final String toString() {
			return "Lowest";
		}
	},

	LOW(1) {
		public final String toString() {
			return "Low";
		}
	},

	HIGH(2) {
		public final String toString() {
			return "High";
		}
	},

	HIGHEST(3) {
		public final String toString() {
			return "Highest";
		}
	};

	public final int level;

	private Priority(int level) {
		this.level = level;
	}
}
