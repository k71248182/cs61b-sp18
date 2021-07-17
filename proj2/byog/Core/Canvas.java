package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;
import java.util.ArrayList;

/** The Canvas object is used to represent the whole world.
 * Everything in the world will be drawn on the same canvas.
 * A 2D array of tiles make up the canvas.
 */
public class Canvas {

    private static final int MINROOMCOUNT = 25;
    private static final int MAXROOMCOUNT = 50;
    private static final int MAXTRY = 20;
    private static final int MAXHALLWAY = 8;

    private static int maxWidth;
    private static int maxHeight;
    private TETile[][] tiles;
    private ArrayList<Room> rooms;
    private Random random;
    private Position Player;

    /** Constructor */
    public Canvas(int weight, int height, Random random) {
        maxWidth = weight;
        maxHeight = height;
        tiles = new TETile[maxWidth][maxHeight];
        this.random = random;
        rooms = new ArrayList<>();
        drawBackground();
    }

    /** Return TETiles representing the canvas */
    public TETile[][] getTiles() {
        return tiles;
    }

    /** Create a random world. */
    public void createWorld() {
        addRandomRooms();
        addHallways();
        closeOpenHallways();
    }

    /** Add a player to the world.
     * The player should be added to a floor tile after
     * the world has be created.
     */
    public void addPlayer() {
        while (true) {
            int x = nextRandomInt(0, maxWidth);
            int y = nextRandomInt(0, maxHeight);
            if (tiles[x][y] == Tileset.FLOOR) {
                Player = new Position(x, y);
                tiles[x][y] = Tileset.PLAYER;
                return;
            }
        }


    }

    /** Move the player for one step depending on user input.
     * This is doable only when the new position is floor tile.
     */
    public void moveOneStep(char key) {
        Position newPlayerPosition = newPlayerPosition(key);
        if (validPlayerPosition(newPlayerPosition)) {
            tiles[Player.x][Player.y] = Tileset.FLOOR;
            Player = newPlayerPosition;
            tiles[Player.x][Player.y] = Tileset.PLAYER;
        }
    }

    /** Return the new player position depending on user input.
     * This method does not check the validity of the new position.
     * @param key
     * @return
     */
    private Position newPlayerPosition(char key) {
        // Convert lower case char to upper case.
        key = Character.toUpperCase(key);
        switch (key) {
            case 'A': return Player.shift(-1, 0);
            case 'W': return Player.shift(0, 1);
            case 'D': return Player.shift(1, 0);
            case 'S': return Player.shift(0, -1);
            default: return Player;
        }
    }

    /** Return true if the position is reachable to the player. */
    private boolean validPlayerPosition(Position p) {
        if (p.x < 0 || p.y < 0 || p.x >= maxWidth || p.y >= maxHeight) {
            return false;
        } else if (tiles[p.x][p.y] == Tileset.FLOOR) {
            return true;
        } else if (tiles[p.x][p.y] == Tileset.PLAYER) {
            return true;
        } else {
            return false;
        }
    }

    /** Add random number of random rooms on canvas. */
    private void addRandomRooms() {
        int count = nextRandomInt(MINROOMCOUNT, MAXROOMCOUNT);
        for (int t = 0; t < count; t += 1) {
            Room randomRoom = randomRoom(1);
            if (randomRoom != null) {
                drawShapes(randomRoom);
                rooms.add(randomRoom);
            }
        }
    }

    /** Add hallways to canvas.
     * For each room, add a hallway on the right wall if possible.
     * For each room, add a hallway on the top wall if possible.
     */
    private void addHallways() {
        for (Room room : rooms) {
            addHallwayR(room.walls[2]);
        }
        for (Room room : rooms) {
            addHallwayT(room.walls[3]);
        }
    }

    /** Close open end hallways.
     * If there is a floor tile directly connected to nothing,
     * create a wall between them.
     */
    private void closeOpenHallways() {
        for (int x = 0; x < maxWidth; x += 1) {
            for (int y = 0; y < maxHeight; y += 1) {
                if (tiles[x][y] == Tileset.FLOOR) {
                    floorSurrounding(new Position(x, y));
                }
            }
        }
    }

    /** Return the positions of all surrounding tiles. */
    private Position[] surroundingPositions(Position p) {
        Position[] sp = new Position[8];
        sp[0] = p.shift(-1, 0);
        sp[1] = p.shift(-1, 1);
        sp[2] = p.shift(-1, -1);
        sp[3] = p.shift(0, -1);
        sp[4] = p.shift(0, 1);
        sp[5] = p.shift(1, -1);
        sp[6] = p.shift(1, 0);
        sp[7] = p.shift(1, 1);
        return sp;
    }

    /** Fot the tile in the given position, change all
     * surrounding NOTHING tiles to WALL.
     */
    private void floorSurrounding(Position p) {
        Position[] sp = surroundingPositions(p);
        for (Position s : sp) {
            if (s.x >= 0 && s.y >= 0 && s.x < maxWidth && s.y < maxHeight) {
                if (tiles[s.x][s.y] == Tileset.NOTHING) {
                    tiles[s.x][s.y] = Tileset.WALL;
                }
            }
        }
    }

    /** Change every tile on canvas to NOTHING. */
    private void drawBackground() {
        drawRectangles(new Position(0, 0), maxWidth, maxHeight, Tileset.NOTHING);
    }

    /** Draw a rectangle on canvas.
     * Existing tiles will be overwritten.
     * Do nothing if any part of the rectangle will
     * go beyond the scope of the canvas.
     * @param sp Starting position of the rectangle
     * @param width
     * @param height
     * @param tile
     */
    private void drawRectangles(Position sp, int width, int height, TETile tile) {

        // Do nothing if any part of the rectangle goes beyond the scope.
        if (sp.x < 0 || sp.y < 0) {
            return;
        }
        if (sp.x + width > maxWidth) {
            return;
        }
        if (sp.y + height > maxHeight) {
            return;
        }

        for (int x = sp.x; x < sp.x + width; x += 1) {
            for (int y = sp.y; y < sp.y + height; y += 1) {
                tiles[x][y] = tile;
            }
        }
    }

    /** Draw a wall on canvas.
     * @param wall
     */
    private void drawWalls(Wall wall) {
        drawRectangles(wall.position, wall.getWidth(), wall.getHeight(), Tileset.WALL);
    }

    /** Draw a shape on canvas.
     * We will draw the floor and walls of the shape.
     * @param shape
     */
    private void drawShapes(Shapes shape) {
        drawRectangles(shape.getPosition(), shape.getWidth(), shape.getHeight(), Tileset.FLOOR);
        for (int i = 0; i < shape.walls.length; i += 1) {
            drawWalls(shape.walls[i]);
        }
    }

    /** Return a random int between min and max.
     * Min is a possible return value, but max is not.
     * @param min
     * @param max
     * @return
     */
    private int nextRandomInt(int min, int max) {
        int nextInt = min + random.nextInt(max - min);
        return nextInt;
    }

    /** Create a room with random width, height, and location.
     * The room must have both width and height that are greater
     * than or equal to 4, and any part of the room should not
     * go beyond the canvas.
     * @return
     */
    private Room randomRoom(int tries) {
        if (tries > MAXTRY) {
            return null;
        } else {
            int h = nextRandomInt(Room.MIN, Room.MAX + 1);
            int w = nextRandomInt(Room.MIN, Room.MAX + 1);
            int x = nextRandomInt(0, maxWidth - w);
            int y = nextRandomInt(0, maxHeight - h);
            Room randomRoom = new Room(w, h, new Position(x, y));
            while (overlap(randomRoom)) {
                return randomRoom(tries + 1);
            }
            return randomRoom;
        }
    }

    /** Return true if the shape will be overlapped with
     * anything else.
     * @param s
     * @return
     */
    private boolean overlap(Shapes s) {
        for (int i = 0; i < s.width; i += 1) {
            for (int j = 0; j < s.height; j += 1) {
                int x = s.position.x + i;
                int y = s.position.y + j;
                if (tiles[x][y] != Tileset.NOTHING) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Return a vertical hallway that connected to another shape.
     * Starting position is provided.
     * Return null if no such value can be found.
     * @param startP
     * @return
     */
    private VerticalHallway hallwayCloseEndT(Position startP) {
        for (int i = 1; i < MAXHALLWAY; i += 1) {
            // Return zero if the up boundary has been reached.
            if (startP.y + i >= maxHeight) {
                return null;
            }
            // Find the shortest close end hallway (wall followed by floor).
            if (tiles[startP.x + 1][startP.y + i] == Tileset.WALL
                    && tiles[startP.x + 1][startP.y + i + 1] == Tileset.FLOOR) {
                VerticalHallway vhw = new VerticalHallway(i + 1, startP);
                return vhw;
            }
        }
        return null;
    }

    /** Return a vertical hallway of random length that does not connect
     * to any other existing shapes.
     * @param startP
     * @return
     */
    private VerticalHallway hallwayOpenEndT(Position startP) {
        int maxLength = 1;
        // Find the maximum hallway length before reaching anything.
        while (startP.y + maxLength < maxHeight) {
            if (tiles[startP.x][startP.y + maxLength] != Tileset.NOTHING
                    || tiles[startP.x + 1][startP.y + maxLength] != Tileset.NOTHING
                    || tiles[startP.x + 2][startP.y + maxLength] != Tileset.NOTHING) {
                break;
            }
            maxLength += 1;
        }
        // Give up creating hallway if there is not enough room.
        if (maxLength < 6) {
            return null;
        }
        // Create a hallway of random length.
        int randomLength = nextRandomInt(5, maxLength);
        VerticalHallway vhw = new VerticalHallway(randomLength, startP);
        return vhw;
    }

    /** Return a horizontal hallway that connected to another shape.
     * Starting position is provided.
     * Return null if no such value can be found.
     * @param startP
     * @return
     */
    private HorizontalHallway hallwayCloseEndR(Position startP) {
        for (int i = 1; i < MAXHALLWAY; i += 1) {
            // Return zero if the right boundary has been reached.
            if (startP.x + i >= maxWidth) {
                return null;
            }
            // Find the shortest close end hallway (wall followed by floor).
            if (tiles[startP.x + i][startP.y + 1] == Tileset.WALL
                    && tiles[startP.x + i + 1][startP.y + 1] == Tileset.FLOOR) {
                HorizontalHallway hhw = new HorizontalHallway(i + 1, startP);
                return hhw;
            }
        }
        return null;
    }

    /** Return a horizontal hallway of random length that does not connect
     * to any other existing shapes.
     * @param startP
     * @return
     */
    private HorizontalHallway hallwayOpenEndR(Position startP) {
        int maxLength = 1;
        // Find the maximum hallway length before reaching anything.
        while (startP.x + maxLength < maxWidth) {
            if (tiles[startP.x + maxLength][startP.y] != Tileset.NOTHING
                    || tiles[startP.x + maxLength][startP.y + 1] != Tileset.NOTHING
                    || tiles[startP.x + maxLength][startP.y + 2] != Tileset.NOTHING) {
                break;
            }
            maxLength += 1;
        }
        // Give up creating hallway if there is not enough room.
        if (maxLength < 6) {
            return null;
        }
        // Create a hallway of random length.
        int randomLength = nextRandomInt(5, maxLength);
        HorizontalHallway hhw = new HorizontalHallway(randomLength, startP);
        return hhw;
    }

    /** Add a horizontal hallway on the right of the given wall.
     * If it is an open ended hallway, add an up turn.
     * @param wall
     */
    private void addHallwayR(Wall wall) {
        // First check if there is a hallway that can connect to another room or hallway.
        for (int t = 0; t < MAXTRY; t += 1) {
            int randomSpot = nextRandomInt(0, wall.height - 2);
            Position randomP = wall.position.shift(0, randomSpot);
            HorizontalHallway hhw = hallwayCloseEndR(randomP);
            if (hhw != null) {
                drawShapes(hhw);
                return;
            }
        }
        //If not, create an open end hallway.
        for (int t = 0; t < MAXTRY; t += 1) {
            int randomSpot = nextRandomInt(0, wall.height - 2);
            Position randomP = wall.position.shift(0, randomSpot);
            HorizontalHallway hhw = hallwayOpenEndR(randomP);
            if (hhw != null) {
                drawShapes(hhw);
                // Handle the open end: create a turn.
                Position spTop = randomP.shift(hhw.width - 2, 2);
                VerticalHallway vhw = hallwayCloseEndT(spTop);
                if (vhw != null) {
                    drawShapes(vhw);
                }
                return;
            }
        }
    }

    /** Add a vertical hallway on the top of the given wall.
     * @param wall
     */
    private void addHallwayT(Wall wall) {
        // First check if there is a hallway that can connect to another room or hallway.
        for (int t = 0; t < MAXTRY; t += 1) {
            int randomSpot = nextRandomInt(0, wall.width - 2);
            Position randomP = wall.position.shift(randomSpot, 0);
            VerticalHallway vhw = hallwayCloseEndT(randomP);
            if (vhw != null) {
                drawShapes(vhw);
                return;
            }
        }
        //If not, create an open end hallway.
        for (int t = 0; t < MAXTRY; t += 1) {
            int randomSpot = nextRandomInt(0, wall.width - 2);
            Position randomP = wall.position.shift(randomSpot, 0);
            VerticalHallway vhw = hallwayOpenEndT(randomP);
            if (vhw != null) {
                drawShapes(vhw);
                return;
            }
        }
    }

}
