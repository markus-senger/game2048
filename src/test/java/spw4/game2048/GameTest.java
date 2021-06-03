package spw4.game2048;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {
    private Game gb;

    @BeforeEach
    void InitGame() {
        gb = new Game();
    }

    @Test
    void InitSizeGameBoard() {
        assertEquals(4, gb.getSize());
    }

    @Test
    void ShowEmptyGameBoard() {
        StringBuilder expected = new StringBuilder();
        for(int row = 0; row < gb.getSize(); row++) {
            for(int col = 0; col < gb.getSize(); col++) {
                expected.append("    .");
            }
            expected.append("\n");
        }
        assertEquals(String.valueOf(expected), gb.toString());
    }

    @Test
    void InitializedGameBoardCorrectlyWith2Numbers() {
        gb.initialize();
        int isNumberCount = 0;
        for(int row = 0; row < gb.getSize(); row++) {
            for(int col = 0; col < gb.getSize(); col++) {
                if(gb.getGameBoard()[row][col] != 0) {
                    isNumberCount++;
                }
            }
        }
        assertEquals(2, isNumberCount);
    }

    @Test
    void InitializedGameBoardCorrectlyFocusOnProbability() {
        int occurrencesOfFour = 0;
        int occurrencesOfTwo = 0;
        for(int i = 0; i < 1000; i++) {
            gb.initialize();
            for(int row = 0; row < gb.getSize(); row++) {
                for(int col = 0; col < gb.getSize(); col++) {
                    if(gb.getGameBoard()[row][col] == 2) {
                        occurrencesOfTwo++;
                    } else if (gb.getGameBoard()[row][col] == 4) {
                        occurrencesOfFour++;
                    }
                }
            }
            gb.reset();
        }
        assertEquals(200, occurrencesOfFour, 50);
        assertEquals(1800, occurrencesOfTwo, 50);
        assertEquals(2000, occurrencesOfFour + occurrencesOfTwo);
    }

    @Nested
    class OpeningMoveTestsWithMockitoWithoutCollisionAllNumbersHasToMove {
        // 0 0 0 0
        // 0 4 0 0
        // 0 0 2 0
        // 0 0 0 0

        @BeforeEach
        void InitGame() {
            var rand = mock(Random.class);
            gb = new Game(rand);
            when(gb.createRandomNumber(anyInt()))
                    .thenReturn(5)  // Index 5 of Array with all empty positions (row: 1 | col: 1)
                    .thenReturn(0)  // 0 for Number 4
                    .thenReturn(9)  // Index 9 of Array with all empty positions (row: 2 | col: 2)
                    .thenReturn(5); // 0 for Number 2

            gb.initialize();
        }

        @Test
        void MoveLeftWithoutCollision() {
            gb.moveLeft();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(1, 0)),
                    () -> assertEquals(2, gb.getNumberOnPosition(2, 0)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }

        @Test
        void MoveRightWithoutCollision() {
            gb.moveRight();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(1, 3)),
                    () -> assertEquals(2, gb.getNumberOnPosition(2, 3)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }

        @Test
        void MoveUpWithoutCollision() {
            gb.moveUp();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(0, 1)),
                    () -> assertEquals(2, gb.getNumberOnPosition(0, 2)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }

        @Test
        void MoveDownWithoutCollision() {
            gb.moveDown();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(3, 1)),
                    () -> assertEquals(2, gb.getNumberOnPosition(3, 2)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }
    }

    @Nested
    class OpeningMoveTestsWithMockitoWithoutCollisionOnlyOneNumberHasToMove {
        // 2 0 0 0
        // 0 2 0 0
        // 0 0 0 0
        // 0 0 0 0

        @BeforeEach
        void InitGame() {
            var rand = mock(Random.class);
            gb = new Game(rand);
            when(gb.createRandomNumber(anyInt()))
                    .thenReturn(0)   // Index 0 of Array with all empty positions (row: 0 | col: 0)
                    .thenReturn(5)   // 5 for Number 2
                    .thenReturn(4)   // Index 4 of Array with all empty positions (row: 1 | col: 1)
                    .thenReturn(5);  // 5 for Number 2

            gb.initialize();
        }

        @Test
        void MoveLeftWithoutCollision() {
            gb.moveLeft();
            assertAll(
                    () -> assertEquals(2, gb.getNumberOnPosition(0, 0)),
                    () -> assertEquals(2, gb.getNumberOnPosition(1, 0)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }

        @Test
        void MoveRightWithoutCollision() {
            gb.moveRight();
            assertAll(
                    () -> assertEquals(2, gb.getNumberOnPosition(0, 3)),
                    () -> assertEquals(2, gb.getNumberOnPosition(1, 3)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }

        @Test
        void MoveUpWithoutCollision() {
            gb.moveUp();
            assertAll(
                    () -> assertEquals(2, gb.getNumberOnPosition(0, 0)),
                    () -> assertEquals(2, gb.getNumberOnPosition(0, 1)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }

        @Test
        void MoveDownWithoutCollision() {
            gb.moveDown();
            assertAll(
                    () -> assertEquals(2, gb.getNumberOnPosition(3, 0)),
                    () -> assertEquals(2, gb.getNumberOnPosition(3, 1)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }
    }

    @Nested
    class OpeningMoveTestsWithMockitoWithCollisionOnTheHorizontalAxis {
        // 0 0 0 0
        // 0 2 2 0
        // 0 0 0 0
        // 0 0 0 0

        @BeforeEach
        void InitGame() {
            var rand = mock(Random.class);
            gb = new Game(rand);
            when(gb.createRandomNumber(anyInt()))
                    .thenReturn(5)   // Index 5 of Array with all empty positions (row: 1 | col: 1)
                    .thenReturn(5)   // 5 for Number 2
                    .thenReturn(5)   // Index 5 of Array with all empty positions (row: 1 | col: 2)
                    .thenReturn(5);  // 5 for Number 2

            gb.initialize();
        }

        @Test
        void MoveLeftWithCollision() {
            gb.moveLeft();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(1, 0)),
                    () -> assertEquals(2, gb.getNumbersCount())
            );
        }

        @Test
        void MoveRightWithCollision() {
            gb.moveRight();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(1, 3)),
                    () -> assertEquals(2, gb.getNumbersCount())
            );
        }

        @Test
        void MoveUpWithoutCollision() {
            gb.moveUp();
            assertAll(
                    () -> assertEquals(2, gb.getNumberOnPosition(0, 1)),
                    () -> assertEquals(2, gb.getNumberOnPosition(0, 2)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }

        @Test
        void MoveDownWithoutCollision() {
            gb.moveDown();
            assertAll(
                    () -> assertEquals(2, gb.getNumberOnPosition(3, 1)),
                    () -> assertEquals(2, gb.getNumberOnPosition(3, 2)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }
    }

    @Nested
    class OpeningMoveTestsWithMockitoWithCollisionOnTheVerticalAxis {
        // 0 0 0 0
        // 0 2 0 0
        // 0 2 0 0
        // 0 0 0 0

        @BeforeEach
        void InitGame() {
            var rand = mock(Random.class);
            gb = new Game(rand);
            when(gb.createRandomNumber(anyInt()))
                    .thenReturn(5)   // Index 5 of Array with all empty positions (row: 1 | col: 1)
                    .thenReturn(5)   // 5 for Number 2
                    .thenReturn(8)   // Index 8 of Array with all empty positions (row: 2 | col: 1)
                    .thenReturn(5);  // 5 for Number 2

            gb.initialize();
        }

        @Test
        void MoveLeftWithOutCollision() {
            gb.moveLeft();
            assertAll(
                    () -> assertEquals(2, gb.getNumberOnPosition(1, 0)),
                    () -> assertEquals(2, gb.getNumberOnPosition(2, 0)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }

        @Test
        void MoveRightWithOutCollision() {
            gb.moveRight();
            assertAll(
                    () -> assertEquals(2, gb.getNumberOnPosition(1, 3)),
                    () -> assertEquals(2, gb.getNumberOnPosition(2, 3)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }

        @Test
        void MoveUpWithCollision() {
            gb.moveUp();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(0, 1)),
                    () -> assertEquals(2, gb.getNumbersCount())
            );
        }

        @Test
        void MoveDownWithCollision() {
            gb.moveDown();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(3, 1)),
                    () -> assertEquals(2, gb.getNumbersCount())
            );
        }
    }

    @Nested
    class OpeningMoveTestsWithMockitoWithCollisionOnTheHorizontalAxisWithDifferentNumbers {
        // 0 0 0 0
        // 0 2 4 0
        // 0 0 0 0
        // 0 0 0 0

        @BeforeEach
        void InitGame() {
            var rand = mock(Random.class);
            gb = new Game(rand);
            when(gb.createRandomNumber(anyInt()))
                    .thenReturn(5)   // Index 5 of Array with all empty positions (row: 1 | col: 1)
                    .thenReturn(5)   // 5 for Number 2
                    .thenReturn(5)   // Index 5 of Array with all empty positions (row: 1 | col: 2)
                    .thenReturn(0);  // 0 for Number 4

            gb.initialize();
        }

        @Test
        void MoveLeftWithCollision() {
            gb.moveLeft();
            assertAll(
                    () -> assertEquals(2, gb.getNumberOnPosition(1, 0)),
                    () -> assertEquals(4, gb.getNumberOnPosition(1, 1)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }

        @Test
        void MoveRightWithCollision() {
            gb.moveRight();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(1, 3)),
                    () -> assertEquals(2, gb.getNumberOnPosition(1, 2)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }

        @Test
        void MoveUpWithOutCollision() {
            gb.moveUp();
            assertAll(
                    () -> assertEquals(2, gb.getNumberOnPosition(0, 1)),
                    () -> assertEquals(4, gb.getNumberOnPosition(0, 2)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }

        @Test
        void MoveDownWithOutCollision() {
            gb.moveDown();
            assertAll(
                    () -> assertEquals(2, gb.getNumberOnPosition(3, 1)),
                    () -> assertEquals(4, gb.getNumberOnPosition(3, 2)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }
    }

    @Nested
    class OpeningMoveTestsWithMockitoWithCollisionOnTheVerticalAxisWithDifferentNumbers {
        // 0 0 0 0
        // 0 2 0 0
        // 0 4 0 0
        // 0 0 0 0

        @BeforeEach
        void InitGame() {
            var rand = mock(Random.class);
            gb = new Game(rand);
            when(gb.createRandomNumber(anyInt()))
                    .thenReturn(5)   // Index 5 of Array with all empty positions (row: 1 | col: 1)
                    .thenReturn(5)   // 5 for Number 2
                    .thenReturn(8)   // Index 8 of Array with all empty positions (row: 2 | col: 1)
                    .thenReturn(0);  // 0 for Number 4

            gb.initialize();
        }

        @Test
        void MoveLeftWithOutCollision() {
            gb.moveLeft();
            assertAll(
                    () -> assertEquals(2, gb.getNumberOnPosition(1, 0)),
                    () -> assertEquals(4, gb.getNumberOnPosition(2, 0)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }

        @Test
        void MoveRightWithOutCollision() {
            gb.moveRight();
            assertAll(
                    () -> assertEquals(2, gb.getNumberOnPosition(1, 3)),
                    () -> assertEquals(4, gb.getNumberOnPosition(2, 3)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }

        @Test
        void MoveUpWithCollision() {
            gb.moveUp();
            assertAll(
                    () -> assertEquals(2, gb.getNumberOnPosition(0, 1)),
                    () -> assertEquals(4, gb.getNumberOnPosition(1, 1)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }

        @Test
        void MoveDownWithCollision() {
            gb.moveDown();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(3, 1)),
                    () -> assertEquals(2, gb.getNumberOnPosition(2, 1)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }
    }

    @Nested
    class OpeningMoveTestsWithMockitoWithoutCollisionSpawnNewNumber {
        // 0 0 0 0
        // 0 4 0 0
        // 0 0 2 0
        // 0 0 0 0

        @BeforeEach
        void InitGame() {
            var rand = mock(Random.class);
            gb = new Game(rand);
            when(gb.createRandomNumber(anyInt()))
                    .thenReturn(5)  // Index 5 of Array with all empty positions (row: 1 | col: 1)
                    .thenReturn(0)  // 0 for Number 4
                    .thenReturn(9)  // Index 9 of Array with all empty positions (row: 3 | col: 3)
                    .thenReturn(5)  // 5 for Number 2
                    .thenReturn(0)  // 0 for Position (row: 0 | col: 0) for new Number
                    .thenReturn(0); // 0 for Number 4

            gb.initialize();
        }

        @Test
        void MoveLeftWithoutCollision() {
            gb.moveLeft();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(0, 0)),
                    () -> assertEquals(4, gb.getNumberOnPosition(1, 0)),
                    () -> assertEquals(2, gb.getNumberOnPosition(2, 0)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }

        @Test
        void MoveRightWithoutCollision() {
            gb.moveRight();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(0, 0)),
                    () -> assertEquals(4, gb.getNumberOnPosition(1, 3)),
                    () -> assertEquals(2, gb.getNumberOnPosition(2, 3)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }

        @Test
        void MoveUpWithoutCollision() {
            gb.moveUp();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(0, 0)),
                    () -> assertEquals(4, gb.getNumberOnPosition(0, 1)),
                    () -> assertEquals(2, gb.getNumberOnPosition(0, 2)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }

        @Test
        void MoveDownWithoutCollision() {
            gb.moveDown();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(0, 0)),
                    () -> assertEquals(4, gb.getNumberOnPosition(3, 1)),
                    () -> assertEquals(2, gb.getNumberOnPosition(3, 2)),
                    () -> assertEquals(3, gb.getNumbersCount())
            );
        }
    }

    @Nested
    class RepeatMoveTestsWithMockitoWithCollisionOnHorizontalAxis {
        // 0 0 0 0
        // 0 2 2 0
        // 0 0 0 0
        // 0 0 0 0

        @BeforeEach
        void InitGame() {
            var rand = mock(Random.class);
            gb = new Game(rand);
            when(gb.createRandomNumber(anyInt()))
                    .thenReturn(5)  // Index 5 of Array with all empty positions (row: 1 | col: 1)
                    .thenReturn(5)  // 5 for Number 2
                    .thenReturn(5)  // Index 5 of Array with all empty positions (row: 1 | col: 2)
                    .thenReturn(5)  // 5 for Number 2
                    .thenReturn(5)  // 5 for Position for new Number
                    .thenReturn(0); // 0 for Number 4

            gb.initialize();
        }

        @Test
        void MoveLeftWithCollision() {
            // 0 0 0 0      0 0 0 0
            // 4 4 0 0  ->  8 0 0 0
            // 0 0 0 0      0 0 0 0
            // 0 0 0 0      0 0 0 ?
            // ? -> random position for random number
            gb.moveLeft();
            gb.moveLeft();
            assertAll(
                    () -> assertEquals(8, gb.getNumberOnPosition(1, 0)),
                    () -> assertEquals(2, gb.getNumbersCount())
            );
        }

        @Test
        void MoveRightWithCollision() {
            // 0 0 0 0      0 0 0 0
            // 0 4 0 4  ->  0 0 0 8
            // 0 0 0 0      0 0 0 0
            // 0 0 0 0      0 0 0 ?
            // ? -> random position for random number
            gb.moveRight();
            gb.moveRight();
            assertAll(
                    () -> assertEquals(8, gb.getNumberOnPosition(1, 3)),
                    () -> assertEquals(2, gb.getNumbersCount())
            );
        }

        @Test
        void MoveUpWithoutCollision() {
            // 0 2 2 0      0 2 2 4
            // 0 0 0 4  ->  0 0 0 0
            // 0 0 0 0      0 0 0 0
            // 0 0 0 0      0 0 0 ?
            // ? -> random position for random number
            gb.moveUp();
            gb.moveUp();
            assertAll(
                    () -> assertEquals(2, gb.getNumberOnPosition(0, 1)),
                    () -> assertEquals(2, gb.getNumberOnPosition(0, 2)),
                    () -> assertEquals(4, gb.getNumberOnPosition(0, 3)),
                    () -> assertEquals(4, gb.getNumbersCount())
            );
        }

        @Test
        void MoveDownWithoutCollision() {
            // 0 0 0 0      0 0 0 0
            // 0 4 0 0  ->  0 0 0 0
            // 0 0 0 0      0 4 0 0
            // 0 2 2 0      0 2 2 ?
            // ? -> random position for random number
            gb.moveDown();
            gb.moveDown();
            assertAll(
                    () -> assertEquals(2, gb.getNumberOnPosition(3, 1)),
                    () -> assertEquals(2, gb.getNumberOnPosition(3, 2)),
                    () -> assertEquals(4, gb.getNumberOnPosition(2, 1)),
                    () -> assertEquals(4, gb.getNumbersCount())
            );
        }
    }

    @Nested
    class RepeatMoveTestsWithMockitoWithCollisionInCenterOfBoardHorizontal {
        // 0 0 0 0
        // 0 2 2 0
        // 0 0 0 0
        // 0 0 0 0

        @BeforeEach
        void InitGame() {
            var rand = mock(Random.class);
            gb = new Game(rand);
            when(gb.createRandomNumber(anyInt()))
                    .thenReturn(5)  // Index 5 of Array with all empty positions (row: 1 | col: 1)
                    .thenReturn(5)  // 5 for Number 2
                    .thenReturn(5)  // Index 5 of Array with all empty positions (row: 1 | col: 2)
                    .thenReturn(5)  // 5 for Number 2
                    .thenReturn(5)  // 5 for Position for new Number
                    .thenReturn(4)  // 4 for Number 2
                    .thenReturn(5)  // 5 for Position for new Number
                    .thenReturn(0); // 0 for Number 4

            gb.initialize();
        }

        @Test
        void MoveLeftWithCollision() {
            // 0 0 0 0      0 0 0 0      0 0 0 0
            // 4 0 2 0  ->  4 2 0 4  ->  4 2 4 0
            // 0 0 0 0      0 0 0 0      0 0 0 0
            // 0 0 0 0      0 0 0 0      0 0 0 ?
            // ? -> random position for random number
            gb.moveLeft();
            gb.moveLeft();
            gb.moveLeft();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(1, 0)),
                    () -> assertEquals(2, gb.getNumberOnPosition(1, 1)),
                    () -> assertEquals(4, gb.getNumberOnPosition(1, 2)),
                    () -> assertEquals(4, gb.getNumbersCount()),
                    () -> assertEquals(4, gb.getScore())
            );
        }

        @Test
        void MoveRightWithCollision() {
            // 0 0 0 0      0 0 0 0      0 0 0 0
            // 2 0 0 4  ->  0 4 2 4  ->  0 4 2 4
            // 0 0 0 0      0 0 0 0      0 0 0 0
            // 0 0 0 0      0 0 0 0      0 0 0 0
            gb.moveRight();
            gb.moveRight();
            gb.moveRight();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(1, 1)),
                    () -> assertEquals(2, gb.getNumberOnPosition(1, 2)),
                    () -> assertEquals(4, gb.getNumberOnPosition(1, 3)),
                    () -> assertEquals(3, gb.getNumbersCount()),
                    () -> assertEquals(4, gb.getScore())
            );
        }

        @Test
        void MoveUpWithoutCollision() {
            // 0 2 2 0      0 2 2 2      4 2 2 2
            // 0 0 0 2  ->  0 0 0 0  ->  0 0 0 0
            // 0 0 0 0      4 0 0 0      0 0 0 0
            // 0 0 0 0      0 0 0 0      0 0 0 ?
            // ? -> random position for random number
            gb.moveUp();
            gb.moveUp();
            gb.moveUp();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(0, 0)),
                    () -> assertEquals(2, gb.getNumberOnPosition(0, 1)),
                    () -> assertEquals(2, gb.getNumberOnPosition(0, 2)),
                    () -> assertEquals(2, gb.getNumberOnPosition(0, 3)),
                    () -> assertEquals(5, gb.getNumbersCount()),
                    () -> assertEquals(0, gb.getScore())
            );
        }

        @Test
        void MoveDownWithoutCollision() {
            // 0 0 0 0      0 0 0 0      0 0 0 0
            // 0 2 0 0  ->  0 4 0 0  ->  0 0 0 0
            // 0 0 0 0      0 0 0 0      0 0 0 0
            // 0 2 2 0      0 4 2 0      0 8 2 ?
            // ? -> random position for random number
            gb.moveDown();
            gb.moveDown();
            gb.moveDown();
            assertAll(
                    () -> assertEquals(8, gb.getNumberOnPosition(3, 1)),
                    () -> assertEquals(2, gb.getNumberOnPosition(3, 2)),
                    () -> assertEquals(3, gb.getNumbersCount()),
                    () -> assertEquals(12, gb.getScore())
            );
        }
    }

    @Nested
    class MoveTestsInAdvancedGame1 {
        @BeforeEach
        void InitGame() {
            int[][] board = {
                                {2, 2, 2, 2},
                                {2, 2, 2, 2},
                                {0, 0, 0, 0},
                                {0, 0, 0, 0}
                            };
            gb.setGameBoard(board);
        }

        @Test
        void MoveLeft() {
            gb.moveLeft();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(0, 0)),
                    () -> assertEquals(4, gb.getNumberOnPosition(0, 1)),
                    () -> assertEquals(4, gb.getNumberOnPosition(1, 0)),
                    () -> assertEquals(4, gb.getNumberOnPosition(1, 1)),
                    () -> assertEquals(5, gb.getNumbersCount()),
                    () -> assertEquals(16, gb.getScore())
            );
        }

        @Test
        void MoveRight() {
            gb.moveRight();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(0, 2)),
                    () -> assertEquals(4, gb.getNumberOnPosition(0, 3)),
                    () -> assertEquals(4, gb.getNumberOnPosition(1, 2)),
                    () -> assertEquals(4, gb.getNumberOnPosition(1, 3)),
                    () -> assertEquals(5, gb.getNumbersCount()),
                    () -> assertEquals(16, gb.getScore())
            );
        }

        @Test
        void MoveUp() {
            gb.moveUp();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(0, 0)),
                    () -> assertEquals(4, gb.getNumberOnPosition(0, 1)),
                    () -> assertEquals(4, gb.getNumberOnPosition(0, 2)),
                    () -> assertEquals(4, gb.getNumberOnPosition(0, 3)),
                    () -> assertEquals(5, gb.getNumbersCount()),
                    () -> assertEquals(16, gb.getScore())
            );
        }

        @Test
        void MoveDown() {
            gb.moveDown();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(3, 0)),
                    () -> assertEquals(4, gb.getNumberOnPosition(3, 1)),
                    () -> assertEquals(4, gb.getNumberOnPosition(3, 2)),
                    () -> assertEquals(4, gb.getNumberOnPosition(3, 3)),
                    () -> assertEquals(5, gb.getNumbersCount()),
                    () -> assertEquals(16, gb.getScore())
            );
        }
    }

    @Nested
    class MoveTestsInAdvancedGame2 {
        @BeforeEach
        void InitGame() {
            int[][] board = {
                    {2, 4, 8, 16},
                    {2, 4, 8, 16},
                    {2, 4, 8, 16},
                    {2, 4, 8, 16}
            };
            gb.setGameBoard(board);
        }

        @Test
        void MoveLeft() {
            int[][] exception = {
                    {2, 4, 8, 16},
                    {2, 4, 8, 16},
                    {2, 4, 8, 16},
                    {2, 4, 8, 16}
            };
            gb.moveLeft();
            assertTrue(Arrays.deepEquals(exception, gb.getGameBoard()));
            assertEquals(0, gb.getScore());
        }

        @Test
        void MoveRight() {
            int[][] exception = {
                    {2, 4, 8, 16},
                    {2, 4, 8, 16},
                    {2, 4, 8, 16},
                    {2, 4, 8, 16}
            };
            gb.moveRight();
            assertTrue(Arrays.deepEquals(exception, gb.getGameBoard()));
            assertEquals(0, gb.getScore());
        }

        @Test
        void MoveUp() {
            gb.moveUp();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(0, 0)),
                    () -> assertEquals(8, gb.getNumberOnPosition(0, 1)),
                    () -> assertEquals(16, gb.getNumberOnPosition(0, 2)),
                    () -> assertEquals(32, gb.getNumberOnPosition(0, 3)),
                    () -> assertEquals(4, gb.getNumberOnPosition(1, 0)),
                    () -> assertEquals(8, gb.getNumberOnPosition(1, 1)),
                    () -> assertEquals(16, gb.getNumberOnPosition(1, 2)),
                    () -> assertEquals(32, gb.getNumberOnPosition(1, 3)),
                    () -> assertEquals(9, gb.getNumbersCount()),
                    () -> assertEquals(120, gb.getScore())
            );
        }

        @Test
        void MoveDown() {
            gb.moveDown();
            assertAll(
                    () -> assertEquals(4, gb.getNumberOnPosition(2, 0)),
                    () -> assertEquals(8, gb.getNumberOnPosition(2, 1)),
                    () -> assertEquals(16, gb.getNumberOnPosition(2, 2)),
                    () -> assertEquals(32, gb.getNumberOnPosition(2, 3)),
                    () -> assertEquals(4, gb.getNumberOnPosition(3, 0)),
                    () -> assertEquals(8, gb.getNumberOnPosition(3, 1)),
                    () -> assertEquals(16, gb.getNumberOnPosition(3, 2)),
                    () -> assertEquals(32, gb.getNumberOnPosition(3, 3)),
                    () -> assertEquals(9, gb.getNumbersCount()),
                    () -> assertEquals(120, gb.getScore())
            );
        }
    }

    @Nested
    class MoveTestsInAdvancedGame2DifferentMovesInARow {
        @BeforeEach
        void InitGame() {
            int[][] board = {
                    {2, 2, 4, 4},
                    {2, 2, 4, 4},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0}
            };
            gb.setGameBoard(board);
        }

        @Test
        void MoveLeftUp() {
            gb.moveLeft();
            gb.moveUp();
            assertAll(
                    () -> assertEquals(8, gb.getNumberOnPosition(0, 0)),
                    () -> assertEquals(16, gb.getNumberOnPosition(0, 1)),
                    () -> assertEquals(4, gb.getNumbersCount()),
                    () -> assertEquals(48, gb.getScore())
            );
        }

        @Test
        void MoveRightUp() {
            gb.moveRight();
            gb.moveUp();
            assertAll(
                    () -> assertEquals(8, gb.getNumberOnPosition(0, 2)),
                    () -> assertEquals(16, gb.getNumberOnPosition(0, 3)),
                    () -> assertEquals(4, gb.getNumbersCount()),
                    () -> assertEquals(48, gb.getScore())
            );
        }

        @Test
        void MoveUpRight() {
            gb.moveUp();
            gb.moveRight();
            assertAll(
                    () -> assertEquals(8, gb.getNumberOnPosition(0, 2)),
                    () -> assertEquals(16, gb.getNumberOnPosition(0, 3)),
                    () -> assertEquals(4, gb.getNumbersCount()),
                    () -> assertEquals(48, gb.getScore())
            );
        }

        @Test
        void MoveUpLeft() {
            gb.moveUp();
            gb.moveLeft();
            assertAll(
                    () -> assertEquals(8, gb.getNumberOnPosition(0, 0)),
                    () -> assertEquals(16, gb.getNumberOnPosition(0, 1)),
                    () -> assertEquals(4, gb.getNumbersCount()),
                    () -> assertEquals(48, gb.getScore())
            );
        }
    }

    @Nested
    class MoveTestsInAdvancedGame2DifferentMovesInARow2 {
        @BeforeEach
        void InitGame() {
            int[][] board = {
                    { 2,  2,  4,  4},
                    { 2,  2,  4,  4},
                    {32, 32, 32, 32},
                    {64, 64, 64, 64}
            };
            gb.setGameBoard(board);
        }

        @Test
        void MoveLeftDown() {
            gb.moveLeft();
            gb.moveDown();
            assertAll(
                    () -> assertEquals(8, gb.getNumberOnPosition(1, 0)),
                    () -> assertEquals(16, gb.getNumberOnPosition(1, 1)),
                    () -> assertEquals(64, gb.getNumberOnPosition(2, 0)),
                    () -> assertEquals(64, gb.getNumberOnPosition(2, 1)),
                    () -> assertEquals(128, gb.getNumberOnPosition(3, 0)),
                    () -> assertEquals(128, gb.getNumberOnPosition(3, 1)),
                    () -> assertEquals(8, gb.getNumbersCount()),
                    () -> assertEquals(432, gb.getScore())
            );
        }

        @Test
        void MoveRightDown() {
            gb.moveRight();
            gb.moveDown();
            assertAll(
                    () -> assertEquals(8, gb.getNumberOnPosition(1, 2)),
                    () -> assertEquals(16, gb.getNumberOnPosition(1, 3)),
                    () -> assertEquals(64, gb.getNumberOnPosition(2, 2)),
                    () -> assertEquals(64, gb.getNumberOnPosition(2, 3)),
                    () -> assertEquals(128, gb.getNumberOnPosition(3, 2)),
                    () -> assertEquals(128, gb.getNumberOnPosition(3, 3)),
                    () -> assertEquals(8, gb.getNumbersCount()),
                    () -> assertEquals(432, gb.getScore())
            );
        }

        @Test
        void MoveDownRight() {
            gb.moveDown();
            gb.moveRight();
            assertAll(
                    () -> assertEquals(8, gb.getNumberOnPosition(1, 2)),
                    () -> assertEquals(16, gb.getNumberOnPosition(1, 3)),
                    () -> assertEquals(64, gb.getNumberOnPosition(2, 2)),
                    () -> assertEquals(64, gb.getNumberOnPosition(2, 3)),
                    () -> assertEquals(128, gb.getNumberOnPosition(3, 2)),
                    () -> assertEquals(128, gb.getNumberOnPosition(3, 3)),
                    () -> assertEquals(8, gb.getNumbersCount()),
                    () -> assertEquals(432, gb.getScore())
            );
        }

        @Test
        void MoveUpLeft() {
            gb.moveUp();
            gb.moveLeft();
            assertAll(
                    () -> assertEquals(8, gb.getNumberOnPosition(0, 0)),
                    () -> assertEquals(16, gb.getNumberOnPosition(0, 1)),
                    () -> assertEquals(64, gb.getNumberOnPosition(1, 0)),
                    () -> assertEquals(64, gb.getNumberOnPosition(1, 1)),
                    () -> assertEquals(128, gb.getNumberOnPosition(2, 0)),
                    () -> assertEquals(128, gb.getNumberOnPosition(2, 1)),
                    () -> assertEquals(8, gb.getNumbersCount()),
                    () -> assertEquals(432, gb.getScore())
            );
        }
    }

    @Nested
    class MoveTestsInFinalGame1 {
        @BeforeEach
        void InitGame() {
            int[][] board = {
                    {2, 4, 2,    4},
                    {4, 2, 4,    2},
                    {2, 4, 2, 1024},
                    {4, 2, 0, 1024}
            };
            gb.setGameBoard(board);
        }

        @Test
        void MoveLeft() {
            gb.moveLeft();
            assertAll(
                    () -> assertTrue(gb.isOver()),
                    () -> assertFalse(gb.isWon())
            );
        }

        @Test
        void MoveRight() {
            gb.moveRight();
            assertAll(
                    () -> assertFalse(gb.isOver()),
                    () -> assertFalse(gb.isWon())
            );
        }

        @Test
        void MoveDown() {
            gb.moveDown();
            assertAll(
                    () -> assertTrue(gb.isOver()),
                    () -> assertTrue(gb.isWon())
            );
        }

        @Test
        void MoveUp() {
            gb.moveUp();
            assertAll(
                    () -> assertTrue(gb.isOver()),
                    () -> assertTrue(gb.isWon())
            );
        }
    }

    @Nested
    class MoveTestsInFinalGame2 {
        @BeforeEach
        void InitGame() {
            int[][] board = {
                    {2, 4, 1024, 4},
                    {4, 2, 1024, 2},
                    {2, 4,    2, 0},
                    {4, 2,    4, 2}
            };
            gb.setGameBoard(board);
        }

        @Test
        void MoveLeft() {
            gb.moveLeft();
            assertAll(
                    () -> assertFalse(gb.isOver()),
                    () -> assertFalse(gb.isWon())
            );
        }

        @Test
        void MoveRight() {
            gb.moveRight();
            assertAll(
                    () -> assertFalse(gb.isOver()),
                    () -> assertFalse(gb.isWon())
            );
        }

        @Test
        void MoveDown() {
            gb.moveDown();
            assertAll(
                    () -> assertTrue(gb.isOver()),
                    () -> assertTrue(gb.isWon())
            );
        }

        @Test
        void MoveUp() {
            gb.moveUp();
            assertAll(
                    () -> assertTrue(gb.isOver()),
                    () -> assertTrue(gb.isWon())
            );
        }
    }

    @Nested
    class MoveTestsInFinalGame3 {
        @BeforeEach
        void InitGame() {
            int[][] board = {
                    {2,   4,    2,   4},
                    {4,   2, 1024, 128},
                    {4, 128, 1024,   0},
                    {4,   2,   32,  64}
            };
            gb.setGameBoard(board);
        }

        @Test
        void MoveLeft() {
            gb.moveLeft();
            assertAll(
                    () -> assertFalse(gb.isOver()),
                    () -> assertFalse(gb.isWon())
            );
        }

        @Test
        void MoveRight() {
            gb.moveRight();
            assertAll(
                    () -> assertTrue(gb.isOver()),
                    () -> assertFalse(gb.isWon())
            );
        }

        @Test
        void MoveDown() {
            gb.moveDown();
            assertAll(
                    () -> assertTrue(gb.isOver()),
                    () -> assertTrue(gb.isWon())
            );
        }

        @Test
        void MoveUp() {
            gb.moveUp();
            assertAll(
                    () -> assertTrue(gb.isOver()),
                    () -> assertTrue(gb.isWon())
            );
        }
    }


}
