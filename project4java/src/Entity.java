import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Arrays;

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
    int distancesArr[];
    int[][] entityMatrix;
    public static final int maxVal = 999;
    public boolean debug = true;
    public  boolean debugConstructor = false;
    public boolean debugInit = true;
    public boolean debugUpdate = true;
    public boolean debugCosts = false;
    public boolean debugForward = false;


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
        this.distancesArr = new int[number_of_entities];
        this.entityMatrix = new int[number_of_entities][number_of_entities];
        for (int i = 0; i < number_of_entities; i++) {
            if (i==(index-1)) distancesArr[index-1] = 0;
            else distancesArr[i] = maxVal;
            // it will run you through the lines
            for (int j = 0; j < number_of_entities; j++) {    // this will run you through each cell in the raw selected
                if (i == j) entityMatrix[i][j] = 0;
                else entityMatrix[i][j] = maxVal;
            }
        }
        if (debugConstructor) {
            System.out.println("Entity DEBUG SET TO TRUE");

            System.out.printf("Initialized distancesVector for entity id = %d \n", index);
            for (int i = 0; i < number_of_entities; ++i){
                System.out.print(distancesArr[i] + "\t");
            }
            System.out.println();
            System.out.printf("Initialized entityMatrix for entity id = %d \n", index);
            for (int i = 0; i < number_of_entities; i++) {
//            if (i == 0) System.out.print(i + " ");
                for (int j = 0; j < number_of_entities; j++) {
                    System.out.print(entityMatrix[i][j] + "\t");
                }
                System.out.println();
            }
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
        int[] costArr = new int[numPackets];
        Packet[] packetArr = new Packet[numPackets];
        for (int i = 0; i < numPackets; i++) {
            if (i == (index-1)) continue;
            if ((neighbor_costs[i].y) < 999) {
                this.entityMatrix[index - 1][i] = (neighbor_costs[i].y);
            }
            if ((neighbor_costs[i].y) < 999) {
                this.distancesArr[i] = (neighbor_costs[i].y);
            }

            costArr[i] = neighbor_costs[i].y;
        }

        for (int i = 0; i < numPackets; i++) {
            Packet p = new Packet(neighbor_costs[i].x, costArr);
            p.set_source(this.index);
            packetArr[i] = p;
        }

        if (debugInit) {
            System.out.println();
            for (int i = 0; i < numPackets; i++) {
                System.out.println("printing out neighbour costs : entity idx = " + neighbor_costs[i].x + " cost = " +  neighbor_costs[i].y);
            }
            System.out.println();
            System.out.println();
            System.out.printf("Updated entityMatrix when calling initialize costs on entity id =  %d. \n", index);
            for (int i = 0; i < number_of_entities; i++) {
                for (int j = 0; j < number_of_entities; j++) {
                    System.out.print(entityMatrix[i][j] + "\t");
                }
                System.out.println();
            }
            System.out.println();
            System.out.printf("Printing out costArr values \n", index);
            for (int i = 0; i < number_of_entities; i++){
                System.out.print(distancesArr[i] + " , ");
            }
            System.out.println();
            System.out.println("printing out packets to send");
            for (int i = 0; i < number_of_entities; i++) {
                System.out.print(packetArr[i] + " , ");
            }

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
        int numPackets = packet.get_costs().length;
        int[] costArr = new int[numPackets];
        int[] destArr = new int [number_of_entities];
        Packet[] packetArr = new Packet[numPackets];
        for (int src = 0; src < numPackets; src++) {
            destArr[src] = src + 1;
            for (int dest = 0; dest < numPackets; dest++) {
                if (src == dest) continue;
                else if (this.entityMatrix[src][dest] != this.maxVal){
                     if (distancesArr[dest] > (distancesArr[src] + this.entityMatrix[src][dest])){
                         distancesArr[dest] = distancesArr[src] + this.entityMatrix[src][dest];
                     }
                }
            }
        }

        for (int i = 0; i < numPackets; i++) {
            Packet p = new Packet(destArr[i], distancesArr);
            p.set_source(this.index);
            packetArr[i] = p;
        }

        if (debugUpdate) {
            System.out.println();
            System.out.printf("Updated entityMatrix when calling update costs on entity id =  %d. \n", index);
            for (int i = 0; i < number_of_entities; i++) {
                for (int j = 0; j < number_of_entities; j++) {
                    System.out.print(entityMatrix[i][j] + "\t");
                }
                System.out.println();
            }
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
        for (int i = 0; i < number_of_entities; i++){
            System.out.println();
        }
        if (debugCosts);

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

        if (debugForward);
        return 1;
    }
}
