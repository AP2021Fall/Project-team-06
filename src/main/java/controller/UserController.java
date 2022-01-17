package controller;

import java.time.LocalDate;

import model.Role;
import model.Team;
import model.User;
import view.LoginAndRegisterMenu;

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
        return new ControllerResult("message send successfully", true);
    }
    
    public ControllerResult showTask(String assignedTeam){
        Team team = Team.getTeamByName(assignedTeam);
        String output = team.showTasks();
        return new ControllerResult(output, true);
    }
    
    public ControllerResult createUser(String username, String password, String password2, String email){
        if(User.userExists(username)){
            return new ControllerResult("user with username " + username + " already exists", false);
        }

        if(!password.equals(password2)){
            return new ControllerResult("These two passwords are not equal",false);
        }

        Role role = Role.MEMBER;
        new User(username, password, email, role);
        return new ControllerResult("user created successfully", true);
    }

    public ControllerResult login(String username, String password) {
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with this username!", false);
        }

        User user = User.getUserByUsername(username);

        if(!user.isValidPassword(password)){
            return new ControllerResult("invalid password!", false);
        }

        correntUser = user;
        correntUser.addLog();
        return new ControllerResult("login successfully", true);
    }

    public ControllerResult banUser(String username) {
        if(User.getUserByUsername(username).getRole() != Role.ADMIN){
            return new ControllerResult("You do not have access to this section",false);
        }
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        User user = User.getUserByUsername(username);
        User.banUser(user);
        return new ControllerResult("user banned successfully",true);
    }

    public ControllerResult changeRole(String username, Role newRole){
        if(User.getUserByUsername(username).getRole() != Role.ADMIN){
            return new ControllerResult("You do not have access to this section",false);
        }
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        User.changeRole(username, newRole);
        return new ControllerResult("change role successfully",true);
    }

    public ControllerResult changeUserPassword(String username, String oldPassword, String newPassword){
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }

        User user = User.getUserByUsername(username);

        if (!user.isValidPassword(oldPassword))
            return new ControllerResult("wrong old password!", false);
        else if (user.oldUsedPassword(newPassword))
            return new ControllerResult("Please type a New Password!", false);
        else if (!User.isStrongPassword(newPassword))
            return new ControllerResult(
                    "Please choose A Strong Password (Containing at least 8 characters including 1 digit and 1 Capital Letter)",
                    false);

        user.changePassword(newPassword);
        return new ControllerResult("password changed successfully",true);
    }
    
    public ControllerResult changeUsername(String username, String assignedUser){
        if (username.length() < 4)
            return new ControllerResult("Your username must include at least 4 characters!", false);
        else if (username.equals(assignedUser))
            return new ControllerResult("you already have this username!", false);
        else if (User.userExists(username))
            return new ControllerResult("username already taken!", false);
        else if (User.usernameHasSpecialChars(username))
            return new ControllerResult(
                    "New username contains Special Characters! Please remove them and try again!",
                    false);

        correntUser.changeUsername(username);
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
//        System.out.println(username);
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
        if(correntUser.getRole() == Role.MEMBER){
            return new ControllerResult("You do not have access to this section",false);
        }
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        User.changeRole(username, newRole);
        return new ControllerResult("changed role successfully",true);
    }

    public ControllerResult sendMessage(String username,String message, int teamId){
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        User user = User.getUserByUsername(username);
        Team team = Team.getTeamById(teamId);
        team.sendMessage(user, message);
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

    public void loadUsers() {
        User.loadUsers();
    }
    
    public void saveUsers(){
        User.saveUser();
    }

    public void updateAssignedUser(String username) {
        LoginAndRegisterMenu.assignedUser = username;
    }
}
