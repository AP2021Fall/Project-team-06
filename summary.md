# Phase 1 Summary (Intended For Writing UML)

**Note:** Output ``invalid command`` for any wrong command.

There are 3 different users in the project:
- **Team Member**: Normal users, people who come in and complete tasks.
- **Team Leader**: They design and distribute ```tasks``` to team members.
- **System Administrator**: They maintain the system.

## First Page
---
The first page will be where users login or signup. **Only signed in users are allowed to use the services**.

### Creating Users
---
This is done by

    user create --username <username> --password1 <password> --password2 <password> --email Address <email>

The results are:

| Output | Description |
|:------:|:-----------:|
| ``user created successfully!`` | If all goes well |
| ``user with username <username> already exists!`` | If duplicate usersnames are seen |
| ``Your passwords are not the same!`` | If ``<password1>`` and ``<password2>`` do not match |
| ``User with this email already exists!`` | If duplicate emails are seen |
| ``Email address is invalid`` | Valid domains are **only** ``gmail.com`` and ``yahoo.com`` and the host may only contain **letters, numbers and the dot (.) charater** |

### Login
---

This is done by:

    user login --usernaem <username> --password <password>

The results are:

| Output | Description |
|:------:|:-----------:|
| ``user logged in successfully!`` | If all goes well |
| ``There is not any user with username: <username>!`` | If no username with ``<username>`` is found |
| ``Username and password didn't match!`` | If ``<password>`` is incorrect |


## Second Page
---

Based on user type (designated as ``ADMIN``, ``LEADER`` or ``MEMBER``), this page will serve different functions.

### To Enter A Menu
---

This is done with

    enter menu <Menu Name>

The user may also leave a menu with ``back``.

**Menus should be listed upon user login** , they can also be addressed with a number, like below:

    1. Menu Name 1
    2. Menu Name 2
    3. Menu Name 3
    etc.

The menus are the following:
- Profile Menu
- Team Menu: It has the following submenus:
    - Chat Room
    - Board Menu
    - ScoreBoard
    - RoadMaps
- Tasks Page
- Calendar Menu
- Notification Bar

### Profile Menu: Change Password
---

Used to change password. **Upon successful execution, logout the user!**.

    Profile --change --oldPassword <old password> --newPassword <new password>

The results are:

| Output | Description |
|:------:|:-----------:|
| ``wrong old password!`` | If ``<old password>`` is incorrect |
| ``Please type a New Password!`` | If ``<new password>`` equals **ANY** of the previous passwords |
| ``Please choose A Strong Password (Containing at least 8 characters including 1 digit and 1 Capital Letter)`` | self explanatory |

***BONUS***: If ``<old password>`` is wrong for 2 times, *logout the user by force*.

### Profile Menu: Change Username
---

This is done to change the username. Make sure the code reflects this change in other parts as well!

    Profile --change --username <new username>

The results are:

| Output | Description |
|:------:|:-----------:|
| ``Your username must include at least 4 characters!`` | self explanatory |
| ``username already taken!`` | If ``<username>`` is already reserved for *another* user |
| ``New username contains Special Characters! Please remove them and try again!`` | Valid characters are **numbers, letters and underscore (_)** |
| ``you already have this username!`` | If the user inputs their own username again |

### Profile Menu: Show Teams
---

Done by:

    Profile --showTeams

- For ``MEMBERS``: Output the name of teams which the user is a member of, sorted from newest to oldest.
- For ``LEADER``: List the teams which the user is the leader, once again sorted from newest to oldest.

### Profile Menu: Show Own Teams
---

Done with:

    Profile --showTeam <team Name>

Upon execution:
- The team name is displayed.
- The name of team leader is displayed.
- Now two things can happen:
    - All team member names will be listed alphabetically **if the user is the team leader**.
    - First the name of the user is displayed and then the rest of the team in alphabetical order **if the user is not the leader**.

### Profile Menu: Show Profile
---

With:

    Profile --show --myProfile

Output will include:
- Full name of the user
- Username
- Date of birth
- Email address
- User role (``ADMIN``, ``MEMBER`` or ``LEADER``)
- Accumulated points

### Profile Menu: Show Logs
---

With:

    profile --show logs

This will list the log of user logins, which consists of:
- Date
- Time (Hour and Minute)

### Profile Menu: Show Notifications
---

With:

    profile --show notifications

This will list all messages sent from **team leader to the user**. (unknown how this will behave for team leaders though ...)

# Tasks Page
---

For each task, the following must be shown:
- ``ID``: A unique ID for the task.
- ``Title``: A name for this task.
- ``Description``: (Defaults to empty) the task description *after a new line*.
- ``Priority``: Values of ``Lowest``, ``Low``, ``High`` and ``Highest``.
- ``Date and time of creation``: Task creation time in format of ``YYYY-MM-DD|HH:MM``.
- ``Date and time of deadline``: See above.
- ``Assigned Users``: After newline, list the username of each user assigned to the task. (Task assignment always sends a notification to that user)
- ***BONUS*** ``Comments``: In each line, list message and username for the sender of that message. Each line has the format of ``<username>: <message>``. These are sorted from **newest to oldest**.

## Team Leader Commands
---

If a user that does not have ``LEADER`` privileges attempts to use the commands below, the message ``You Don't Have Access To Do This Action!`` will be shown.

### Change Title
---

    edit --task --id <task id> --title <new title>

Return ``Title updated successfully!``.

### Change Description
---

    edit --task --id <task id> --description <new description>

Return ``Description updated successfully!``.

### Change Priority
---

    edit --task --id <task id> --priority <new priority>

Return ``Priority updated successfully!``.

### Change Deadline
---

    edit --task --did <task id> --Deadline <new deadline>

Results:

| Output | Description |
|:------:|:-----------:|
| ``New deadline is invalid!`` | If ``<new deadline>`` does not conform to the pattern ``YYYY-MM-DD|HH:MM``, **you must also check for correct values for date and time**, and also, **the new daedline cannot precede the creation date** |
| ``Deadline updated successfully!`` | If all goes well |

### Remove Assigned User
---

    edit --task --id <task id> --assignedUsers <username> --remove

| Output | Description |
|:------:|:-----------:|
| ``There is not any user with this username <username> in list!`` | If a username does not exist |
| ``User <username> removed successfully!`` | If all goes well |

### Add To Assigned Users
---

    edit --task --id <task id> --assignedUsers <username> --add

| Output | Description |
|:------:|:-----------:|
| ``There is not any user with this username <username>!`` | If a username does not exist |
| ``User <username> added successfully!`` | If all goes well |

# Team Menu
---

With the exception of ``ADMIN`` users, all other users are part of at least one team. Upon entering this menu, list the teams that the user is a member of (from newest to oldes, sorted alphabetically in case of stalemate).

    1 <team name 1>
    2 <team name 2>
    3 <team name 3>
    etc.

### Enter Team Page
---

    Enter team <teamName>

This can be done either with ``teamName`` or ``teamNumber`` (according to the list above). Each team has 1 leader and the rest are just ``MEMBER``s.

### Show Scoreboard
---

    Scoreboard --show

Sort *team member* points from high to low, sort alphabetically in case of stalemate. **Scores can be negative**.

### Show Roadmap
---

    Roadmap --show

List the percentage of progress for each task and it's title. Sort by percentage done and alphabetically if stalemate occures.

| Output | Description |
|:------:|:-----------:|
| ``<task title> : <percentage> % done`` | If there are some tasks to list |
| ``no tasks yet`` | If no task has been assigned yet |

### Show Chatroom
---

    Chatroom --show

| Output | Description |
|:------:|:-----------:|
| ``<sender name> : "<messages>"`` | If there are some messages to list |
| ``no messages yet`` | If there are no messages to list |

### Send Message
---

    send --message "<message>"

Note that it is necessary for the message to come between double qoutes, also, chat room must update as soon as possible (i.e. it should show the message immediately).

### Show Tasks
---

    show tasks

List all tasks based on deadline from nearest to furthest away. In case of stalemate, use task priority for sorting. The format is the following:

    1.<task title 1> : id <id>,creation date : <date> , deadline : <date> , assign to : <usernames>, priority : <high|low|… >
    2.<task title 2> : id <id>,creation date : <date> , deadline : <date> , assign to : <usernames>, priority : <high|low|… >
    etc.

The field ``assign to`` lists the username of people assigned to each task in alphabetical order. **If there is no task, print ``no tasks yet``.

### Show Task
---

    show task --id <task id>

# Team :: Board Menu
---

The Board Menu is a submenu of the Team Menu, you need to access it from there.

**Each ``Board`` is associated with one ``Team``**. The ``Board`` will contain lists like ``todo`` and ``inProgress`` which are maintained by the team leader. The user also has a board that is partitioned into ``done`` and ``failed`` lists.

## Leader Commands
---

The commands below should only be used by the leader. The members, upon using this command will see ``You do not have the permission to do this action!``.

Configuring a board has two stages for configuration:
- Creation, where the leader first attempts to create a new board object. If a command for the next stage is given in this stage, output ``Please finish creating the board first``.
- Board configuration, which we shall see later.

If at any point, the name of a board is used, but a board with that name is not found, output ``There is no board with this name``.

### Create Stage 1 Board
---

    board --new --name <board name>

| Output | Description |
|:------:|:-----------:|
| ``There is already a board with this name`` | Duplicate board name |

### Remove Board From Team
---

    board --remove --name <board name>

### (BONUS) Select Command
---

It would be tedious to input the board name for each following command. **Any** user that manipulates a board, can select a board and have all the commands that follow it *assume* that they are working with the selected board.

    board --select --name <board name>

If a board is not selected and commands are inputted without ``<board name>``, then print ``No board is selected``.

The command below can be used to deselect a board:

    board --deselect

| Output | Description |
|:------:|:-----------:|
| ``No board is selected`` | If no board is selected |

## Stage One Board Command
---

### create category
---

    board --new --category <category name> --name <board name>

| Output | Description |
|:------:|:-----------:|
| ``The name is already taken for a category!`` | If the category name is already taken |

### Move / Create Category In Column
---

    board --category <category name> --column <column> --name <board name>


| Output | Description |
|:------:|:-----------:|
| ``wrong column!`` | If the number for the column is not correct |

**Make sure to increment the number of columns following ``<column>``**.

### Finish Stage One
---

    board --done --name <board name>

| Output | Description |
|:------:|:------------|
| ``Please make a category first`` | If no category has been created yet |

## Stage Two Board Commands
---

### Add Task To Board
---

    board --add <task id> --name <board name>

| Output | Description |
|:------:|:-----------:|
| ``This task has already been added to this board ``| If the task already exists on the board |
| ``Invalid task id!`` | If the task does not exist yet |
| ``The deadline of this task has already passed`` | If the task deadline has already passed |
| ``Please assign this task to someone first`` | If the task does not have any assignee |

### Assign Task To Team Member
---

    board --assign <team member> --task <task id> --name <board name>

| Output | Description |
|:------:|:-----------:|
| ``Invalid task id`` | If the task is not on the board |
| ``Invalid teammate`` | If the a member with name ``<team member>`` is not found in the group |
| ``This task has already finished`` | If the task was already finished and moved to ``done`` category |

### Change Task State
---

    board --force --category <Todo/Inprogress/...> --task <task title> --name <board name>

| Output | Description |
|:------:|:-----------:|
| ``There is no task with given information`` | If the task is not found on the board |
| ``Invalid category`` | If the category after ``--force`` is invalid |
| ``board --category next --task <task> --name <board name>`` | Move the task to it's next stage |

### Move Task To Next State
---

    board --category next --task <task> --name <board name>

| Output | Description |
|:------:|:-----------:|
| ``This task is not assigned to you`` | If the current user is not the leader |
| ``Invalid task!`` | If a task with the given info is not found |

### (BONUS) Show Task By Category
---

    board --show --category <category name> --board <board name>

**Output format**:

    Task <task title> by <username> is waiting for confirmation
    Task <task title> by <teammate> is in progress
    etc.

| Output | Description |
|:------:|:-----------:|
| ``invalid category`` | If ``<category name>`` is not valid |

## Task Progression
---

Upon successful execution of a task, it will be moved to the ``done`` category and an amount of **10 points** will be awarded to the assignee. If the deadline for a task has passed, it will automatically move to the ``failed`` category and an amount of **-5 points** will be awarded to the assignee.

### Show Done/Failed Tasks
---

    board --show <done/failed> --name --board <board name>

### Restart Task
---

    board --open --task <task title> (--assign <teammate>)? --deadline <deadline> (--category <category name>)? --name <board name>

| Output | Description |
|:------:|:-----------:|
| ``You do not have the permission to this action`` | If the input is not given by the team leader |
| ``Invalid task`` | If the task is not on the board |
| ``This task is not in failed section`` | If the task is not in the ``Failed`` state |
| ``Invalid category`` | Nonexistent category name |
| ``Invalid teammember`` | Nonexistent teammember name |

## Normal Board Member Commands (Team Member Commands)
---

    Board --show --name <board name>

Outputs:

    Board name: <board name>
    Board completion: percentage of Done tasks
    Board failed: percentage of failed tasks
    Board leader: leader name
    Board tasks: <tasks listed from Highest to Lowest priority>
        Title: <title>
        Category: <Todo/Inprogress/Done/...>
        Description: <description>
        Creation Date: <date and time of creation>
        Deadline: <deadline>
        Assigned to: <teammate>
        Status: <Failed/Done/Inprogress>
        etc.

# Calendar Menu
---

### Show deadlines
---

    calendar --show deadlines

List deadlines sorted from nearest to furthest. The output format is:

    <stars> YYYY-MM-DD <assigner team name> | remaining days: <remaining days>

Hers, ``<stars>`` will be:
- ``***`` if there is **less than 4 days** until deadline.
- ``**`` if there is between 4 to 10 days (inclusive) until deadline.
- ``*`` if more than 10 days is left until deadline.

If no deadlines exist, show ``no deadlines``. If a deadline has passed, it will no be shown.

# Team Leader Tasks
---

**Only the team leader or an ``ADMIN`` can override changes done by a team leader, they also possess the privilege to run all normal user commands**.

### Show Teams
---

    show --teams

This will list all teams under the supervision of the current leader. They are sorted by date (from early to late) and alphabetically in case of stalemate.

    1- <Team 1>
    2- <Team 2>
    3- <Team 2>
    etc.

If no teams are out, print ``There is no team for you!``.

### Team Select
---

    show --team <team name or team number in the list>

| Output | Description |
|:------:|:-----------:|
| ``Team not found!`` | If the team name or number does not match an available team |

### Create Team
---

    create --team <team name>

| Output | Description |
|:------:|:-----------:|
| ``Team name is invalid!`` | Team names must be **between 5 to 12 characters long**, contain **at least one capital and one number**, and **should not start with a number** |
| ``There is another team with this name!`` | Duplicate name |
| ``Team created successfully! Waiting For Admin’s confirmation …`` | Success! now the leader waits for an ``ADMIN`` to confirm |

## Team Leader Commands After Selecting Team
---

### Show All Team Tasks
---

    sudo show --all --tasks

preferred to sort this by time, the information shall be:
- Date of creation
- Associated users
- Current state
- Deadline

### Create Task For Team
---

    create task --title <task title> --startTime <start time> --deadline <deadline>

| Output | Description |
| ``Task created successfully!`` | If all goes well |
| ``There is another task with this title!`` | Duplicate title |
| ``Invalid start date!`` | Invalid date format for ``<start date>``, should be ``YYYY-MM-DD|HH:MM`` |
| ``Invalid deadline!`` | Invalid date format for ``<deadline>``, should be ``YYYY-MM-DD|HH:MM`` |

### List Team Members
---

    show --members

### Add Member To Team
---

    Add member --username <username>

| Output | Description |
|:------:|:-----------:|
| ``No user exists with this username!`` | Nonexistent username |

### Remove Member From Team
---

    delete member --username <username>

Same as above ...

### Suspend Team Member
---

    suspend member --username <username>

Same as above ...

### Promote To Team Leader
---

    promote --username <username> --rate <rate>

After this, the current user **is no longer the leader and is removed from the group**.
***TODO***: It is not said what ``rate`` is supposed to do!!

| Output | Description |
|:------:|:-----------:|
| ``No user exists with this username!`` | Nonexistent username |

### Assign Member To Task
---

    assign member --task <task id> --username <username>

| Output | Description |
|:------:|:-----------:|
| ``No user exists with this username!`` | Nonexistent username |
| ``No task exists with this id!`` | Nonexistent task id |
| ``Member Assigned Successfully!`` | All goes well ... |

### Show Scoreboard
---

    show --scoreboard

The format must be:

    Rank    Username    Score
    1       user1       <user1 score>
    2       user2       <user2 score>
    3       user3       <user3 score>

| Output | Description |
|:------:|:-----------:|
| ``There is no member in this team!`` | If no member exists to show |

### Send Notifications
---

    send --notification <notification> --username <username>

| Output | Description |
|:------:|:-----------:|
| ``No user exists with this username!`` | Nonexistent username |

**OR** to the whole team:

    send --notification <notification> --team <team name>

| Output | Description |
|:------:|:-----------:|
| ``No team exists with this name!`` | Nonexistent team name |

# Admin Commands
---

### Query User Profile
---

    show profile --username <username>

| Output | Description |
|:------:|:-----------:|
| ``You are not logged in`` | If the user has not logged in (this is strange ...) |
| ``You do not have access to this section`` | If the user does no possess ``ADMIN`` role |
| ``There is no user with this username`` | Nonexistent username |

### Ban User
---

    ban user --user <username>

| Output | Description |
|:------:|:-----------:|
| ``You are not logged in`` | If the user has not logged in (this is strange ...) |
| ``You do not have access to this section`` | If the user does no possess ``ADMIN`` role |
| ``There is no user with this username`` | Nonexistent username |

### Change Role
---

    change role --user <username> --role <new role>

The role can be either ``Team Member`` or ``Team Leader``.

| Output | Description |
|:------:|:-----------:|
| ``You are not logged in`` | If the user has not logged in (this is strange ...) |
| ``You do not have access to this section`` | If the user does no possess ``ADMIN`` role |
| ``There is no user with this username`` | Nonexistent username |

### Send Notification

There are 3 scales to this commands:

    send --notificaion <notification> --all
    send --notificaion <notification> --user <username>
    send --notificaion <notification> --team <team name>

| Output | Description |
|:------:|:-----------:|
| ``You are not logged in`` | If the user has not logged in (this is strange ...) |
| ``You do not have access to this section`` | If the user does no possess ``ADMIN`` role |
| ``There is no user with this username`` OR ``There is no team with this name`` | Nonexistent username / team name |

### Show Scoreboard
---

Same as the one for ``LEADER``.

### Accept New Teams
---

    show --pendingTeams

Show teams pending for admission from earliest to oldest. If no pending teams exists, output ``There are no Teams in Pending Status!``.

To accept a team:

    accept --teams <team names>

You can add multiple, like ``accept --teams team1 team2 team3 ...``.

| Output | Description |
|:------:|:-----------:|
| ``Some teams are not in pending status! Try again`` | If any of the teams above, are not in pending status |

### Reject Team
---

    reject --teams <team names>

Same as above
