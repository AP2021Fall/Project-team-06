package model;

public enum Priority {
	LOWEST {
		public String toString() {
			return "Lowest";
		}
	},
	
	LOW {
		public String toString() {
			return "Low";
		}
	},
	
	HIGH {
		public String toString() {
			return "High";
		}
	},
	
	HIGHEST {
		public String toString() {
			return "Highest";
		}
	}
}
