import java.io.*;
public class Main {

    public static void main(String[] args) {
//        Pair<Integer,Integer> test = new Pair (1,1);
//        System.out.println(test);
        Entity e = new Entity(1,3);
        Entity e1 = new Entity(1,3);

        Pair<Integer, Integer>[][] network1 = new Pair[4][];
        network1[0] = new Pair[] {new Pair(0, 3), new Pair(1, 8), new Pair(2, 8)};
        network1[1] = new Pair[] {new Pair(0, 1), new Pair(1,3), new Pair(2, 4)};
        network1[2] = new Pair[] {new Pair(0, 3), new Pair(1, 1), new Pair(3, 2)};
        network1[3] = new Pair[] {new Pair(0, 7), new Pair(2, 2)};
//        System.out.println(network1[0][0]);

        Packet p0[] = e.initialize_costs(network1[0]);
        Packet p1[] = e.initialize_costs(network1[1]);
        System.out.println(p1[0].toString());
//        Packet p2[] = e.initialize_costs(network1[2]);
        e.update(p1[2]);
//      e.update()
    }
}
