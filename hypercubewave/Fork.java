/** Ben F Rayfield offers this software opensource GNU GPL 2+ */
package hypercubewave;

/** A dimension to display as either wave or average forked in hypercube
which tends to look like quantum crystals.
*/
public class Fork{
	
	public final float dy, dx;
	
	public final float multA, multB;
	
	public Fork(float dy, float dx, float multA, float multB){
		this.dy = dy;
		this.dx = dx;
		this.multA = multA;
		this.multB = multB;
	}
	
	public Fork setDy(float dy){
		return new Fork(dy, dx, multA, multB);
	}
	
	public Fork setDx(float dx){
		return new Fork(dy, dx, multA, multB);
	}
	
	public Fork setMultA(float multA){
		return new Fork(dy, dx, multA, multB);
	}
	
	public Fork setMultB(float multB){
		return new Fork(dy, dx, multA, multB);
	}

}
