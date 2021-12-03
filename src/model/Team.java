package model;

import java.util.ArrayList;

public interface Team {
	public static ArrayList<Team> teams = new ArrayList<Team>();
	public static ArrayList<Team> pendingTeams = new ArrayList<Team>();
	
	public static Team getTeamById(int id) {
		for (Team team: teams)
			if (team.getId() == id)
				return team;
		return null;
	}
	
	public static String showPendingTeams() {
		if (pendingTeams.size() == 0)
			return "There are no Teams in Pending Status!";
		
		String output = "Pending teams:";
		for (int i = 1; i <= pendingTeams.size(); i++)
			output += String.format("\n%d. %s", i, pendingTeams.get(i-1).getName());
		return output;
	}
	
	public int getId();
	public String getName();
	public String showScoreboard();
	public String showRoadmap();
	public String showChatroom();
	public String showTasks();
	public void addBoard(String name);
	public void removeBoard(String name);
	public void addTaskToBoard(String boardName, int taskId);
	public void assignUserToBoardTask(String teamMemberUsername, int taskId, String boardName);
	public void changeTaskCategory(String category, String taskTitle, String boardName);
	public void progressTask(String taskTitle, String boardName);
	public String boardTaskByCategory(String boardName, String category);
	public String showBoard(String boardName);
	public String showMembers();
	public void suspendMember(String username);
	public void deleteMember(String username);
	public void promoteMember(String username);
	public void assignMemberToTask(String username, String taskTitle);
}
