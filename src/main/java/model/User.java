package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class User {
    private static int idCounter = 0;
    private ArrayList<Team> teams;
    private final int id;
    private String username;
    private String password;
    private ArrayList<String> usedPassword;
    private Role role;
    private String email;
    private LocalDateTime dateOfBirth;
    private int score;
    private ArrayList<LocalDateTime> log;
    private boolean banned;

    public User(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;

        this.id = assignId();
        this.teams = new ArrayList<>();
        this.usedPassword = new ArrayList<>();
        this.log = new ArrayList<>();
        this.dateOfBirth = null;
        this.role = role;
        this.score = 0;
        this.banned = false;
        UserSave.addUser(this);
    }
    
    public static void loadUsers(){
        UserSave.load();
    }

    public static void saveUser(){
        UserSave.save();
    }

    private static int assignId() {
        ++idCounter;
        return idCounter-1;
    }

    public static void banUser(User user) {
        user.banned = true;
    }

    public static void changeRole(String username, Role role) {
        getUserByUsername(username).setRole(role);
    }

    public static User getUserByUsername(String username) {
        for(User user : UserSave.getUsers()){
            if(user.username.equals(username)){
                return user;
            }
        }
        return null;
    }

    public static User getUserById(int id) {
        for(User user : UserSave.getUsers()){
            if(user.id == id){
                return user;
            }
        }
        return null;
    }

    public static boolean userExists(String username) {
        for(User user : UserSave.getUsers()){
            if (user.username.equals(username)){
                return true;
            }
        }
        return false;
    }

    public static boolean userExists(int id) {
        for(User user : UserSave.getUsers()){
            if(user.id == id){
                return true;
            }
        }
        return false;
    }

    public static boolean emailExists(String email) {
        for(User user : UserSave.getUsers()){
            if(user.username.equals(email)){
                return true;
            }
        }
        return false;
    }

    public static void clearAll() {
        UserSave.getUsers().clear();
        idCounter = 0;
    }

    public void addLog() {
        log.add(Board.getNow());
    }

    public Role getRole() {
        return role;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int scoreToAdd) { this.score += scoreToAdd; }

    public void setUsername(String Username) {
        username = Username;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<Team> getTeams() { return teams; }

    public void addToTeams(Team team) {
        this.teams.add(team);
    }

    public void setPassword(String Password) {
        password = Password;
        usedPassword.add(Password);
    }
    public void setEmail(String Email) {
        email = Email;
    }

    public boolean isValidPassword(String Password) {
        return password.equals(Password);
    }

    public void changePassword(String newPassword) {
        password = newPassword;
        usedPassword.add(newPassword);
    }

    public boolean oldUsedPassword(String Password) {
        for(String pass : usedPassword){
            if(pass.equals(Password)){
                return true;
            }
        }
        return false;
    }

    public String showTeams() {
        String showTeam = "";
        String teamName;
        for(int i=teams.size() ; i>0 ; i--){
            teamName = teams.get(i).getName();
            showTeam += String.valueOf(teams.size()-i+1)+". "+teamName+"\n";
        }
        return showTeam;
    }

    public String showTeam(String teamName) {
        Team team = Team.getTeamByName(teamName);
        String showTeam;
        ArrayList<String> member = team.getMember();
        int n=1;
        showTeam = "name: "+teamName+"\n";
        showTeam += "leader: "+team.getLeader().username+"\n";
        if(!team.getLeader().username.equals(username)){
            showTeam += "1. "+username+"\n";
            n++;
        }
        for(int i=1 ; i<=member.size(); i++){
            if(member.get(i).equals(username)){
                member.remove(username);
            }
        }
        for(String Name : member){
            showTeam += n+". "+Name+"\n";
        }
        return showTeam; 
    }

    public void setDateOfBrith(LocalDateTime date) {
        dateOfBirth = date;
    }

    public String showProfile() {
        String profile;
        profile = "Name: "+username+"\n";
        profile += "username: "+username+"\n";
        profile += "date of birth: "+dateOfBirth+"\n";
        profile += "email: "+email+"\n";
        profile += "role: "+role+"\n";
        profile += "score: "+score;
        return profile;
    }

    public static String showProfile(String username) {
        User user = getUserByUsername(username);
        return user.showProfile();
    }

    public String showLogs() {
        String allLogs = "";
        for(LocalDateTime date : log){
            allLogs += date+"\n";
        }
        return allLogs;
    }

    public String listTeams() {
        String output = "";
        int i=1;
        for(Team team : teams){
            output += i+"- "+team.getName()+"\n";
            i++;
        }
        return output;
    }

    public boolean teamsExists() {
        if(teams.size() > 0){
            return true;
        }
        return false;
    }

    public boolean teamExists(String Name) {
        for(Team team : teams){
            if(team.getName().equals(Name)){
                return true;
            }
        }
        return false;
    }
    
    public boolean teamExists(int Id) {
        for(Team team : teams){
            if(team.getId() == id){
                return true;
            }
        }
        return false;
    }

    public void setRole(Role newRole) {
        role = newRole;
    }

    public void sendMessage(String message, int teamId) {
        ;
    }

    public String showCalender() {
        //get all teams get all boards get all task assigned and no finished
        String output = "";
        ArrayList<Task> assignTasks = Task.getAllTasksAssignedToUser(this);
        if(assignTasks.size() < 1){
            return "no deadlines";
        }
        long time;
        for(Task task : assignTasks){
            time = ChronoUnit.DAYS.between(Board.getNow(), task.getDeadline());
            if(time < 4 && time >= 0){
                output += "***"+task.getDeadline().format(DateTimeFormatter.ISO_DATE)+"__remaining days: "+time+" team: "+task.getTeamName() + "\n";
            }else if(time < 10){
                output += "**"+task.getDeadline().format(DateTimeFormatter.ISO_DATE)+"__remaining days: "+time+" team: "+task.getTeamName() + "\n";
            }else{
                output += "*"+task.getDeadline().format(DateTimeFormatter.ISO_DATE)+"__remaining days: "+time+" team: "+task.getTeamName() + "\n";
            }
        }

        return output;
    }

    public String showTeamTasks(String teamName) {
        Team team = Team.getTeamByName(teamName);
        return team.showTasks();
    }
}
