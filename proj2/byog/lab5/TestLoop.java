package byog.lab5;

import java.util.ArrayList;
import org.junit.Test;

public class TestLoop {

     private static boolean exist(int h, ArrayList<Integer> list) {
         for (Integer i : list) {
             if (h == i) {
                 return true;
             }
         }
         return false;
     }

     private static ArrayList<Integer> adjacentHexagons(int i) {
         ArrayList<Integer> l = new ArrayList<>();
         l.add(i - 1);
         l.add(i - 2);
         l.add(i + 1);
         l.add(i + 2);
         return l;
     }


     @Test
     public void testLoop() {

         ArrayList<Integer> centerHexagons = new ArrayList<>();
         ArrayList<Integer> uniqueHexagons = new ArrayList<>();

         centerHexagons.add(5);
         uniqueHexagons.add(5);

         for(int i = 0; i < centerHexagons.size(); i += 1) {
             ArrayList<Integer> adjacent = adjacentHexagons(centerHexagons.get(i));
             for (Integer h : adjacent) {
                 if (exist(h, uniqueHexagons)) {
                     System.out.println("Already added: " + h);
                 } else if (h < 0 || h > 10) {
                     System.out.println("Out of scope: " + h);
                 } else {
                     uniqueHexagons.add(h);
                     centerHexagons.add(h);
                     System.out.println("added " + h);
                 }
             }
         }

         for (int i : uniqueHexagons) {
             System.out.println(i);
         }

     }
}
