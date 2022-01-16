package controller;

import java.time.LocalDate;

import model.Role;
import model.Team;
import model.User;

public class UserController {
    private static final UserController controller = new UserController();

    public static UserController getController() {
        return controller;
    }

    private UserController() {}

    public boolean checkLeaderPrivilege(String username) {
        Role userRole = User.getUserByUsername(username).getRole();
        if (userRole == null)
            return false;
        return userRole == Role.ADMIN;
    }

    public ControllerResult createUser(String username, String password1, String password2, String email) {
        if (User.userExists(username))
            return new ControllerResult("user with username " + username + " already exists!", false);
        else if (!password1.equals(password2))
            return new ControllerResult("Your passwords are not the same!", false);
        else if (User.emailExists(email))
            return new ControllerResult("User with this email already exists!", false);
        else
            new User(username, password1, email, Role.MEMBER);
        return new ControllerResult("user created successfully!", true);
    }

    public ControllerResult login(String username, String password) {
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with this username!", false);
        }
        User user = User.getUserByUsername(username);
        if(!user.isValidPassword(password)){
            return new ControllerResult("invalid password!", false);
        }
        return new ControllerResult("login successfully", true);
    }

    public ControllerResult banUser(String username) {
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        User user = User.getUserByUsername(username);
        User.banUser(user);
        return new ControllerResult("user banned successfully",true);
    }

    public ControllerResult changeRole(String username, Role newRole){
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        User.changeRole(username, newRole);
        return new ControllerResult("change role successfully",true);
    }

    public ControllerResult changeUserPassword(String username, String newPassword){
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        User user = User.getUserByUsername(username);
        user.changePassword(newPassword);
        return new ControllerResult("password changed successfully",true);
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

    public ControllerResult setRole(String username, Role newRole){
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        User.changeRole(username, newRole);
        return new ControllerResult("changed role successfully",true);
    }

    public ControllerResult sendMessage(String username,String message, String teamName){
        int teamId = Team.getTeamByName(teamName).getId();
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        User user = User.getUserByUsername(username);
        user.sendMessage(message,teamId);
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
        return new ControllerResult("birthday updated successfully",true);
    }

}
