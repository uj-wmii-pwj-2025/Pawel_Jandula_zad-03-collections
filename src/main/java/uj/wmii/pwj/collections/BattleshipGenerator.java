package uj.wmii.pwj.collections;
import java.util.Random;
public interface BattleshipGenerator {

    String generateMap();

    static BattleshipGenerator defaultInstance() {
        return new BattleShipImplementation();
    }
} 
 class BattleShipImplementation implements BattleshipGenerator {
    private static final int Size = 10;
    private static final char Ships = '#';
    private static final char Water = '.';
    private static final int[] ShipSizes = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
    private final Random random = new Random();
    private final char[][] board = new char[Size][Size];
    @Override
    public String generateMap() {
        while (true) {
            if (tryGenerateBoard()) {
                return flattenBoard();
            }
        }
    }
    public boolean tryGenerateBoard() {
        clearBoard(); 
        for (int shipSize : ShipSizes) {
            boolean placed = false;
            int attempts = 0;
            while (!placed && attempts < 1000) {
                placed = tryPlaceShip(shipSize);
                attempts++;
            }
            if (!placed) {
                return false;
            }
        }
        return true;
    }
    private boolean tryPlaceShip(int shipSize) {
        boolean horizontal = random.nextBoolean();
        int row, col;
        if (horizontal) {
            row = random.nextInt(Size);
            col = random.nextInt(Size - shipSize + 1);
        } 
        else {
            row = random.nextInt(Size - shipSize + 1);
            col = random.nextInt(Size);
        }

        if (!canPlaceShip(row, col, shipSize, horizontal)) {
            return false;
        }

        for (int i = 0; i < shipSize; i++) {
            if (horizontal) {
                board[row][col + i] = Ships;
            } 
            else {
                board[row + i][col] = Ships;
            }
        }
        return true;
    }
    private boolean canPlaceShip(int row, int col, int shipSize, boolean horizontal) {
        for (int i = -1; i <= shipSize; i++) {
            for (int j = -1; j <= 1; j++) {
                int r;
                int c;
                if (horizontal) {
                    r = row + j;
                    c = col + i;
                } 
                else {
                    r = row + i;
                    c = col + j;
                }
                if (r >= 0 && r < Size && c >= 0 && c < Size) {
                    if (board[r][c] == Ships) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    private void clearBoard() {
        for (int i = 0; i < Size; i++) {
            for (int j = 0; j < Size; j++) {
                board[i][j] = Water;
            }
        }
    }
    private String flattenBoard() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Size; i++) {
            for (int j = 0; j < Size; j++) {
                sb.append(board[i][j]);
            }
        }
        return sb.toString();
    }
}
