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
        if(!powerOfPassword(password)){
            return new ControllerResult("Password power is low",false);
        }
        Role role = Role.MEMBER;
        User user = new User(username, password, email, role);
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

    public ControllerResult changeUserPassword(String username, String newPassword){
        if(!User.userExists(username)){
            return new ControllerResult("no user exists with username!",false);
        }
        if(!powerOfPassword(newPassword)){
            return new ControllerResult("Password power is low",false);
        }
        User user = User.getUserByUsername(username);
        if(user.oldUsedPassword(newPassword)){
            return new ControllerResult("this password has been userd before",false);
        }
        user.changePassword(newPassword);
        return new ControllerResult("password changed successfully",true);
    }
    
    public boolean powerOfPassword(String password){
        Pattern pattern1 = Pattern.compile("[a-z]+");
        Pattern pattern2 = Pattern.compile("[A-Z]+");
        Pattern pattern3 = Pattern.compile("[1-9]+");
        if(!pattern1.matcher(password).find()){
            return false;
        }
        if(!pattern2.matcher(password).find()){
            return false;
        }
        if(!pattern3.matcher(password).find()){
            return false;
        }
        return true;
    }
    
    public ControllerResult changeUsername(String username){
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
}
