package controller;

import model.Board;
import model.Task;
import model.Team;
import model.User;

public class TeamController {
    private static TeamController controller = new TeamController();

    public static TeamController getController() {return controller;}

    public ControllerResult creatTeam(String teamName, User leader) {
        Team team = new Team(teamName, leader);
        return new ControllerResult("team created successfully",true);
    }
    
    public ControllerResult showChatroom(String assignedTeam){
        String message = Team.getTeamByName(assignedTeam).showChatromm();
        return new ControllerResult(message, true);
    }

    public ControllerResult showTeams(){
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

    public ControllerResult deleteTeamMember(String teamName, String username){
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
        if(!Team.teamExists(teamName)){
            return new ControllerResult("team does not exist with this name",false);
        }
        Team team = Team.getTeamByName(teamName);
        team.changeTaskCategoryInBoard(category, taskTitle, boardName);
        return new ControllerResult("category changed successfully",true);
    }

    public ControllerResult addTeamBoard(String teamName, String boardName){
        if(!Team.teamExists(teamName)){
            return new ControllerResult("team does not exist with this name",false);
        }
        Team team = Team.getTeamByName(teamName);
        team.addBoard(boardName);
        return new ControllerResult("board add successfully",true);
    }

    public ControllerResult removeTeamBoard(String teamName, String boardName){
        if(!Team.teamExists(teamName)){
            return new ControllerResult("team does not exist with this name",false);
        }
        Team team = Team.getTeamByName(teamName);
        team.removeBoard(boardName);
        return new ControllerResult("board removed successfully",true);
    }
}
