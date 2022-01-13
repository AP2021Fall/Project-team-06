package controller;

import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TasksControllerTest {
    private TasksController controller = TasksController.getController();

    /**
     * Create 6 users
     */
    public static void createUsers() {
        new User("user1", "pass1", "email@gmail.com", Role.LEADER);
        new User("user2", "pass2", "email@gmail.com", Role.MEMBER);
        new User("user3", "pass3", "email@gmail.com", Role.MEMBER);
        new User("user4", "pass4", "email@gmail.com", Role.LEADER);
        new User("user5", "pass5", "email@gmail.com", Role.MEMBER);
        new User("user6", "pass6", "email@gmail.com", Role.MEMBER);
    }

    /**
     * Create 2 teams, 3 members each
     * user1 is leader of team1
     * user4 is leader of team2
     */
    public static void createTeams() {
        Team team1 = new Team("team1", User.getUserByUsername("user1"));
        Team team2 = new Team("team2", User.getUserByUsername("user4"));

        team1.addMember(User.getUserByUsername("user2"));
        team1.addMember(User.getUserByUsername("user3"));
        team2.addMember(User.getUserByUsername("user5"));
        team2.addMember(User.getUserByUsername("user6"));
    }

    /**
     * Add two boards to each team
     */
    public static void createBoards() {
        Team team1 = Team.getTeamByName("team1");
        Team team2 = Team.getTeamByName("team2");

        team1.addBoard("board1");
        team1.addBoard("board2");
        team2.addBoard("board1");
        team2.addBoard("board2");
    }

    /**
     * Add a 'To Do' and 'Doing' category to each board
     */
    public static void createCats() {
        Board.getBoardByName("team1", "board1").addCategory("To Do");
        Board.getBoardByName("team1", "board1").addCategory("Doing");

        Board.getBoardByName("team1", "board2").addCategory("To Do");
        Board.getBoardByName("team1", "board2").addCategory("Doing");

        Board.getBoardByName("team2", "board1").addCategory("To Do");
        Board.getBoardByName("team2", "board1").addCategory("Doing");

        Board.getBoardByName("team2", "board2").addCategory("To Do");
        Board.getBoardByName("team2", "board2").addCategory("Doing");
    }

    /**
     * Add two tasks to each board
     */
    public static void createTasks() {
        Board team1Board1 = Board.getBoardByName("team1", "board1");
        Board team1Board2 = Board.getBoardByName("team1", "board2");
        Board team2Board1 = Board.getBoardByName("team2", "board1");
        Board team2Board2 = Board.getBoardByName("team2", "board2");

        assert team1Board1 != null;
        assert team1Board2 != null;
        assert team2Board1 != null;
        assert team2Board2 != null;

        team1Board1.addTask(new Task("task1 for team1", LocalDateTime.now(), LocalDateTime.now().plusDays(1), "team1"));
        team1Board1.addTask(new Task("task2 for team1", LocalDateTime.now(), LocalDateTime.now().plusDays(2), "team1"));
        team1Board2.addTask(new Task("task3 for team1", LocalDateTime.now(), LocalDateTime.now().plusDays(3), "team1"));
        team1Board2.addTask(new Task("task4 for team1", LocalDateTime.now(), LocalDateTime.now().plusDays(4), "team1"));

        team2Board1.addTask(new Task("task1 for team2", LocalDateTime.now(), LocalDateTime.now().plusDays(5), "team2"));
        team2Board1.addTask(new Task("task2 for team2", LocalDateTime.now(), LocalDateTime.now().plusDays(6), "team2"));
        team2Board2.addTask(new Task("task3 for team2", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "team2"));
        team2Board2.addTask(new Task("task4 for team2", LocalDateTime.now(), LocalDateTime.now().plusDays(8), "team2"));
    }

    /**
     * set task categories, one as 'To Do' and one as 'Doing'
     */
    public static void setInitialCategoriesForTasks() {
        Board team1Board1 = Board.getBoardByName("team1", "board1");
        Board team1Board2 = Board.getBoardByName("team1", "board2");
        Board team2Board1 = Board.getBoardByName("team2", "board1");
        Board team2Board2 = Board.getBoardByName("team2", "board2");

        assert team1Board1 != null;
        assert team1Board2 != null;
        assert team2Board1 != null;
        assert team2Board2 != null;

        team1Board1.setTaskCategory("To Do", "task1 for team1");
        team1Board1.setTaskCategory("Doing", "task2 for team1");
        team1Board2.setTaskCategory("To Do", "task3 for team1");
        team1Board2.setTaskCategory("Doing", "task4 for team1");

        team2Board1.setTaskCategory("To Do", "task1 for team2");
        team2Board1.setTaskCategory("Doing", "task2 for team2");
        team2Board2.setTaskCategory("To Do", "task3 for team2");
        team2Board2.setTaskCategory("Doing", "task4 for team2");
    }

    /**
     * assign users to tasks
     */
    public static void assignTasks() {
        Board team1Board1 = Board.getBoardByName("team1", "board1");
        Board team1Board2 = Board.getBoardByName("team1", "board2");
        Board team2Board1 = Board.getBoardByName("team2", "board1");
        Board team2Board2 = Board.getBoardByName("team2", "board2");

        assert team1Board1 != null;
        assert team1Board2 != null;
        assert team2Board1 != null;
        assert team2Board2 != null;

        team1Board1.assignUserToTask("user2", 0);
        team1Board1.assignUserToTask("user3", 0);
        team1Board1.assignUserToTask("user2", 1);
        team1Board2.assignUserToTask("user2", 2);
        team1Board2.assignUserToTask("user3", 3);


        team2Board1.assignUserToTask("user5", 4);
        team2Board1.assignUserToTask("user6", 5);
        team2Board1.assignUserToTask("user6", 6);
        team2Board2.assignUserToTask("user5", 7);
    }

    /**
     * set task priorities
     */
    public static void setPriorities() {
        Task.getTaskById(0).setPriority(Priority.LOWEST);
        Task.getTaskById(1).setPriority(Priority.LOW);
        Task.getTaskById(2).setPriority(Priority.HIGH);
        Task.getTaskById(3).setPriority(Priority.HIGHEST);

        Task.getTaskById(7).setPriority(Priority.LOWEST);
        Task.getTaskById(6).setPriority(Priority.LOW);
        Task.getTaskById(5).setPriority(Priority.HIGH);
        Task.getTaskById(4).setPriority(Priority.HIGHEST);
    }

    @BeforeEach
    public void setup() {
        createUsers();
        createTeams();
        createBoards();
        createCats();
        createTasks();
        setInitialCategoriesForTasks();
        assignTasks();
        setPriorities();
    }

    @AfterEach
    public void tearDown() {
        Task.clearAll();
        Board.clearAll();
        Team.clearAll();
        User.clearAll();
    }

    @Test
    public void initMembersTest() {
        assertTrue(Team.getTeamByName("team1").isMember("user2"));
        assertTrue(Team.getTeamByName("team1").isMember("user3"));
        assertTrue(Team.getTeamByName("team2").isMember("user5"));
        assertTrue(Team.getTeamByName("team2").isMember("user6"));
    }

    @Test
    public void initTasksTest() {
        Board team1Board1 = Board.getBoardByName("team1", "board1");
        Board team1Board2 = Board.getBoardByName("team1", "board2");
        Board team2Board1 = Board.getBoardByName("team2", "board1");
        Board team2Board2 = Board.getBoardByName("team2", "board2");

        assertNotNull(team1Board1.getTaskByTitle("task1 for team1"));
        assertNotNull(team1Board1.getTaskByTitle("task2 for team1"));
        assertNull(team1Board1.getTaskByTitle("task3 for team1"));
        assertNotNull(team1Board2.getTaskByTitle("task4 for team1"));
        assertNotNull(team2Board1.getTaskByTitle("task1 for team2"));
        assertNotNull(team2Board1.getTaskByTitle("task2 for team2"));
        assertNull(team2Board1.getTaskByTitle("task3 for team2"));
        assertNotNull(team2Board2.getTaskByTitle("task4 for team2"));
    }

    @Test
    public void initCatTest() {
        Board team1Board1 = Board.getBoardByName("team1", "board1");
        assertEquals("To Do", team1Board1.getTaskByTitle("task1 for team1").getCategory());
        assertEquals("Doing", team1Board1.getTaskByTitle("task2 for team1").getCategory());
    }

    @Test
    public void changeTaskTitleTest() {
        ControllerResult result1 = controller.changeTaskTitle("user2", 1, "new title for task1 for team1");
        ControllerResult result2 = controller.changeTaskTitle("user1", 10, "new title for task1 for team1");
        ControllerResult result3 = controller.changeTaskTitle("user1", 1, "new title for task1 for team1");

        assertEquals("You Don't Have Access To Do This Action!", result1.message);
        assertEquals("No task exists with this id!", result2.message);
        assertEquals("Title updated successfully", result3.message);
    }

    @Test
    public void changeDescriptionTest() {
        ControllerResult result1 = controller.changeDescription("user4", 11, "new description");
        ControllerResult result2 = controller.changeDescription("user4", 4, "new description");

        assertEquals("No task exists with this id!", result1.message);
        assertEquals("Description updated successfully", result2.message);
    }

    @Test
    public void changePriorityTest() {
        ControllerResult result1 =controller.changePriority("user1", 0, "LOW");
        ControllerResult result2 =controller.changePriority("user2", 0, "LOW");

        assertEquals("Priority updated successfully!", result1.message);
        assertEquals("You Don't Have Access To Do This Action!", result2.message);
    }

    @Test
    public void changeDeadlineTest() {
        System.out.println(Task.getTaskById(1).getDeadline());
        ControllerResult result1 = controller.changeDeadline("user1", 1, "2022-00-16");
        ControllerResult result2 = controller.changeDeadline("user1", 1, "2021-01-15");
        ControllerResult result3 = controller.changeDeadline("user2", 1, "2022-01-20");
        ControllerResult result4 = controller.changeDeadline("user1", 1, "2022-01-20");

        assertEquals("New deadline is invalid!", result1.message);
        assertEquals("New deadline is invalid!", result2.message);
        assertEquals("You Don't Have Access To Do This Action!", result3.message);
        assertEquals("Deadline updated successfully!", result4.message);
    }

    @Test
    public void removeOrAssignUserTest() {
        System.out.println(Board.getBoardByName("team1", "board1").showBoardPipeline());
        ControllerResult result1 = controller.removeAssignedUser("user2", 1, "aragif");
        ControllerResult result2 = controller.removeAssignedUser("user1", 1, "aragif");

        ControllerResult result3 = controller.removeAssignedUser("user1", 1, "user2");
        assertEquals(false, Task.getTaskById(1).isAssignedToTask(User.getUserByUsername("user2")));
        System.out.println(Board.getBoardByName("team1", "board1").showBoardPipeline());

        ControllerResult result4 = controller.assignUser("user1", 1, "user2");
        assertEquals(true, Task.getTaskById(1).isAssignedToTask(User.getUserByUsername("user2")));

        assertEquals("You Don't Have Access To Do This Action!", result1.message);
        assertEquals("There is not any user with this username aragif!", result2.message);
        assertEquals("User user2 removed successfully!", result3.message);
        assertEquals("User user2 added successfully!", result4.message);
    }
}