import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

// The main simulator responsible for scheduling and delivering packets
// sent between entities in the network.
public class NetworkSimulator {
    // Debugging level for `printf()` outputs during execution.
    private int debug;

    // List of all node entities in the network.
    private Entity entities[];

    // Queue of packets to be sent in the network.
    private Vector<Pair<Double, Packet>> packets;

    // Costs of neighbor links.
    private Pair<Integer, Integer> entity_links[][];

    // Current simulator time.
    private double time;

    // Random number generator to schedule packet arrivals.
    private Random rand;

    // Create and configure the simulator.
    //
    // Arguments:
    // - `entity_links`: Array of entities containing (destination, cost)
    //                   tuples for all valid links in the network. See
    //                   project.py for examples.
    // - `seed`:         Number to seed the random number generator with.
    // - `debug`:        Debugging level (0-3).
    public NetworkSimulator(Pair<Integer, Integer> entity_links[][], int seed, int debug) {
        int number_of_entities = entity_links.length;

        this.debug = debug;

        // Initialize queue of packets to be sent in the simulator.
        this.packets = new Vector();

        // Initialize array for list of all entities in the network.
        this.entities = new Entity[number_of_entities];

        // Time starts at 0.0.
        this.time = 0.0;

        // Setup random seed.
        this.rand = new Random(seed);

        // Save entity links so we can verify packet transmissions.
        this.entity_links = entity_links;

        // Actually create all of the entities.
        for (int i=0; i<number_of_entities; i++) {
            this.entities[i] = new Entity(i, number_of_entities);
        }

        // Supply each entity with immediate link costs.
        for (int i=0; i<number_of_entities; i++) {
            Packet[] to_send = this.entities[i].initialize_costs(entity_links[i]);
            this.to_layer2(i, to_send);
        }
    }

    // Internal function used to schedule packet transmissions.
    private void to_layer2(int entity_index, Packet[] packets) {
        for (int pi=0; pi<packets.length; pi++) {
            Packet packet = packets[pi];
            // Verify packet is valid.

            // Check the length of the costs value
            if (packet.get_costs().length != this.entities.length) {
                System.out.println("ERROR: Invalid packet with incorrect number of cost values.");
                System.exit(-1);
            }

            // Check that the destination is valid (i.e. it is a neighbor
            // of the sending node).
            boolean valid = false;
            for (int i=0; i<this.entity_links[entity_index].length; i++) {
                Pair<Integer, Integer> link = this.entity_links[entity_index][i];
                if (packet.get_destination() == link.x) {
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                System.out.println("ERROR: Invalid packet with destination not one-hop.");
                System.exit(-2);
            }

            // Check that the packet is not being sent to the sender.
            if (packet.get_destination() == entity_index) {
                System.out.println("ERROR: Source and destination the same.");
                System.exit(-3);
            }

            // Set the packet source.
            packet.set_source(entity_index);

            // Now actually send the packet by adding it to the queue with the
            // correct send time.
            if (this.debug > 2) {
                System.out.println("Sending Packet " + packet);
            }

            // This packet should arrive sometime after the last packet with the same
            // source and destination. So, we need to find the latest packet with the
            // same source and destination.
            double latest_time = this.time;
            for (int i=0; i<this.packets.size(); i++) {
                Pair<Double, Packet> packet_in_queue = this.packets.get(i);
                Packet past_packet = packet_in_queue.y;
                double packet_time = packet_in_queue.x;

                if (packet.get_source() == past_packet.get_source() &&
                        packet.get_destination() == past_packet.get_destination()) {
                    latest_time = packet_time;
                }
            }

            // Now we can calculate some time in the future for the packet time.
            double arrival_time = latest_time + 1.0 + (this.rand.nextDouble() * 9.0);

            // Now we need to insert the packet into the array of packets correctly.
            int index = 0;
            for (int i=0; i<this.packets.size(); i++) {
                Double packet_time = this.packets.get(i).x;
                if (arrival_time > packet_time) {
                    index = i + 1;
                }
            }
            this.packets.add(index, new Pair<Double, Packet>(arrival_time, packet));
        }
    }

    // Run the actual simulator and stop when there are no more packets left.
    public void run() {
        while (true) {
            if (this.packets.isEmpty()) {
                break;
            }

            // Get the earliest packet in the system
            Pair<Double, Packet> earliest = this.packets.remove(0);
            Double packet_time = earliest.x;
            Packet next_packet = earliest.y;

            if (this.debug > 1) {
                System.out.println("");
                System.out.println("Handling packet at time=" + packet_time);
                System.out.println("    " + next_packet);
            }

            // Update our simulator time to when this packet happens
            this.time = packet_time;

            // Pass the packet to the correct entity
            int destination = next_packet.get_destination();
            Packet[] to_send = this.entities[destination].update(next_packet);
            this.to_layer2(destination, to_send);
        }

        // If we get here then we are done with the simulation
        if (this.debug > 0) {
            System.out.println("Simulation finished at time t=" + this.time);
        }
    }

    // Print a forwarding table for a given entity for every destination in the
    // network. Show the cost to that destination and the next hop.
    //
    // For example, an example forwarding table for entity 0 is:
    //
    //     E0 | Cost | Next Hop
    //     ---+------+---------
    //     0  |    0 |        0
    //     1  |    1 |        1
    //     2  |    2 |        1
    //     3  |    4 |        1
    //
    // The first column contains the destinations.
    //
    // Arguments:
    // - `entity_index`: The node to print the forwarding table for.
    public void display_forwarding_table(int entity_index) {
        Pair<Integer, Integer> final_costs[] = this.entities[entity_index].get_all_costs();

        System.out.println("E"+entity_index+" | Cost | Next Hop");
        System.out.println("---+------+---------");
        for (int i=0; i<this.entities.length; i++) {
            System.out.println(i + "  | " + String.format("%4s", final_costs[i].y)
                    + " | " + String.format("%8s", final_costs[i].x));
        }
    }

    // Generate an array of hops, including the source and destination, for a
    // packet traversing with the lowest cost through the network.
    public int[] route_packet(int source, int destination) {
        int[] hops = new int[this.entity_links.length];
        hops[0] = source;

        int index = 1;
        int current_hop = source;
        while (current_hop != destination) {
            hops[index] = this.entities[current_hop].forward_next_hop(destination);
            current_hop = hops[index];
            index += 1;
        }
        return Arrays.copyOfRange(hops, 0, index);
    }
}