import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Arrays;
import java.lang.Math;

//d_X(y) = cost of least path from x to y
/**
 * d_x(y) = min taken over all neighbours v of x
 * d_x(y) =   min{ {c(x,v}                     + d_v(y)}
 *     cost to neighbour v                cost from neighbour v to destination y
 */


//private static final String TAG="";
// Code for an entity in the network. This is where you should implement the
// distance-vector algorithm.

// Entity that represents a node in the network.
//
// Each function should be implemented so that the Entity can be instantiated
// multiple times and successfully run a distance-vector routing algorithm.
public class Entity {
    int index;
    int number_of_entities;
    int costs[];
    int[][] nodeTable;
    HashMap<Integer, Integer> bestNextMap;
    public static final int maxVal = 999;
    public boolean debug = true;
    public boolean debugConstructor = false && debug;
    public boolean debugInit = false&& debug;
    public boolean debugUpdate = false && debug;
    public boolean debugCosts = false && debug;
    public boolean debugForward = false & debug;

    // This initialization function will be called at the beginning of the
    // simulation to setup all entities.
    //
    // Arguments:
    // - `entity_index`:    The id of this entity.
    // - `number_entities`: Number of total entities in the network.
    //
    // Return Value: None.
    public Entity(int entity_index, int number_of_entities) {
        this.index = entity_index;
        this.number_of_entities = number_of_entities;
        this.costs = new int[number_of_entities];
        this.nodeTable = new int[number_of_entities][number_of_entities];
        this.bestNextMap = new HashMap<Integer,Integer>(number_of_entities);
        for (int src = 0; src < number_of_entities; src++) {
            if (src==index) costs[index] = 0;
            else costs[src] = maxVal;
            for (int dest = 0; dest < number_of_entities; dest++) {
                if (src == dest) nodeTable[src][dest] = 0;
                else nodeTable[src][dest] = maxVal;
            }
        }
        if (debugConstructor) {
            System.out.println("-------------------------------------------------------");
            System.out.println("ENTITY CONSTRUCTOR");
            System.out.printf("Initialized distancesVector for entity id = %d out of %d \n", index, number_of_entities);
            for (int i = 0; i < number_of_entities; ++i){
                System.out.print(costs[i] + "\t");
            }
            System.out.println();
            System.out.printf("Initialized nodeTable for entity id = %d \n", index);
            for (int i = 0; i < number_of_entities; i++) {
                for (int j = 0; j < number_of_entities; j++) {
                    System.out.print(nodeTable[i][j] + "\t");
                }
                System.out.println();
            }
            System.out.println("-------------------------------------------------------");
        }

    }

    // This function will be called at the beginning of the simulation to
    // provide a list of neighbors and the costs on those one-hop links.
    //
    // Arguments:
    // - `neighbor_costs`:  Array of (entity_index, cost) tuples for
    //                      one-hop neighbors of this entity in this network.
    //
    // Return Value: This function should return an array of `Packet`s to be
    // sent from this entity (if any) to neighboring entities.
    public Packet[] initialize_costs(Pair<Integer, Integer> neighbor_costs[]) {
        int numPackets = neighbor_costs.length;
        Packet[] packetArr = new Packet[numPackets];
        for (int i = 0; i < numPackets; i++) {
            if (index == neighbor_costs[i].x) continue;
            if ((neighbor_costs[i].y) != this.maxVal) this.costs[neighbor_costs[i].x] = (neighbor_costs[i].y);
        }
        for (int i = 0; i < costs.length; i++){
            this.nodeTable[index][i] = costs[i];
            bestNextMap.put(i,i);
        }
        for (int i = 0; i < numPackets; i++) {
            Packet p = new Packet(neighbor_costs[i].x, costs);
            p.set_source(this.index);
            packetArr[i] = p;
        }
        if (debugInit) {
            System.out.println("INITIALIZE COSTS");
            for (int i = 0; i < numPackets; i++) {
                System.out.println("printing out neighbour costs : entity idx = " + neighbor_costs[i].x + " cost = " +  neighbor_costs[i].y);
            }

            System.out.printf("Printing out distancesArr values \n", index);
            for (int i = 0; i < number_of_entities; i++){
                System.out.print(costs[i] + " , ");
            }
            System.out.println();
            System.out.printf("Updated nodeTable when calling initialize costs on entity id =  %d. \n", index);
            for (int i = 0; i < number_of_entities; i++) {
                for (int j = 0; j < number_of_entities; j++) {
                    System.out.print(nodeTable[i][j] + "\t");
                }
                System.out.println();
            }
            System.out.print("printing out next best hash map : ");
            System.out.println(bestNextMap);

            System.out.println("printing out packets to send");
            for (int i = 0; i < numPackets; i++) {
                System.out.print(packetArr[i] + " , ");
            }
            System.out.println();
            System.out.println("-------------------------------------------------------");
        }
        return packetArr;
    }

    // This function is called when a packet arrives for this entity.
    //
    // Arguments:
    // - `packet`: The incoming packet of type `Packet`.
    //
    // Return Value: This function should return an array of `Packet`s to be
    // sent from this entity (if any) to neighboring entities.
    public Packet[] update(Packet packet) {
//        System.out.println("-------------------------------------------------------");
//        System.out.println("UPDATE PACKETS");
//        System.out.printf("INDEX = %d \n", index);
        boolean update = false;
        int numPackets = packet.get_costs().length;
        Packet[] packetArr = new Packet[numPackets];
//        System.out.printf("costs array length = %d \n", numPackets);
        int[] incomingCostArr = packet.get_costs();
//        System.out.print("Incoming cost array : ");
//        for (int i = 0; i < numPackets; i++){
//            System.out.print(incomingCostArr[i] + " , ");
//        }
//        System.out.println();
        for (int dest = 0; dest < number_of_entities; dest++) {
            if ((dest == index) || (incomingCostArr[dest]==0)) continue;
            int cheapest = incomingCostArr[dest];
            int throughNode = dest;
//            System.out.printf("cheapest = %d \n", cheapest);
//            System.out.printf("index = %d, cheapest = %d, dest = %d, throughNode = %d \n",index,cheapest,dest,throughNode);
            for (int src = 0; src < costs.length; src++){
//                if (src == index || incomingCostArr[src] == maxVal){
//                    continue;
//                }
                int costThrough = incomingCostArr[src] + nodeTable[src][dest];
//                System.out.printf("costThrough = %d, incoming cost arr = %d, node table value = %d, cheapest = %d\n",costThrough, incomingCostArr[src],nodeTable[src][dest],cheapest);
                if (costThrough < cheapest){
                    cheapest = costThrough;
                    throughNode = src;
                }
            }
//            System.out.printf("Updated cost for %s -> %s. Prev cost: %s. New cost: %s. Routes via %s\n", index, dest, nodeTable[index][dest], cheapest, throughNode);
            if (cheapest != nodeTable[index][dest]){
//                nodeTable[index][index] = 0;
                System.out.printf("Updated cost for %s -> %s. Prev cost: %s. New cost: %s. Routes via %s\n", index, dest, nodeTable[index][dest], cheapest, throughNode);
                nodeTable[index][dest] = cheapest;
//                System.out.printf("Updated cost for %s -> %s. Prev cost: %s. New cost: %s. Routes via %s\n", index, dest, nodeTable[index][dest], cheapest, throughNode);
                bestNextMap.put(dest,throughNode);
                update = true;
            }
//            for (int src = 0; src < numPackets; src++) {
//                for (int dest = 0; dest < numPackets; dest++) {
//                    if (src == dest) continue;
//                    if (distancesArr[dest] > (incomingCostArr[src] + this.entityMatrix[src][dest])) {
//                        distancesArr[dest] = incomingCostArr[src] + this.entityMatrix[src][dest];
//                    }
//                }
////                System.out.print(incomingCostArr[src] + " , ");
//            }
        }
        for (int i = 0; i < numPackets; i++) {
            Packet p = new Packet(i, costs);
            p.set_source(this.index);
            packetArr[i] = p;
        }

        if (debugUpdate) {
            System.out.println("-------------------------------------------------------");
            System.out.println("UPDATE PACKETS");
            System.out.printf("INDEX = %d \n ", index);
            System.out.println("incoming cost array");
            for (int i = 0; i < numPackets; i++){
                System.out.print(incomingCostArr[i] + " , ");
            }
            System.out.println();
            System.out.printf("Updated entityMatrix when calling update costs on entity id =  %d. \n", index);
            for (int i = 0; i < number_of_entities; i++) {
                for (int j = 0; j < number_of_entities; j++) {
                    System.out.print(nodeTable[i][j] + "\t");
                }
                System.out.println();
            }

//            System.out.println();
//            System.out.println("updated distances Arr");
//            for (int i = 0; i < numPackets; i ++){
//                System.out.print(costs[i] + "\t");
//            }
            System.out.println();
            System.out.print("printing out next best hash map : ");
            System.out.println(bestNextMap);
            System.out.println("-------------------------------------------------------");
        }

        return packetArr;
    }
    // This function is used by the simulator to retrieve the calculated routes
    // and costs from an entity. This is most useful at the end of the
    // simulation to collect the resulting routing state.
    //
    // Return Value: This function should return an array of (next_hop, cost)
    // tuples for _every_ entity in the network based on the entity's current
    // understanding of those costs. The array should be sorted such that the
    // first element of the array is the next hop and cost to entity index 0,
    // second element is to entity index 1, etc.
    public Pair<Integer, Integer>[] get_all_costs() {
//        implement main vector here pierce
//        for (int i = 0; i < number_of_entities; i++){
//            System.out.println();
//        }
//        if (debugCosts);

        return null;
    }

    // Return the best next hop for a packet with the given destination.
    //
    // Arguments:
    // - `destination`: The final destination of the packet.
    //
    // Return Value: The index of the best neighboring entity to use as the
    // next hop.
    public int forward_next_hop(int destination) {
        return bestNextMap.get(destination);
    }
}
