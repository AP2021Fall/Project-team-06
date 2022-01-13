package controller;

import model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TasksControllerTest extends Initializer {
    private TasksController controller = TasksController.getController();

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

    @AfterAll
    public static void cleanup() {
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