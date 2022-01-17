package controller;

import java.time.LocalDateTime;

import model.Board;
import model.Role;
import model.Task;
import model.Team;
import model.User;

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

        Team team = Team.getTeamByName(teamName);
        assert team != null;
        team.addMember(user);
        return new ControllerResult("member add successfully", true);
    }
    
    public ControllerResult createTaskForTeam(String teamName, String taskTitle,
                                             String startTime, String deadline){
        User user = UserController.correntUser;
        if(user.getRole() == Role.MEMBER){
            return new ControllerResult("You do not have access to this section",false);
        }
        LocalDateTime createTime = LocalDateTime.parse(startTime);
        LocalDateTime endTime = LocalDateTime.parse(deadline);
        Task task = new Task(taskTitle, createTime, endTime, teamName);
        return new ControllerResult("task created successfully",true);
    }
    
    public ControllerResult promoteTeamLeader(String teamName, String username){
        User userCheck = UserController.correntUser;
        if(userCheck.getRole() == Role.MEMBER){
            return new ControllerResult("You do not have access to this section",false);
        }
        Team team = Team.getTeamByName(teamName);
        User user = User.getUserByUsername(username);

        assert team != null;

        team.setLeader(user);
        return new ControllerResult("team leader promoted successfullt", true);
    }
    
    public ControllerResult suspendTeamMember(String username, String teamName){
        User userCheck = UserController.correntUser;
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
    public ControllerResult promoteTeamMember(String teamName, String username){
        User userCheck = UserController.getController().correntUser;
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
        team.promoteMember(username);
        return new ControllerResult("user promoted successfully",true);
    }

    @Privileged
    public ControllerResult deleteTeamMember(String teamName, String username){
        User userCheck = UserController.getController().correntUser;
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

    public ControllerResult changeTeamTaskCategoryInBoard(String teamName, String category, String taskTitle, String boardName){
        User userCheck = UserController.getController().correntUser;
        if(userCheck.getRole() == Role.MEMBER){
            return new ControllerResult("You do not have access to this section",false);
        }
        if(!Team.teamExists(teamName)){
            return new ControllerResult("team does not exist with this name",false);
        }
        Team team = Team.getTeamByName(teamName);
        team.changeTaskCategoryInBoard(category, taskTitle, boardName);
        return new ControllerResult("category changed successfully",true);
    }

    public ControllerResult addTeamBoard(String teamName, String boardName){
        User userCheck = UserController.getController().correntUser;
        if(userCheck.getRole() == Role.MEMBER){
            return new ControllerResult("You do not have access to this section",false);
        }
        if(!Team.teamExists(teamName)){
            return new ControllerResult("team does not exist with this name",false);
        }
        Team team = Team.getTeamByName(teamName);
        team.addBoard(boardName);
        return new ControllerResult("board add successfully",true);
    }
    
    public ControllerResult removeTeamBoard(String teamName, String boardName){
        User userCheck = UserController.getController().correntUser;
        if(userCheck.getRole() == Role.MEMBER){
            return new ControllerResult("You do not have access to this section",false);
        }
        if(!Team.teamExists(teamName)){
            return new ControllerResult("team does not exist with this name",false);
        }
        Team team = Team.getTeamByName(teamName);
        team.removeBoard(boardName);
        return new ControllerResult("board removed successfully",true);
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
}
