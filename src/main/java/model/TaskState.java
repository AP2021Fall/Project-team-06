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

	/**
	 * If not DONE or FAILED, task is INPROGRESS
	 */
	INPROGRESS {
		public String toString() {
			return "In Progress";
		}
	}
}
