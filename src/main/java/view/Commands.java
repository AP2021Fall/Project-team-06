package view;

import java.util.regex.Pattern;

public class Commands {
    public static Pattern[] COMMAND_PATTERNS = new Pattern[]{
//            first page(LoginAndRegisterMenu) commands: 0-1
            Pattern.compile("user create --username (\\S+) --password1 (\\d+) --password2 (\\d+) --email Address (\\S+)"),  //Creating Users
            Pattern.compile("user login --username (\\S+) --password (\\d+)"),   //Login
//            second page(profileMenu) commands: 2-9
            Pattern.compile("enter menu (\\S+)"),   // To Enter A Menu
            Pattern.compile("profile --change --oldPassword (\\d+) --newPassword (\\d+)"),   //Change Password
            Pattern.compile("profile --change --username (\\S+)"),   //Change Username
            Pattern.compile("profile --showTeams"),   //Show Teams
            Pattern.compile("profile --showTeam (\\S+)"),   //Show Own Teams
            Pattern.compile("profile --show --myProfile"),   //Show Profile
            Pattern.compile("profile __show logs"),   // Show Logs
            Pattern.compile("profile __show notifications"),   //Show Notifications
//            Task Page(TaskMenu) commands: 10-15
//              Team Leader Commands
            Pattern.compile("edit --task --id (\\d+) --title (\\S+)"),   //change title
            Pattern.compile("edit --task --id (\\d+) --description (\\S+)"),   //Change Description
            Pattern.compile("edit --task --id (\\d+) --priority (\\S+)"),   //Change Priority
            Pattern.compile("edit --task --did (\\d+) --Deadline (\\S+)"),   //Change Deadline
            Pattern.compile("edit --task --id (\\d+) --assignedUsers (\\S+) --remove"),   //Remove Assigned User
            Pattern.compile("edit --task --id (\\d+) --assignedUsers (\\S+) --add"),   //Add To Assigned Users
//            Team Menu(TeamMenu) commands: 16-22
            Pattern.compile("Enter team (\\S+)"),   //Enter Team Page
            Pattern.compile("Scoreboard --show"),   //Show Scoreboard
            Pattern.compile("Roadmap --show"),   //Show Roadmap
            Pattern.compile("Chatroom --show"),   //Show Chatroom
            Pattern.compile("send --message \"(\\S+)\"") ,  //Send Message ?
            Pattern.compile("show tasks"),   //Show Tasks
            Pattern.compile("show task --id (\\d+)"),   //Show Task
//            Team :: Board Menu(BoardMenu) commands:
//              Leader commands 23-26
            Pattern.compile("board --new --name (\\S+)"),   //Create Stage 1 Board
            Pattern.compile("board --remove --name (\\S+)"),   //Remove Board From Team
            Pattern.compile("board --select --name (\\S+)"),   //Select Command
            Pattern.compile("board --deselect"),
//              Stage One Board Command 27-29
            Pattern.compile("board --new --category (\\S+) --name (\\S+)"),   //create category
            Pattern.compile("board --category (\\S+) --column (\\d+) --name (\\S+)"),   //Move / Create Category In Column
            Pattern.compile("board --done --name (\\S+)"),   //Finish Stage One
//              Stage Two Board Commands 30-34
            Pattern.compile("board --add (\\d+) --name (\\S+)"),   //Add Task To Board
            Pattern.compile("board --assign (\\S+) --task (\\d+) --name (\\S+)"),   //Assign Task To Team Member
            Pattern.compile("board --force --category (\\S+) --task (\\S+) --name (\\S+)"),   //Change Task State
            Pattern.compile("board --category next --task (\\S+) --name (\\S+)"),   //Move Task To Next State
            Pattern.compile("board --show --category (\\S+) --board (\\S+)"),   //Show Task By Category
//              Task Progression 35-36
            Pattern.compile("board --show (\\S+) --name --board (\\S+)"),   //Show Done/Failed Tasks
            Pattern.compile("board --open --task (\\S+) (--assign (\\S+))? --deadline (\\S+) (--category (\\S+))? --name (\\S+)"),   //Restart Task
//              Normal Board Member Commands (Team Member Commands) 37-37
            Pattern.compile("Board --show --name (\\S+)"),
//            Calendar Menu 38-38
            Pattern.compile("calendar --show deadlines"),
//            Team Leader Tasks: 39-41
            Pattern.compile("show --teams"),   //Show Teams
            Pattern.compile("show --team <team name or team number in the list>"),   //Team Select
            Pattern.compile("create --team (\\S+)"),   //Create Team
//            Team Leader Commands After Selecting Team 42-51
            Pattern.compile("sudo show --all --tasks"),   //Show All Team Tasks
            Pattern.compile("create task --title (\\S+) --startTime (\\S+) --deadline (\\S+)"),   //Create Task For Team
            Pattern.compile("show --members"),   //List Team Members
            Pattern.compile("Add member --username (\\S+)"),   //Add Member To Team
            Pattern.compile("delete member --username (\\S+)"),   //Remove Member From Team
            Pattern.compile("suspend member --username (\\S+)"),   //Suspend Team Member
            Pattern.compile("promote --username (\\S+) --rate (\\d+)"),   //Promote To Team Leader
            Pattern.compile("assign member --task (\\d+) --username (\\S+)"),   //Assign Member To Task
            Pattern.compile("show --scoreboard"),   //Show Scoreboard
            Pattern.compile("send --notification (\\S+) --username (\\S+)"),   //Send Notifications
//            Admin Commands 52-58
            Pattern.compile("show profile --username (\\S+)"),   //Query User Profile
            Pattern.compile("ban user --user (\\S+)"),   //Ban User
            Pattern.compile("change role --user (\\S+) --role (\\S+)"),   //Change Role
            Pattern.compile("send --notificaion <notification> --all\n" +
                    "send --notificaion <notification> --user <username>\n" +
                    "send --notificaion <notification> --team <team name>"),   //Send Notification
            Pattern.compile("show --pendingTeams"),   //Accept New Teams
            Pattern.compile("accept --teams (\\S+)"),   //Accept New Teams
            Pattern.compile("reject --teams <team names>"),   //Reject Team
//            BACK and EXIT 59-60
            Pattern.compile("back"),
            Pattern.compile("exit")
    };
}

