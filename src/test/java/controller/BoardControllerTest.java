package controller;

import jira.controller.BoardController;
import jira.controller.ControllerResult;
import jira.model.Board;
import jira.model.Task;
import jira.model.Team;
import jira.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BoardControllerTest extends Initializer {
    private BoardController controller = BoardController.getController();

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
    public void createAndRemoveStageOneControllerTest() {
        ControllerResult result1 = controller.createStageOneBoard("user2", "team1", "board3");
        ControllerResult result2 = controller.createStageOneBoard("user1", "team1", "board1");
        ControllerResult result3 = controller.createStageOneBoard("user1", "team1", "board3");

        assertEquals("You Don't Have Access To Do This Action!", result1.message);
        assertEquals("There is already a board with this name", result2.message);
        assertEquals("Stage 1 board created successfully!", result3.message);

        assertTrue(Board.boardExists("team1", "board3"));

        ControllerResult result4 = controller.removeBoard("user1", "team1", "board4");
        ControllerResult result5 = controller.removeBoard("user1", "team1", "board2");

        assertEquals("There is no board with this name", result4.message);
        assertEquals("Board removed successfully!", result5.message);

        assertFalse(Board.boardExists("team1", "board2"));
    }

    @Test
    public void addCategoryToBoardTest() {
        ControllerResult result1 = controller.addCategoryToBoard("user4", "team2", "aragif", "To Do");
        ControllerResult result2 = controller.addCategoryToBoard("user4", "team2", "board2", "To Do");
        ControllerResult result3 = controller.addCategoryToBoard("user4", "team2", "board2", "Review");

        assertEquals("There is no board with this name", result1.message);
        assertEquals("The name is already taken for a category!", result2.message);
        assertEquals("Category successfully added", result3.message);

        System.out.println(Board.getBoardByName("team2", "board2").showBoardPipeline());
    }

    @Test
    public void moveOrCreateInColumnTest() {
        System.out.println("Switch category test \n");
        System.out.println("Before:\n");
        System.out.println(Board.getBoardByName("team2", "board1").showBoardPipeline());

        ControllerResult result1 = controller.moveOrCreateCategoryInColumn(
                "user1", "team2", "aragif", "Doing", 10);
        ControllerResult result2 = controller.moveOrCreateCategoryInColumn(
                "user1", "team2", "board1", "Doing", 10);
        ControllerResult result3 = controller.moveOrCreateCategoryInColumn(
                "user1", "team2", "board1", "Doing", 0);

        assertEquals("There is no board with this name", result1.message);
        assertEquals("wrong column!", result2.message);
        assertEquals("Done", result3.message);

        System.out.println("After:\n");
        System.out.println(Board.getBoardByName("team2", "board1").showBoardPipeline());

        controller.moveOrCreateCategoryInColumn(
                "user1", "team2", "board2", "review", 2);
        System.out.println(Board.getBoardByName("team2", "board2").showBoardPipeline());
        controller.moveOrCreateCategoryInColumn(
                "user1", "team1", "board2", "review", 1);
        System.out.println(Board.getBoardByName("team1", "board2").showBoardPipeline());
    }

    @Test
    public void finalizeBoardTest() {
        ControllerResult result1 = controller.finalizeBoard("user1", "team1", "aragif");
        controller.createStageOneBoard("user1", "team1", "board3");
        ControllerResult result2 = controller.finalizeBoard("user1", "team1", "board3");
        controller.addCategoryToBoard("user1", "team1", "board3", "cat 1");
        controller.addCategoryToBoard("user1", "team1", "board3", "cat 2");
        ControllerResult result3 = controller.finalizeBoard("user1", "team1", "board3");

        assertEquals("There is no board with this name", result1.message);
        assertEquals("Please make a category first", result2.message);
        assertEquals("Board finalized", result3.message);

        assertTrue(Board.boardExists("team1", "board3"));
        System.out.println(Board.getBoardByName("team1", "board3").showBoardPipeline());
    }

    @Test
    public void doNextOnTaskTest() {
        Board board = Board.getBoardByName("team1", "board1");
        System.out.println("BEFORE:\n");
        System.out.println(board.showBoardPipeline());

        ControllerResult result1 = controller.doNextOnTask(
                "user1", "team1", "board10", "task10 for team1");
        ControllerResult result2 = controller.doNextOnTask(
                "user1", "team1", "board1", "task10 for team1");
        ControllerResult result3 = controller.doNextOnTask(
                "user2", "team1", "board1", "task1 for team1");

        assertEquals("There is no board with this name", result1.message);
        assertEquals("Invalid task!", result2.message);
        assertEquals("Done", result3.message);

        System.out.println("USER 2 is DONE\n");
        System.out.println(board.showBoardPipeline());

        assertEquals(25, board.getTaskPercentDone(Task.getTaskById(0)));

        controller.doNextOnTask(
                "user1", "team1", "board1", "task1 for team1");

        assertEquals(50, board.getTaskPercentDone(Task.getTaskById(0)));

        System.out.println("LEADER APPROVED\n");
        System.out.println(board.showBoardPipeline());

        controller.doNextOnTask(
                "user1", "team1", "board1", "task1 for team1");

        System.out.println("TASK IS DONE\n");
        System.out.println(board.showBoardPipeline());

        assertEquals(10, User.getUserByUsername("user2").getScore());
        assertEquals(10, User.getUserByUsername("user3").getScore());
    }

    @Test
    public void showDoneOrFailedTasksTest() {
        System.out.println(controller.showDoneOrFailedTasks("team1", "board1", false).message);
        System.out.println(controller.showDoneOrFailedTasks("team1", "board1", true).message);


        controller.doNextOnTask(
                "user1", "team1", "board1", "task1 for team1");
        controller.doNextOnTask(
                "user1", "team1", "board1", "task1 for team1");
        System.out.println(controller.showDoneOrFailedTasks("team1", "board1", true).message);
        controller.doNextOnTask(
                "user1", "team1", "board1", "task2 for team1");
        System.out.println(controller.showDoneOrFailedTasks("team1", "board1", true).message);
    }

    @Test
    public void testFails() {
        Board.setNow(LocalDateTime.now().plusDays(100));

        System.out.println(controller.showDoneOrFailedTasks("team1", "board1", false).message);
        System.out.println(controller.showDoneOrFailedTasks("team1", "board2", false).message);
        System.out.println(controller.showDoneOrFailedTasks("team2", "board1", false).message);
        System.out.println(controller.showDoneOrFailedTasks("team2", "board2", false).message);

        assertEquals(-15, User.getUserByUsername("user2").getScore());
        assertEquals(-10, User.getUserByUsername("user3").getScore());
        assertEquals(-10, User.getUserByUsername("user5").getScore());
        assertEquals(-10, User.getUserByUsername("user6").getScore());
    }

    @Test
    public void testRestart() {
        Board.setNow(LocalDateTime.now().plusDays(3).plusHours(12));

        System.out.println("BEFORE\n");
        System.out.println(controller.showDoneOrFailedTasks("team1", "board1", false).message);
        System.out.println(controller.showDoneOrFailedTasks("team1", "board2", false).message);

        ControllerResult result1 = controller.restartTask(
                "user1", "team1", "board2", "2022-01-15",
                "task4 for team1", null, null);
        ControllerResult result2 = controller.restartTask(
                "user1", "team1", "board1", "2022-1-15",
                "task1 for team1", null, null);
        ControllerResult result3 = controller.restartTask(
                "user1", "team1", "board1", "2021-01-15",
                "task1 for team1", null, null);
        ControllerResult result4 = controller.restartTask(
                "user1", "team1", "board1", "2022-01-15",
                "task1 for team1", null, null);
        ControllerResult result5 = controller.restartTask(
                "user1", "team1", "board1", "2022-01-15",
                "task1 for team1", null, "review");
        ControllerResult result6 = controller.restartTask(
                "user1", "team1", "board1", "2022-01-15",
                "task1 for team1", "aragif", "Doing");
        ControllerResult result7 = controller.restartTask(
                "user1", "team1", "board1", "2022-01-15",
                "task2 for team1", null, "Doing");
        ControllerResult result8 = controller.restartTask(
                "user1", "team1", "board2", "2022-01-15",
                "task3 for team1", "user2", "Doing");

        assertEquals("This task is not in failed section", result1.message);
        assertEquals("Invalid deadline", result2.message);
        assertEquals("Invalid deadline", result3.message);
        assertEquals("Done", result4.message);
        assertEquals("Invalid category", result5.message);
        assertEquals("Invalid teammember", result6.message);
        assertEquals("Done", result7.message);
        assertEquals("Done", result8.message);

        System.out.println("AFTER\n");
        System.out.println(controller.showDoneOrFailedTasks("team1", "board1", false).message);
        System.out.println(controller.showDoneOrFailedTasks("team1", "board2", false).message);

        System.out.println(Board.getBoardByName("team1", "board1").showBoardPipeline());
        System.out.println(Board.getBoardByName("team1", "board2").showBoardPipeline());
    }
}