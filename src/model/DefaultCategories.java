package model;

public enum DefaultCategories {
	FAILED {
		public String toString() {
			return "Failed";
		}
	},
	
	DONE {
		public String toString() {
			return "Done";
		}
	},
	
	TODO {
		public String toString() {
			return "Todo";
		}
	}
}
