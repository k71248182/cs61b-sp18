import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    //private ArrayList<Vertex> vertices;
    private HashMap<Long, Vertex> vertices;

    class Vertex {
        Long id;
        double lat;
        double lon;
        String name;
        private ArrayList<Edge> edges;

        Vertex(Long id, double lat, double lon) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            edges = new ArrayList<>();
        }

        void addLocation(String location) {
            name = location;
        }

        void addEdge(Vertex v, String roadName) {
            Edge edge = new Edge(this, v, roadName);
            edges.add(edge);
        }

        void removeEdge() {

        }

        ArrayList<Edge> getEdges() {
            return edges;
        }

        ArrayList<Long> adjacent() {
            ArrayList<Long> adjacentVertices = new ArrayList<>();
            for (Edge edge : edges) {
                adjacentVertices.add(edge.end.id);
            }
            return adjacentVertices;
        }
    }

    class Edge {
        private Vertex start;
        private Vertex end;
        private String roadName;

        Edge(Vertex startV, Vertex endV, String name) {
            start = startV;
            end = endV;
            this.roadName = name;
        }

        Vertex getStart() {
            return start;
        }

        Vertex getEnd() {
            return end;
        }

        String getRoadName() {
            return roadName;
        }
    }

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        vertices = new HashMap<>();
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        List<Long> removeNodes = new ArrayList<>();
        for (Long node : vertices()) {
            Vertex v = vertices.get(node);
            if (v.getEdges().size() == 0) {
                removeNodes.add(node);
            }
        }
        for (Long node : removeNodes) {
            vertices.remove(node);
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        return vertices.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        Vertex vertex = vertices.get(v);
        return vertex.adjacent();
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        double minDistance = Double.MAX_VALUE;
        long closestNode = 0;
        for (Long nodeID : vertices()) {
            Vertex v = vertices.get(nodeID);
            double lonV = v.lon;
            double latV = v.lat;
            double distance = distance(lon, lat, lonV, latV);
            if (distance < minDistance) {
                minDistance = distance;
                closestNode = nodeID;
            }
        }
        return closestNode;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        Vertex vertex = vertices.get(v);
        return vertex.lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        Vertex vertex = vertices.get(v);
        return vertex.lat;
    }

    /** add a vertex to graph. */
    Vertex addVertex(Long id, double lat, double lon) {
        Vertex v = new Vertex(id, lat, lon);
        vertices.put(id, v);
        return v;
    }

    /** add an edge to graph. */
    void addEdge(Vertex v1, Vertex v2, String roadName) {
        v1.addEdge(v2, roadName);
        v2.addEdge(v1, roadName);
    }

    /** Add a highway to graph. */
    void addWay(List<Long> nodeIDs, String roadName) {
        int n = nodeIDs.size();
        for (int i = 0; i < n - 1; i += 1) {
            Long startNodeID = nodeIDs.get(i);
            Long endNodeID = nodeIDs.get(i + 1);
            Vertex startV = vertices.get(startNodeID);
            Vertex endV = vertices.get(endNodeID);
            addEdge(startV, endV, roadName);
        }
    }

    /** Return total number of vertices in the graph. */
    double numVertices() {
        return vertices.keySet().size();
    }
}
