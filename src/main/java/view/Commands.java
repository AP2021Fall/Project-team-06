package view;

import java.util.regex.Pattern;

public class Commands {
    public static Pattern[] COMMAND_PATTERNS = new Pattern[]{
//            first page(LoginAndRegisterMenu) commands: 0-1
            Pattern.compile("user create --username ([^\\s]+) --password1 ([^\\s]+) --password2 ([^\\s]+) --email Address ([^\\s]+)"),  //Creating Users
            Pattern.compile("user login --username ([^\\s]+) --password ([^\\s]+)"),   //Login
//            second page(profileMenu) commands: 2-9
            Pattern.compile("enter menu (.*)"),   // To Enter A Menu
            Pattern.compile("profile --change --oldPassword ([^\\s]+) --newPassword ([^\\s]+)"),   //Change Password
            Pattern.compile("profile --change --username ([^\\s]+)"),   //Change Username
            Pattern.compile("profile --showTeams"),   //Show Teams
            Pattern.compile("profile --showTeam ([^\\s]+)"),   //Show Own Teams
            Pattern.compile("profile --show --myProfile"),   //Show Profile
            Pattern.compile("profile --show logs"),   // Show Logs
            Pattern.compile("profile --show notifications"),   //Show Notifications
//            Task Page(TaskMenu) commands: 10-15
//              Team Leader Commands
            Pattern.compile("edit --task --id (\\d+) --title (.*)"),   //change title
            Pattern.compile("edit --task --id (\\d+) --description (.*)"),   //Change Description
            Pattern.compile("edit --task --id (\\d+) --priority ([^\\s]+)"),   //Change Priority
            Pattern.compile("edit --task --did (\\d+) --Deadline ([^\\s]+)"),   //Change Deadline
            Pattern.compile("edit --task --id (\\d+) --assignedUsers ([^\\s]+) --remove"),   //Remove Assigned User
            Pattern.compile("edit --task --id (\\d+) --assignedUsers ([^\\s]+) --add"),   //Add To Assigned Users
//            Team Menu(TeamMenu) commands: 16-22
            Pattern.compile("Enter team ([^\\s]+)"),   //Enter Team Page
            Pattern.compile("Scoreboard --show"),   //Show Scoreboard
            Pattern.compile("Roadmap --show"),   //Show Roadmap
            Pattern.compile("Chatroom --show"),   //Show Chatroom
            Pattern.compile("send --message \"(.*)\"") ,  //Send Message ?
            Pattern.compile("show tasks"),   //Show Tasks
            Pattern.compile("show task --id (\\d+)"),   //Show Task
//            Team :: Board Menu(BoardMenu) commands:
//              Leader commands 23-26
            Pattern.compile("board --new --name ([^\\s]+)"),   //Create Stage 1 Board
            Pattern.compile("board --remove --name ([^\\s]+)"),   //Remove Board From Team
            Pattern.compile("board --select --name ([^\\s]+)"),   //Select Command
            Pattern.compile("board --deselect"),
//              Stage One Board Command 27-29
            Pattern.compile("board --new --category ([^\\s]+) --name ([^\\s]+)"),   //create category
            Pattern.compile("board --category ([^\\s]+) --column (\\d+) --name ([^\\s]+)"),   //Move / Create Category In Column
            Pattern.compile("board --done --name ([^\\s]+)"),   //Finish Stage One
//              Stage Two Board Commands 30-34
            Pattern.compile("board --add (\\d+) --name ([^\\s]+)"),   //Add Task To Board
            Pattern.compile("board --assign ([^\\s]+) --task (\\d+) --name ([^\\s]+)"),   //Assign Task To Team Member
            Pattern.compile("board --force --category ([^\\s]+) --task ([^\\s]+) --name ([^\\s]+)"),   //Change Task State
            Pattern.compile("board --category next --task ([^\\s]+) --name ([^\\s]+)"),   //Move Task To Next State
            Pattern.compile("board --show --category ([^\\s]+) --board ([^\\s]+)"),   //Show Task By Category
//              Task Progression 35-36
            Pattern.compile("board --show ([^\\s]+) --name --board ([^\\s]+)"),   //Show Done/Failed Tasks
            Pattern.compile("board --open --task ([^\\s]+) (--assign ([^\\s]+))? --deadline ([^\\s]+) (--category ([^\\s]+))? --name ([^\\s]+)"),   //Restart Task
//              Normal Board Member Commands (Team Member Commands) 37-37
            Pattern.compile("Board --show --name ([^\\s]+)"),
//            Calendar Menu 38-38
            Pattern.compile("calendar --show deadlines"),
//            Team Leader Tasks: 39-41
            Pattern.compile("show --teams"),   //Show Teams
            Pattern.compile("show --team <team name or team number in the list>"),   //Team Select
            Pattern.compile("create --team ([^\\s]+)"),   //Create Team
//            Team Leader Commands After Selecting Team 42-51
            Pattern.compile("sudo show --all --tasks"),   //Show All Team Tasks
            Pattern.compile("create task --title ([^\\s]+) --startTime ([^\\s]+) --deadline ([^\\s]+)"),   //Create Task For Team
            Pattern.compile("show --members"),   //List Team Members
            Pattern.compile("Add member --username ([^\\s]+)"),   //Add Member To Team
            Pattern.compile("delete member --username ([^\\s]+)"),   //Remove Member From Team
            Pattern.compile("suspend member --username ([^\\s]+)"),   //Suspend Team Member
            Pattern.compile("promote --username ([^\\s]+) --rate (\\d+)"),   //Promote To Team Leader
            Pattern.compile("assign member --task (\\d+) --username ([^\\s]+)"),   //Assign Member To Task
            Pattern.compile("show --scoreboard"),   //Show Scoreboard
            Pattern.compile("send --notification ([^\\s]+) --username ([^\\s]+)"),   //Send Notifications
//            Admin Commands 52-58
            Pattern.compile("show profile --username ([^\\s]+)"),   //Query User Profile
            Pattern.compile("ban user --user ([^\\s]+)"),   //Ban User
            Pattern.compile("change role --user ([^\\s]+) --role ([^\\s]+)"),   //Change Role
            Pattern.compile("send --notificaion <notification> --all\n" +
                    "send --notificaion <notification> --user <username>\n" +
                    "send --notificaion <notification> --team <team name>"),   //Send Notification
            Pattern.compile("show --pendingTeams"),   //Accept New Teams
            Pattern.compile("accept --teams ([^\\s]+)"),   //Accept New Teams
            Pattern.compile("reject --teams <team names>"),   //Reject Team
//            BACK and EXIT 59-60
            Pattern.compile("back"),
            Pattern.compile("exit")
    };
}

