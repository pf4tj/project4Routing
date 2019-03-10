import java.io.*;
import java.util.*;

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
    private boolean debug = false;
    Pair<Integer, Integer> forwardingTable[];
    int [] distancevector;
    Vector<Vector<Integer>> networkVec = new Vector<Vector<Integer>>();
    int [] costVec;

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
        for (int i = 0; i < number_of_entities;i++){
            for (int j = 0; j < number_of_entities;j++){
                this.networkVec.put(9999);
            }
        }
        // Initialization here                                                                                                                                                                                  .
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
        for (int i = 0; i < neighbor_costs.size();i++){

        }
    }
//        int[] array = new int[2];
//        return array;
//    }

    // This function is called when a packet arrives for this entity.
    //
    // Arguments:
    // - `packet`: The incoming packet of type `Packet`.
    //
    // Return Value: This function should return an array of `Packet`s to be
    // sent from this entity (if any) to neighboring entities.
//    public Packet[] update(Packet packet) {
//        return
//    }

    // This function is used by the simulator to retrieve the calculated routes
    // and costs from an entity. This is most useful at the end of the
    // simulation to collect the resulting routing state.
    //
    // Return Value: This function should return an array of (next_hop, cost)
    // tuples for _every_ entity in the network based on the entity's current
    // understanding of those costs. The array should be sorted such that the
    // first element of the array is the next hop and cost to entity index 0,
    // second element is to entity index 1, etc.
//    public Pair<Integer, Integer>[] get_all_costs() {
//
//    }

    // Return the best next hop for a packet with the given destination.
    //
    // Arguments:
    // - `destination`: The final destination of the packet.
    //
    // Return Value: The index of the best neighboring entity to use as the
    // next hop.
//    public int forward_next_hop(int destination) {
//
//    }
}
