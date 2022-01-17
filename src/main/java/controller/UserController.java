package controller;

import java.time.LocalDate;

import model.Role;
import model.Team;
import model.User;

public class UserController {
    private static UserController controller = new UserController();
    public static User correntUser;

    public boolean checkLeaderPrivilege(String username) {
        Role userRole = User.getUserByUsername(username).getRole();
        if (userRole == null)
            return false;
        return userRole == Role.ADMIN;
    }

    public static UserController getController() {
        return controller;
    }
    
    public boolean duplicateUsernames(String username){
        if(User.userExists(username)){
            return true;
        }
        return false;
    }
    
    public boolean duplicateEmails(String email){
        if(User.emailExists(email)){
            return true;
        }
        return false;
    }
    
    public static ControllerResult showNotifications(){
        String output = correntUser.showNotifications();
        return new ControllerResult(output, true);
    }
    
    public ControllerResult sendMessage(String assignedTeam, String message){
        Team team = Team.getTeamByName(assignedTeam);
        team.sendMessage(correntUser, message);
        User.saveUser();
        return new ControllerResult("message send successfully", true);
    }
    
    public ControllerResult showTask(String assignedTeam){
        Team team = Team.getTeamByName(assignedTeam);
        String output = team.showTasks();
        return new ControllerResult(output, true);
    }
    
    public ControllerResult createUser(String username, String password, String password2, String email){
        if(!password.equals(password2)){
            return new ControllerResult("These two passwords are not equal",false);
        }
        Role role = Role.MEMBER;
        User user = new User(username, password, email, role);
        User.saveUser();
        return new ControllerResult("user created successfully", true);
    }

    public ControllerResult login(String username, String password) {
        User.loadUsers();
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with this username!", false);
        }
        User user = User.getUserByUsername(username);
        if(!user.isValidPassword(password)){
            return new ControllerResult("invalid password!", false);
        }
        correntUser = user;
        User.saveUser();
        return new ControllerResult("login successfully", true);
    }

    @Privileged
    public ControllerResult banUser(String username) {
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        User user = User.getUserByUsername(username);
        User.banUser(user);
        User.saveUser();
        return new ControllerResult("user banned successfully",true);
    }

    @Privileged
    public ControllerResult changeRole(String username, Role newRole){
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        User.changeRole(username, newRole);
        User.saveUser();
    return new ControllerResult("change role successfully",true);
    }

    public ControllerResult changeUserPassword(String username, String newPassword){
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        User user = User.getUserByUsername(username);
        user.changePassword(newPassword);
        User.saveUser();
        return new ControllerResult("password changed successfully",true);
    }
    
    public ControllerResult changeUsername(String username){
        correntUser.changeUsername(username);
        User.saveUser();
        return new ControllerResult("username change successfully", true);
    }

    public ControllerResult showLogs(String username){
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        User user = User.getUserByUsername(username);
        return new ControllerResult(user.showLogs(),true);
    }

    public ControllerResult listTeams(String username){
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        User user = User.getUserByUsername(username);
        if(!user.teamsExists()){
            return new ControllerResult("this user is not a member of any team!",false);
        }
        return new ControllerResult(user.listTeams(),true);
    }

    public ControllerResult showTeams(String username){
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        User user = User.getUserByUsername(username);
        return new ControllerResult(user.showTeams(),true);
    }

    @Privileged
    public ControllerResult setRole(String username, Role newRole){
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        User.changeRole(username, newRole);
        User.saveUser();
        return new ControllerResult("changed role successfully",true);
    }

    public ControllerResult sendMessage(String username,String message, int teamId){
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        User user = User.getUserByUsername(username);
        Team team = Team.getTeamById(teamId);
        team.sendMessage(user, message);
        User.saveUser();
        return new ControllerResult("send message successfully",true);
    }

    public ControllerResult showCalendar(String username){
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        User user = User.getUserByUsername(username);
        return new ControllerResult(user.showCalender(),true);
    }

    public ControllerResult showProfile(String username){
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        User user = User.getUserByUsername(username);
        return new ControllerResult(user.showProfile(),true);
    }

    public ControllerResult showProfile(String username, String queryUsername){
        Role role = User.getUserByUsername(queryUsername).getRole();
        if(role != Role.ADMIN){
            return new ControllerResult("You Don't Have Access To Do This Action!",false);
        }
        User user = User.getUserByUsername(username);
        return new ControllerResult(user.showProfile(),true);
    }

    public ControllerResult setDateOfBirth(String username,String date){
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        User user = User.getUserByUsername(username);
        user.setDateOfBrith(LocalDate.parse(date).atStartOfDay());
        User.saveUser();
        return new ControllerResult("birthday updated successfully",true);
    }

    public void loadUsers() {
        User.loadUsers();
    }
}
