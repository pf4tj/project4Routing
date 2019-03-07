import java.io.*;

public class Project {
    public final static void main(String[] argv) {

        // Network 1
        //
        //    1
        // E0 --- E1
        // | \    |
        // |  \   |
        // |7  \3 | 1
        // |    \ |
        // |     \|
        // E3 --- E2
        //     2
        //
        Pair<Integer, Integer>[][] network1 = new Pair[4][];
        network1[0] = new Pair[] {new Pair(1, 1), new Pair(2, 3), new Pair(3, 7)};
        network1[1] = new Pair[] {new Pair(0, 1), new Pair(2, 1)};
        network1[2] = new Pair[] {new Pair(0, 3), new Pair(1, 1), new Pair(3, 2)};
        network1[3] = new Pair[] {new Pair(0, 7), new Pair(2, 2)};

        // Network 2
        //
        //    1
        // E0 --- E1
        // | \    | \
        // |  \   |  \
        // |3  \4 | 4 |
        // |    \ |   |
        // |     \|   |
        // E3     E2  / 1
        //  \________/
        //
        Pair<Integer, Integer>[][] network2 = new Pair[4][];
        network2[0] = new Pair[] {new Pair(1, 1), new Pair(2, 4), new Pair(3, 3)};
        network2[1] = new Pair[] {new Pair(0, 1), new Pair(2, 4), new Pair(3, 1)};
        network2[2] = new Pair[] {new Pair(0, 4), new Pair(1, 4)};
        network2[3] = new Pair[] {new Pair(0, 3), new Pair(1, 1)};

        // Network 3
        //
        //     1      2
        // E0 --- E1 --- E2
        // | \    |      |
        // |  \5  |3     |1
        // |   \  |      |
        // |3   \-E3     E4
        // |            /
        // E5 ---------/
        //          8
        //
        Pair<Integer, Integer>[][] network3 = new Pair[6][];
        network3[0] = new Pair[] {new Pair(1, 1), new Pair(3, 5), new Pair(5, 3)};
        network3[1] = new Pair[] {new Pair(0, 1), new Pair(2, 2), new Pair(3, 3)};
        network3[2] = new Pair[] {new Pair(1, 2), new Pair(4, 1)};
        network3[3] = new Pair[] {new Pair(0, 5), new Pair(1, 3)};
        network3[4] = new Pair[] {new Pair(2, 1), new Pair(5, 8)};
        network3[5] = new Pair[] {new Pair(0, 3), new Pair(4, 8)};

        // Network 4
        //
        //     1                 5
        // E0 --- E1         E4 --- E5
        // |      |          /\   / |
        // |      |3       4/ 1\ /2 |
        // |2  8  |    1   /    X   |5
        // |  /---E2 ---- E3---/ \  |
        // | /-----------/        \-E6
        // E7       6               |
        //  \----------------------/
        //              15
        //
        Pair<Integer, Integer>[][] network4 = new Pair[8][];
        network4[0] = new Pair[] {new Pair(1, 1), new Pair(7, 2)};
        network4[1] = new Pair[] {new Pair(0, 1), new Pair(2, 3)};
        network4[2] = new Pair[] {new Pair(1, 3), new Pair(3, 1), new Pair(7, 8)};
        network4[3] = new Pair[] {new Pair(2, 1), new Pair(4, 4), new Pair(5, 2), new Pair(7, 6)};
        network4[4] = new Pair[] {new Pair(3, 4), new Pair(5, 5), new Pair(6, 1)};
        network4[5] = new Pair[] {new Pair(3, 2), new Pair(4, 5), new Pair(6, 5)};
        network4[6] = new Pair[] {new Pair(4, 1), new Pair(5, 5), new Pair(7, 15)};
        network4[7] = new Pair[] {new Pair(0, 2), new Pair(2, 8), new Pair(3, 6), new Pair(6, 15)};



        NetworkSimulator simulator;
        simulator = new NetworkSimulator(network4, 500, 3);

        simulator.run();
        simulator.display_forwarding_table(0);
        simulator.display_forwarding_table(1);
        simulator.display_forwarding_table(2);
        display_route(simulator.route_packet(0, 3));
    }

    public static void display_route(int[] hops) {
        System.out.print("[");
        for (int i=0; i<hops.length; i++) {
            System.out.print(hops[i] + " ");
        }

        System.out.println("]");
    }
}
