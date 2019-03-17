
// Class representing a packet being sent between neighboring nodes one-hop
// from each other.
public class Packet {
    private int source;
    private int destination;
    private int[] costs;

    // Create a packet containing DV cost information. Source is not required
    // when creating a packet as that will automatically be filled in by the
    // simulator.

    // Arguments:
    // - `destination`: The entity index of the recipient node.
    // - `costs`:       An array with a length equivalent to the number of
    //                  entities in the network. Each value in the array should
    //                  be the cost from the sender to the entity with that
    //                  index.

    // Return Value: None
    public Packet(int destination, int[] costs) {
        this.destination = destination;

        this.costs = new int[costs.length];
        for (int i = 0; i < costs.length; i++) {
            this.costs[i] = costs[i];
        }
    }

    // Return the source index of the packet.

    // Return Value: The entity index of the sender of this packet.
    public int get_source() {
        return this.source;
    }

    // Return the cost values sent in the packet.

    // Return Value: The array of costs transmitted by this packet.
    public int[] get_costs() {
        return this.costs;
    }

    public void set_costs(int index, int costs){
        if (index > this.costs.length) {
            throw new IllegalArgumentException();
        }
        this.costs[index] = costs;
    }

    // Manually set the source of the packet. This should only be called by the
    // simulator.
    public void set_source(int source) {
        this.source = source;
    }

    // Return the destination of the packet. This likely only needs to be
    // called by the simulator.
    public int get_destination() {
        return this.destination;
    }

    public String toString() {
        String str;
        str = this.source + " -> " + this.destination + "  containing: [";

        for (int i=0; i<this.costs.length; i++) {
            str += this.costs[i] + " ";
        }

        str += "]";

        return str;
    }
}
