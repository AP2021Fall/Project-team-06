package controller;

import java.time.LocalDateTime;

import model.Board;
import model.Role;
import model.Task;
import model.Team;
import model.User;

public class TeamController {
    private static TeamController controller = new TeamController();

    public static TeamController getController() {return controller;}

    public ControllerResult creatTeam(String teamName) {
        User leader = UserController.correntUser;
        Team team = new Team(teamName, leader);
        User.saveUser();
        return new ControllerResult("team created successfully",true);
    }
    
    public ControllerResult addMemberToTeam(String username, String teamName){
        User userCheck = UserController.getController().correntUser;
        if(userCheck.getRole() == Role.MEMBER){
            return new ControllerResult("You do not have access to this section",false);
        }
        User user = User.getUserByUsername(username);
        Team team = Team.getTeamByName(teamName);
        team.addMember(user);
        return new ControllerResult("member add successfully", true);
    }
    
    public ControllerResult createTaskForTeam(String teamName, String taskTitle,
                                             String startTime, String deadline){
        User user = UserController.getController().correntUser;
        if(user.getRole() == Role.MEMBER){
            return new ControllerResult("You do not have access to this section",false);
        }
        LocalDateTime createTime = LocalDateTime.parse(startTime);
        LocalDateTime endTime = LocalDateTime.parse(deadline);
        Task task = new Task(taskTitle, createTime, endTime, teamName);
        User.saveUser();
        return new ControllerResult("task created successfully",true);
    }
    
    public ControllerResult promoteTeamLeader(String teamName, String username){
        User userCheck = UserController.getController().correntUser;
        if(userCheck.getRole() == Role.MEMBER){
            return new ControllerResult("You do not have access to this section",false);
        }
        Team team = Team.getTeamByName(teamName);
        User user = User.getUserByUsername(username);
        team.setLeader(user);
        User.saveUser();
        return new ControllerResult("team leader promoted successfullt", true);
    }
    
    public ControllerResult suspendTeamMember(String username, String teamName){
        User userCheck = UserController.getController().correntUser;
        if(userCheck.getRole() == Role.MEMBER){
            return new ControllerResult("You do not have access to this section",false);
        }
        Team team = Team.getTeamByName(teamName);
        team.suspendMember(username);
        User.saveUser();
        return new ControllerResult("memeber suspend successfully", true);
    }
    
    public ControllerResult sendNotifications(String message, String senderUser){
        if(User.getUserByUsername(senderUser).getRole() != Role.ADMIN){
            return new ControllerResult("You do not have access to this section",false);
        }
        User sender = User.getUserByUsername(senderUser);
        Team.sendNotification(message, sender);
        User.saveUser();
        return new ControllerResult("Notifications sended successfully",true);
    }
    
    public ControllerResult showChatroom(String assignedTeam){
        String message = Team.getTeamByName(assignedTeam).showChatromm();
        return new ControllerResult(message, true);
    }

    public ControllerResult showTeams(){
        if(UserController.correntUser.getRole() != Role.ADMIN){
            return new ControllerResult("You do not have access to this section", false);
        }
        return new ControllerResult(Team.showTeams(), true);
    }

    public ControllerResult showTeamScoreboard(String teamName){
        Team team = Team.getTeamByName(teamName);
        return new ControllerResult(team.showScoredoard(),true);
    }

    public ControllerResult showTeamRoadmap(String teamName){
        Team team = Team.getTeamByName(teamName);
        return new ControllerResult(team.showRoadmap(),true);
    }
    
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
        User.saveUser();
        return new ControllerResult("user promoted successfully",true);
    }

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
        User.saveUser();
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
        User.saveUser();
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
        User.saveUser();
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
        User.saveUser();
        return new ControllerResult("board removed successfully",true);
    }
}
