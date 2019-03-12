// Utility class for creating a 2-tuple.
public class Pair<X, Y> {
	public final X x;
	public final Y y;

	public Pair(X x, Y y) {
		this.x = x;
		this.y = y;
	}
//	pierce
	public String toString(){//overriding the toString() method
		return "x value =  " + x + ", y value = " + y;
	}
}
