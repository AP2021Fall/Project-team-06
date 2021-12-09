package model;

public enum TaskState {
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

	INPROGRESS {
		public String toString() {
			return "In Progress";
		}
	}
}
