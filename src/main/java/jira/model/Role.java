package jira.model;

public enum Role {
	ADMIN {
		public String toString() {
			return "System Administrator";
		}
	},
	
	LEADER {
		public String toString() {
			return "Team Leader";
		}
	},
	
	MEMBER {
		public String toString() {
			return "Team Member";
		}
	}
}
