import org.eclipse.jetty.server.session.SessionHandler;

import javax.swing.plaf.metal.MetalTheme;
import java.io.FileInputStream;
import java.security.UnresolvedPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
/**
 * Rastering is the job of converting information into a pixel-by-pixel image.
 */
public class Rasterer {

    private static final double ULLON = -122.2998046875;
    private static final double ULLAT = 37.892195547244356;
    private static final double LRLON = -122.2119140625;
    private static final double LRLAT = 37.82280243352756;
    private static final double IMGPIXELS = 256;

    private double[] levelLonDDP;
    private double[] levelLatDDP;

    public Rasterer() {
        setLevelDDP();
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);

        // If the inputs are invalid, return null.
        if (!checkInputParams(params)) {
            return null;
        }

        // Process input parameters
        double lowerRightLongitude = params.get("lrlon");
        double lowerRightLatitude = params.get("lrlat");
        double upperLeftLongitude = params.get("ullon");
        double upperLeftLatitude = params.get("ullat");
        double windowWidth = params.get("w");
        double windowHeight = params.get("h");

        // Requested location is outside of the root longitude/latitudes.
        if (!locationInScope(params)) {
            Map<String, Object> resultQueryFailed = new HashMap<>();
            resultQueryFailed.put("query_success", false);
            return resultQueryFailed;
        }

        // Doing the work
        int level = getImgLevel(lowerRightLongitude, upperLeftLongitude, windowWidth);

        int minX = getImgX(level, upperLeftLongitude);
        int minY = getImgY(level, upperLeftLatitude);
        int maxX = getImgX(level, lowerRightLongitude);
        int maxY = getImgY(level, lowerRightLatitude);

        double raster_ul_lat = getTopLatitude(level, minY);
        double raster_ul_lon = getLeftLongitude(level, minX);
        double raster_lr_lat = getBottomLatitude(level, maxY);
        double raster_lr_lon = getRightLongitude(level, maxX);

        String[][] renderGrid = getRenderGrid(level, minX, minY, maxX, maxY);

        // Prepare output parameters
        Map<String, Object> results = new HashMap<>();
        results.put("raster_ul_lon", raster_ul_lon);
        results.put("raster_ul_lat", raster_ul_lat);
        results.put("raster_lr_lon", raster_lr_lon);
        results.put("raster_lr_lat", raster_lr_lat);
        results.put("depth", level);
        results.put("query_success", true);
        results.put("render_grid", renderGrid);

        //results = getDefaultMapRaster();
        return results;
    }

    /** Default image output used for initial testing. */
    private Map<String, Object> getDefaultMapRaster() {
        Map<String, Object> results = new HashMap<>();
        results.put("raster_ul_lon", -122.24212646484375);
        results.put("raster_ul_lat", 37.87701580361881);
        results.put("raster_lr_lon", -122.24006652832031);
        results.put("raster_lr_lat", 37.87538940251607);
        results.put("depth", 7);
        results.put("query_success", true);
        int n = 3;
        String[][] renderGrid = new String[n][n];
        for (int i = 0; i < n; i += 1) {
            for (int j = 0; j < n; j += 1) {
                String imgFile = "d7_x8" + (j + 4)
                        + "_y" + (i + 28) + ".png";
                renderGrid[i][j] = imgFile;
            }
        }
        results.put("render_grid", renderGrid);
        return results;
    }

    /** Return longitudinal distance per pixel. */
    private double lonDPP(double lrlon, double uplon, double width) {
        double lonDPP = (lrlon - uplon) / width;
        return lonDPP;
    }

    /** Return latitudinal distance per pixel. */
    private double latDPP(double lrlat, double ullat, double height) {
        double latDPP = (ullat - lrlat) / height;
        return latDPP;
    }

    /** Calculate the LonDDP and LatDDP for each level and store them. */
    private void setLevelDDP() {
        levelLonDDP = new double[8];
        levelLatDDP = new double[8];
        double largestLonDPP = lonDPP(LRLON, ULLON, IMGPIXELS);
        double largestLatDPP = latDPP(LRLAT, ULLAT, IMGPIXELS);
        for (int i = 0; i <= 7; i += 1) {
            int numImg = (int) Math.pow(2, i);
            levelLonDDP[i] = largestLonDPP / numImg;
            levelLatDDP[i] = largestLatDPP / numImg;
        }
    }

    /** Return the correct img level based on LonDDP:
     * find the level with the greatest LonDPP that is less than
     * or equal to the LonDPP of the query box.
     * Return level 7 if the requested LonDPP is not available.
     */
    private int getImgLevel(double lrlon, double uplon, double width) {
        double requestLonDPP = lonDPP(lrlon, uplon, width);
        for (int i = 0; i <= 7; i += 1) {
            if (levelLonDDP[i] <= requestLonDPP) {
                return i;
            }
        }
        return 7;
    }

    /** Return the x index of the image file that contains the
     * input pixel.
     * Return -1 if the requested point is out of boundary.
     * @param level
     * @param lon
     * @return
     */
    private int getImgX(int level, double lon) {
        int maxX = (int) Math.pow(2, level) - 1;
        for (int x = 0; x <= maxX; x += 1) {
            double left = ULLON + x * levelLonDDP[level] * 256;
            double right = left + levelLonDDP[level] * 256;
            if (left <= lon && right >= lon) {
                return x;
            }
        }
        return -1;
    }

    /** Return the y index of the image file that contains the
     * input pixel.
     * Return -1 if the requested point is out of boundary.
     * @param level
     * @param lat
     * @return
     */
    private int getImgY(int level, double lat) {
        int maxY = (int) Math.pow(2, level) - 1;
        for (int y = 0; y <= maxY; y += 1) {
            double top = ULLAT - y * levelLatDDP[level] * 256;
            double bottom = top - levelLatDDP[level] * 256;
            if (bottom <= lat && top >= lat) {
                return y;
            }
        }
        return -1;
    }

    /** Return the full name of image file for the given
     * level, x index and y index.
     * @param level
     * @param x
     * @param y
     * @return
     */
    private String getImgFileName(int level, int x, int y) {
        String imgFile = "d" + level;
        imgFile += "_x" + x;
        imgFile += "_y" + y;
        imgFile += ".png";
        return imgFile;
    }

    /** Return the names of all image files that need to be displayed.
     * @param level
     * @param minX
     * @param minY
     * @param maxX
     * @param maxY
     * @return
     */
    private String[][] getRenderGrid(int level,
                                     int minX, int minY,
                                     int maxX, int maxY) {
        int numX = maxX - minX + 1;
        int numY = maxY - minY + 1;
        String[][] renderGrid = new String[numY][numX];
        for (int x = minX; x <= maxX; x += 1) {
            for (int y = minY; y <= maxY; y += 1) {
                String imgFile = getImgFileName(level, x, y);
                int j = x - minX;
                int i = y - minY;
                renderGrid[i][j] = imgFile;
                System.out.println(imgFile);
            }
        }
        return renderGrid;
    }

    /** Return the leftmost longitude of the provided image. */
    private double getLeftLongitude(int level, int x) {
        double left = ULLON + x * levelLonDDP[level] * 256;
        return left;
    }

    /** Return the rightmost longitude of the provided image. */
    private double getRightLongitude(int level, int x) {
        double right = ULLON + (x + 1) * levelLonDDP[level] * 256;
        return right;
    }

    /** Return the uppermost latitude of the provided image. */
    private double getTopLatitude(int level, int y) {
        double top = ULLAT - y * levelLatDDP[level] * 256;
        return top;
    }

    /** Return the lowermost latitude of the provided image. */
    private double getBottomLatitude(int level, int y) {
        double top = ULLAT - (y + 1) * levelLatDDP[level] * 256;
        return top;
    }

    /** Check if the provided input parameters are valid. */
    private boolean checkInputParams(Map<String, Double> params) {
        boolean validParams = true;

        // check if the input params contain the following keys.
        validParams = validParams & params.containsKey("lrlon");
        validParams = validParams & params.containsKey("lrlat");
        validParams = validParams & params.containsKey("ullon");
        validParams = validParams & params.containsKey("ullat");
        validParams = validParams & params.containsKey("w");
        validParams = validParams & params.containsKey("h");

        // check if the width and height are positive. */
        validParams = validParams & (params.get("w") > 0);
        validParams = validParams & (params.get("h") > 0);

        return validParams;
    }

    /** Check if the requested location is within boundary. */
    private boolean locationInScope(Map<String, Double> params) {
        if (params.get("lrlon") < ULLON) {
            return false;
        }
        if (params.get("lrlat") > ULLAT) {
            return false;
        }
        if (params.get("ullon") > LRLON) {
            return false;
        }
        if (params.get("ullat") < LRLAT) {
            return false;
        }
        return true;
    }

}
