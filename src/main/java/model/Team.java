package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.jar.Attributes.Name;


public class Team {
    private static ArrayList<Team> teams = new ArrayList<>();
    private static ArrayList<Team> pendingTeams = new ArrayList<>();
    private static int idCounter = 0;
    private final int id;
    private String name;
    private User leader;
    private ArrayList<User> members;
    private ArrayList<Notification> chat;
    private ArrayList<Board> boards;

    public Team(String name, User leader) {
        this.id = assignId();
        this.name = name;
        this.leader = leader;
        this.members = new ArrayList<User>();
        this.chat = new ArrayList<Notification>();
        this.boards = new ArrayList<Board>();
        teams.add(this);
    }

    private static int assignId() {
        ++idCounter;
        return idCounter-1;
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

    public static String showPendingTeams() {
		if (pendingTeams.size() == 0)
			return "There are no Teams in Pending Status!";

		String output = "Pending teams:";
		for (int i = 1; i <= pendingTeams.size(); i++)
			output += String.format("\n%d. %s", i, pendingTeams.get(i-1).getName());
		return output;
	}

    public String showScoredoard() {
        int number=1;
        String strScoreboard = "Rank\tUsername\tScore";
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
        for(int i=score.size() ; i>0 ; i--){
            for(int j=1 ; j<=memberName.size() ;j++){
                if(scoreboard.get(name) == score.get(i)){
                    strScoreboard += number+"\t"+name+"\t"+score.get(i)+"\n";
                    number++;
                }
            }
        }
        return strScoreboard;
    }

    public String showRoadmap() {
        String output = "";
        ArrayList<Task> allTasks = new ArrayList<>();
        for(Board board : boards){
            allTasks = board.getTasks();
            for(Task task : allTasks){
                output += task.getTitle()+" : "+task.getPercentDone()+"% done\n";
            }
        }
        return output;
    }

    public String showChatromm() {
        if(chat.size()<1){
            return "no message yet";
        }
        String output = "";
        for(Notification notification : chat){
            output += notification.getSender()+" : "+notification.getMessage();
        }
        return output;
    }

    public String showTasks() {
        if(boards.size()<1){
            return "no task yet";
        }
        String output = "";
        int i=1;
        ArrayList<Task> allTasks = new ArrayList<>();
        ArrayList<User> assignUser = new ArrayList<>();
        for(Board board : boards){
            allTasks = board.getTasks();
            for(Task task : allTasks){
                output += i+"."+task.getTitle()+": id "+task.getId()+",creation date : "+task.getCreationDate();
                output += ",deadline :"+task.getDeadline()+",assign to :";
                i++;
                assignUser = task.getAssignedUsers();
                for(User user : assignUser){
                    output += user.getUsername()+" ";
                }
                output += ",priority :"+task.getPriority().toString()+"\n";
            }
        }
        return output;
    }

    public void addBoard(String Name) {
        Board board = new Board(name, Name);
        boards.add(board);
    }

    public void removeBoard(String Name) {
        for(Board board : boards){
            if(board.getName().equals(Name)){
                boards.remove(board);
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
        Board.getBoardByName(name, boardName).getTaskByTitle(taskTitle).setCategory(category);
    }

    public void progressTask(String taskTitle, String boardName) {
        ;
    }

    public String showBoardTaskByCategory(String boardName, String category) {
        Task task = Task.getTaskByTitle(name, category);
        String output = task.getTitle()+": id"+task.getId()+",creation date : ";
        output += task.getCreationDate()+",deadline :"+task.getDeadline()+",assign to :";
        ArrayList<User> assignUser = task.getAssignedUsers();
        for(User user : assignUser){
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
        ArrayList<User> assignUser = task.getAssignedUsers();
        for(User user : assignUser){
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
        String stringName = "";
        for(User user : members){
            memberName.add(user.getUsername());
        }
        memberName.sort(Comparator.naturalOrder());
        for(String nameMember : memberName){
            stringName += nameMember+"\n";
        }
        return stringName;
    }

    public void suspendMember(String username) {
        ;
    }

    public void deleteMember(String username) {
        members.remove(User.getUserByUsername(username));
    }

    public void promoteMember(String username) {
        leader = User.getUserByUsername(username);
    }

    public void assignMemberToTask(String username, String taskTitle) {
        Task task = Task.getTaskByTitle(name, taskTitle);
        task.assignUserToTask(User.getUserByUsername(username));
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        name = Name;
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
}
