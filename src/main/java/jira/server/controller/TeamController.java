package jira.server.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import jira.ControllerResult;
import jira.server.model.Board;
import jira.Role;
import jira.server.model.Task;
import jira.server.model.Team;
import jira.server.model.User;

public class TeamController {
    private static final TeamController controller = new TeamController();

    public static TeamController getController() {return controller;}

    public ControllerResult creatTeam(String leader, String teamName) {
        if (!Team.isValidTeamName(teamName))
            return new ControllerResult("Team name is invalid!", false);
        else if (Team.teamExists(teamName) || Team.pendingExists(teamName))
            return new ControllerResult("There is another team with this name!", false);

        Team team = new Team(teamName, User.getUserByUsername(leader));
        Team.addToPending(team);
        return new ControllerResult("Team created successfully! Waiting For Admin’s confirmation …",true);
    }

    @Privileged
    public ControllerResult showPendingTeams(String username) {
        if (User.getUserByUsername(username).getRole() != Role.ADMIN)
            return new ControllerResult("You do not have access to this section",false);

        return new ControllerResult(Team.showPendingTeams(), true);
    }

    @Privileged
    public ControllerResult acceptPendingTeam(String username, String teamNames) {
        if (User.getUserByUsername(username).getRole() != Role.ADMIN)
            return new ControllerResult("You do not have access to this section",false);

        String[] teams = teamNames.split("\\s+");
        for (String teamName: teams)
            if (!Team.pendingExists(teamName))
                return new ControllerResult("Some teams are not in pending status! Try again", false);

        Team.acceptPending(teams);

        return new ControllerResult(null, true);
    }

    @Privileged
    public ControllerResult rejectPendingTeam(String username, String teamNames) {
        if (User.getUserByUsername(username).getRole() != Role.ADMIN)
            return new ControllerResult("You do not have access to this section",false);

        String[] teams = teamNames.split("\\s+");
        for (String teamName: teams)
            if (!Team.pendingExists(teamName))
                return new ControllerResult("Some teams are not in pending status! Try again", false);

        Team.rejectPending(teams);

        return new ControllerResult(null, true);
    }

    @Privileged
    public ControllerResult addMemberToTeam(String username, String teamName, String memberUsername){
        if(User.getUserByUsername(username).getRole() == Role.MEMBER){
            return new ControllerResult("You do not have access to this section",false);
        }

        User user = User.getUserByUsername(memberUsername);
        if (user == null)
            return new ControllerResult("No user exists with this username!", false);
        if (user.getRole() != Role.MEMBER)
            return new ControllerResult("You can't add this user to the team!", false);

        Team team = Team.getTeamByName(teamName);
        assert team != null;
        team.addMember(user);
        return new ControllerResult("member add successfully", true);
    }
    
    public ControllerResult createTaskForTeam(String assignedUser, String teamName, String taskTitle,
                                             String startTime, String deadline){
        User user = User.getUserByUsername(assignedUser);
        if(user.getRole() == Role.MEMBER){
            return new ControllerResult("You do not have access to this section",false);
        }

        LocalDateTime createTime;
        try {
            LocalDate createDate = LocalDate.parse(startTime);
            createTime = createDate.atStartOfDay();
        }
        catch (DateTimeParseException e) {
            return new ControllerResult("Invalid start date!",false);
        }

        LocalDateTime endTime;
        try {
            LocalDate deadlineDate = LocalDate.parse(deadline);
            endTime = deadlineDate.atStartOfDay();
        }
        catch (DateTimeParseException e) {
            return new ControllerResult("Invalid deadline!",false);
        }

        new Task(taskTitle, createTime, endTime, teamName);
        return new ControllerResult("task created successfully",true);
    }
    
    public ControllerResult promoteTeamLeader(String teamName, String assignedUser, String username){
        User userCheck = User.getUserByUsername(assignedUser);
        if(userCheck.getRole() == Role.MEMBER){
            return new ControllerResult("You do not have access to this section",false);
        }
        Team team = Team.getTeamByName(teamName);
        User user = User.getUserByUsername(username);

        assert team != null;

        team.setLeader(user);
        return new ControllerResult("team leader promoted successfully", true);
    }
    
    public ControllerResult suspendTeamMember(String assignedUser, String teamName, String username){
        User userCheck = User.getUserByUsername(assignedUser);
        if(userCheck.getRole() == Role.MEMBER){
            return new ControllerResult("You do not have access to this section",false);
        }
        Team team = Team.getTeamByName(teamName);

        assert team != null;

        team.suspendMember(username);
        return new ControllerResult("memeber suspend successfully", true);
    }
    
    public ControllerResult sendNotifications(String message, String senderUser){
        if(User.getUserByUsername(senderUser).getRole() != Role.ADMIN){
            return new ControllerResult("You do not have access to this section",false);
        }
        User sender = User.getUserByUsername(senderUser);
        Team.sendNotification(message, sender);
        return new ControllerResult("Notifications sended successfully",true);
    }
    
    public ControllerResult showChatroom(String assignedTeam){
        String message = Team.getTeamByName(assignedTeam).showChatromm();
        return new ControllerResult(message, true);
    }

    @Privileged
    public ControllerResult showTeams(String username){
        if (User.getUserByUsername(username).getRole() == Role.MEMBER)
            return new ControllerResult("You do not have access to this section",false);

        return new ControllerResult(Team.showTeams(username), true);
    }

    /**
     * Lists the name of teams that the user is either the leader
     * or a member.
     * @param username Username of the one to check
     * @return List of team names
     */
    public ArrayList<String> showTeamsAffiliated(String username) {
        User user = User.getUserByUsername(username);
        if (user != null) {
            if (user.getRole() == Role.MEMBER)
                return showMemberTeams(user);
            else if (user.getRole() == Role.LEADER)
                return showLeaderTeams(user);
        }
        return null;
    }

    public ArrayList<Integer> getAffiliatedTeamsMemberCount(String username) {
        User user = User.getUserByUsername(username);
        if (user != null) {
            if (user.getRole() == Role.MEMBER)
                return getMemberCountOfTeams(Team.getTeamsUserIsMemberOf(user));
            else if (user.getRole() == Role.LEADER)
                return getMemberCountOfTeams(Team.getTeamsLedByUser(user));
        }

        return null;
    }

    private ArrayList<Integer> getMemberCountOfTeams(ArrayList<Team> teams) {
        ArrayList<Integer> memberCounts = new ArrayList<>();
        for (Team team: teams)
            memberCounts.add(team.getMember().size());
        return memberCounts;
    }

    private ArrayList<String> showLeaderTeams(User user) {
        ArrayList<String> teamNames = new ArrayList<>();
        ArrayList<Team> teamsLedByUser = Team.getTeamsLedByUser(user);
        for (Team team: teamsLedByUser)
            teamNames.add(team.getName());
        return teamNames;
    }

    private ArrayList<String> showMemberTeams(User user) {
        ArrayList<String> teamNames = new ArrayList<>();
        ArrayList<Team> teamsUserIsMemberOf = Team.getTeamsUserIsMemberOf(user);
        for (Team team: teamsUserIsMemberOf)
            teamNames.add(team.getName());
        return teamNames;
    }

    public ControllerResult showAllTeams() {
        return new ControllerResult(Team.showAllTeams(), true);
    }

    public ControllerResult showTeamScoreboard(String teamName){
        Team team = Team.getTeamByName(teamName);
        return new ControllerResult(team.showScoredoard(),true);
    }

    public ControllerResult showTeamRoadmap(String teamName){
        Team team = Team.getTeamByName(teamName);
        return new ControllerResult(team.showRoadmap(),true);
    }

    @Privileged
    public ControllerResult deleteTeamMember(String username, String assignedUser, String teamName){
        User userCheck = User.getUserByUsername(assignedUser);
        if(userCheck.getRole() == Role.MEMBER){
            return new ControllerResult("You do not have access to this section",false);
        }
        if(!Team.teamExists(teamName)){
            return new ControllerResult("team does not exist with this name",false);
        }
        Team team = Team.getTeamByName(teamName);
        if(!team.isMember(username)){
            return new ControllerResult("this user is not a member of the team",false);
        }
        team.deleteMember(username);
        return new ControllerResult("user delete from team successfully",true);
    }

    public ControllerResult showTeamMembers(String teamName){
        if(!Team.teamExists(teamName)){
            return new ControllerResult("team does not exist with this name",false);
        }

        Team team = Team.getTeamByName(teamName);
        return new ControllerResult(team.showMember(),true);
    }

    public ControllerResult showTeamTasks(String teamName){
        if(!Team.teamExists(teamName)){
            return new ControllerResult("team does not exist with this name",false);
        }
        Team team = Team.getTeamByName(teamName);
        return new ControllerResult(team.showTasks(),true);
    }

    public ControllerResult showTeamBoard(String teamName, String boardName){
        if(!Team.teamExists(teamName)){
            return new ControllerResult("team does not exist with this name",false);
        }
        if(!Board.boardExists(teamName, boardName)){
            return new ControllerResult("board does not exist with this name",false);
        }
        Team team = Team.getTeamByName(teamName);
        return new ControllerResult(team.showBoard(boardName),true);
    }

    public ControllerResult showTeamBoardTaskByCategory(String teamName, String boardName, String category){
        if(!Team.teamExists(teamName)){
            return new ControllerResult("team does not exist with this name",false);
        }
        Team team = Team.getTeamByName(teamName);
        return new ControllerResult(team.showBoardTaskByCategory(boardName, category),true);
    }

    public ControllerResult progressTeamTask(String teamName, String taskTitle, String boardName){
        if(!Team.teamExists(teamName)){
            return new ControllerResult("team does not exist with this name",false);
        }
        Board board = Board.getBoardByName(teamName, boardName);
        return new ControllerResult("progress done"+board.getTaskPercentDone(board.getTaskByTitle(taskTitle))+"%",true);
    }

    public ControllerResult checkTeamToken(String username, String teamToken) {
        Team team = Team.getTeamByName(teamToken);
        if (team != null)
            return new ControllerResult(teamToken, true);

        try {
            int teamNumber = Integer.parseInt(teamToken);
            team = Team.getNthTeam(username, teamNumber);
            if (team != null)
                return new ControllerResult(team.getName(), true);
        }
        catch (NumberFormatException e) {
            return new ControllerResult("Team not found!", false);
        }
        return new ControllerResult("Team not found!", false);
    }

    public boolean teamIsSelectable(String teamName) {
        return Team.teamExists(teamName);
    }

    public ArrayList<String> getMemberData(String teamName) {
        Team team = Team.getTeamByName(teamName);
        assert team != null;
        return team.getMember();
    }

    public ArrayList<String> getPendingTeams() {
        ArrayList<String> pendingTeamNames = new ArrayList<>();
        for (Team team: Team.getPendingTeams())
            pendingTeamNames.add(team.getName());

        return pendingTeamNames;
    }
}
