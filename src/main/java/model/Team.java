package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Pattern;


public class Team {
    private static ArrayList<Team> teams = new ArrayList<>();
    private static ArrayList<Team> pendingTeams = new ArrayList<>();
    private static int idCounter = 0;
    private final int id;
    private String name;
    private User leader;
    private ArrayList<User> members;
    private ArrayList<Notification> chat;

    public Team(String name, User leader) {
        this.id = assignId();
        this.name = name;
        this.leader = leader;
        this.members = new ArrayList<User>();
        this.chat = new ArrayList<Notification>();
        Board.initTeamBoard(name);
    }

    private static int assignId() {
        ++idCounter;
        return idCounter-1;
    }
	 
    public void sendMessage(User sender ,String message){
        Notification notification = new Notification(message, sender);
        chat.add(notification);
    }
	
    public static void sendNotification(String message, User sender){
        for(Team team : teams){
            team.sendMessage(sender, message);
        }
    }

    public void addMember(User user) {
        members.add(user);
        user.addToTeams(this);
    }

    public static Team getTeamById(int id) {
        for(Team team : teams){
            if(team.id == id){
                return team;
            }
        }
        return null;
    }

    public static Team getTeamByName(String Name) {
        for(Team team : teams){
            if(team.name.equals(Name)){
                return team;
            }
        }
        return null;
    }

    public static Team getPendingByName(String Name) {
        for(Team team : pendingTeams){
            if(team.name.equals(Name)){
                return team;
            }
        }
        return null;
    }

    public static boolean teamExists(String name) {
        return getTeamByName(name) != null;
    }

    public static boolean pendingExists(String name) {
        return getPendingByName(name) != null;
    }
	
    public static String showTeams(String leaderName){
        StringBuilder output = new StringBuilder("teams:");
        for (int i = 1; i <= teams.size(); i++)
            if (teams.get(i-1).leader.getUsername().equals(leaderName))
                output.append(String.format("\n%d. %s", i, teams.get(i - 1).getName()));

        return output.toString();
    }

    public static Team getNthTeam(String leaderName, int n){
        int m = n;
        for (int i = 1; i <= teams.size(); i++)
            if (teams.get(i-1).leader.getUsername().equals(leaderName)) {
                --m;
                if (m == 0)
                    return teams.get(i);
            }

        return null;
    }

    public static String showAllTeams(){
        StringBuilder output = new StringBuilder("teams:");
        for (int i = 1; i <= teams.size(); i++)
            output.append(String.format("\n%d. %s", i, teams.get(i - 1).getName()));

        return output.toString();
    }

    public static boolean isValidTeamName(String teamName) {
        Pattern pattern1 = Pattern.compile("^\\d.*$");
        Pattern pattern2 = Pattern.compile("(?=.*\\d)(?=.*[A-Z])([^\\s+])");

        return !pattern1.matcher(teamName).find() &&
                pattern2.matcher(teamName).find() &&
                teamName.length() >= 5 &&
                teamName.length() <= 12;
    }

    public static String showPendingTeams() {
	if (pendingTeams.size() == 0)
		return "There are no Teams in Pending Status!";

	String output = "Pending teams:";
	for (int i = 1; i <= pendingTeams.size(); i++)
		output += String.format("\n%d. %s", i, pendingTeams.get(i-1).getName());
	return output;
    }

    public static void clearAll() {
        teams.clear();
        pendingTeams.clear();
    }

    public static void addToPending(Team team) {
        pendingTeams.add(team);
    }

    private static void acceptPending(String teamName) {
        Team team = getPendingByName(teamName);
        if (team != null) {
            teams.add(team);
            team.leader.setRole(Role.LEADER);
            pendingTeams.remove(team);
        }
    }

    public static void acceptPending(String[] teamNames) {
        for (String teamName: teamNames)
            acceptPending(teamName);
    }

    private static void rejectPending(String teamName) {
        Team team = getPendingByName(teamName);
        if (team != null)
            pendingTeams.remove(team);
    }

    public static void rejectPending(String[] teamNames) {
        for (String teamName: teamNames)
            rejectPending(teamName);
    }

    public String showScoredoard() {
        int number=1;
        StringBuilder strScoreboard = new StringBuilder("Rank\tUsername\tScore\n");
        HashMap<String,Integer> scoreboard = new HashMap<>();
        ArrayList<String> memberName = new ArrayList<>();
        ArrayList<Integer> score = new ArrayList<>();
        for(User user : members){
            scoreboard.put(user.getUsername(), user.getScore());
            memberName.add(user.getUsername());
        }
        memberName.sort(Comparator.naturalOrder());
        for(String name : memberName){
            score.add(scoreboard.get(name));
        }
        score.sort(Comparator.naturalOrder());
        for(int i=score.size()-1 ; i>=0 ; i--){
            for(int j=1 ; j<=memberName.size() ;j++){
                if(scoreboard.get(memberName.get(j-1)).equals(score.get(i))){
                    strScoreboard.append(number).append("\t").append(memberName.get(j-1)).append("\t").append(score.get(i)).append("\n");
                    number++;
                }
            }
        }
        return strScoreboard.toString();
    }

    public String showRoadmap() {
        StringBuilder output = new StringBuilder();
        ArrayList<Task> allTasks;

        for(Board board : Board.getTeamBoards(name)){
            allTasks = board.getTasks();
            for(Task task : allTasks){
                output.append(task.getTitle()).append(" : ").append(board.getTaskPercentDone(task)).append("% done\n");
            }
        }

        if (output.toString().isEmpty())
            return "no tasks yet";
        return output.toString();
    }

    public String showChatromm() {
        if(chat.size()<1){
            return "no message yet";
        }
        StringBuilder output = new StringBuilder();
        for(Notification notification : chat){
            output.append(notification.getSender().getUsername()).append(" : ").append(notification.getMessage());
        }
        return output.toString();
    }

    public String showTasks() {
        if(Board.getTeamBoards(name).size()<1){
            return "no task yet";
        }
        String output = "";
        int i=1;
        ArrayList<Task> allTasks;
        for(Board board : Board.getTeamBoards(name)){
            allTasks = board.getTasks();
            for(Task task : allTasks){
                output += i+"."+task.getTitle()+": id "+task.getId()+",creation date : "+task.getCreationDate();
                output += ",deadline :"+task.getDeadline()+",assign to :";
                i++;
                for(User user : task.getAssignedUsers().keySet()){
                    output += user.getUsername()+" ";
                }
                output += ",priority :"+task.getPriority().toString()+"\n";
            }
        }
        return output;
    }

    public void addBoard(String boardName) {
        if (!Board.boardExists(name, boardName))
            new Board(name, boardName);
    }

    /**
     * @implNote Added a few things, so that by deleting a board, any reference to
     * the tasks in that board are also removed. This makes sure that no user will
     * have them in their inventory ever.
     * @param Name board name
     */
    public void removeBoard(String Name) {
        for(Board board : Board.getTeamBoards(name)){
            if (board.getName().equals(Name)){
                board.delete();
                Board.getTeamBoards(name).remove(board);
                return;
            }
        }
    }

    public void addTaskToBoard(String boardName, Task task) {
        Board.getBoardByName(name, boardName).addTask(task);
    }

    public void assignUserToBoardTask(String teamMember, int taskId, String boardName) {
        Board.getBoardByName(name, boardName).assignUserToTask(teamMember, taskId);
    }

    public void changeTaskCategoryInBoard(String category, String taskTitle, String boardName) {
        Board.getBoardByName(name, boardName).setTaskCategory(category, taskTitle);
    }

    public String showBoardTaskByCategory(String boardName, String category) {
        Task task = Task.getTaskByTitle(name, category);
        String output = task.getTitle()+": id"+task.getId()+",creation date : ";
        output += task.getCreationDate()+",deadline :"+task.getDeadline()+",assign to :";
        for(User user : task.getAssignedUsers().keySet()){
            output += user.getUsername()+" ";
        }
        output += ",priority :"+task.getPriority().toString();
        return output;
    }

    public String showBoard(String boardName) {
        Board board = Board.getBoardByName(name, boardName);
        return board.showBoard();
    }

    public String showTask(int id) {
        Task task = Task.getTaskById(id);
        String output = task.getTitle()+": id"+task.getId()+",creation date : ";
        output += task.getCreationDate()+",deadline :"+task.getDeadline()+",assign to :";
        for(User user : task.getAssignedUsers().keySet()){
            output += user.getUsername()+" ";
        }
        output += ",priority :"+task.getPriority().toString();
        return output;
    }

    public ArrayList<String> getMember() {
        ArrayList<String> memberName = new ArrayList<>();
        for(User user : members){
            memberName.add(user.getUsername());
        }
        memberName.sort(Comparator.naturalOrder());
        return memberName;
    }

    public String showMember() {
        ArrayList<String> memberName = new ArrayList<>();
        StringBuilder stringName = new StringBuilder();
        stringName.append("Team Name: ").append(name).append("\n");
        stringName.append("Leader: ").append(leader.getUsername()).append("\n");

        for(User user : members){
            memberName.add(user.getUsername());
        }

        memberName.sort(Comparator.naturalOrder());
        for(String nameMember : memberName){
            stringName.append(nameMember).append("\n");
        }
        return stringName.toString();
    }

    public void suspendMember(String username) {
        User user = User.getUserByUsername(username);
        User.banUser(user);
    }

    public void deleteMember(String username) {
        members.remove(User.getUserByUsername(username));
    }

    public void promoteMember(String username) {
        leader = User.getUserByUsername(username);
    }

    public void assignMemberToTask(String username, String taskTitle) {
        Task task = Task.getTaskByTitle(name, taskTitle);
        assert task != null;
        task.assignUserToTask(User.getUserByUsername(username));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User user) {
        leader = user;
    }

    public int getId() {
        return id;
    }

    public boolean isMember(String username) {
        for (User member: members)
            if (member.getUsername().equals(username))
                return true;
        return false;
    }
}
