package gen;

import com.google.common.base.MoreObjects;
import net.minecraft.util.Mth;

import javax.annotation.concurrent.Immutable;

@Immutable
public class Vec3i implements Comparable<Vec3i> {
	/**
	 * An immutable vector with zero as all coordinates.
	 */
	public static final Vec3i ZERO = new Vec3i(0, 0, 0);
	private int x;
	private int y;
	private int z;

	public Vec3i(int pX, int pY, int pZ) {
		this.x = pX;
		this.y = pY;
		this.z = pZ;
	}

	public boolean equals(Object pOther) {
		if (this == pOther) {
			return true;
		} else if (!(pOther instanceof Vec3i)) {
			return false;
		} else {
			Vec3i vec3i = (Vec3i) pOther;
			if (this.getX() != vec3i.getX()) {
				return false;
			} else if (this.getY() != vec3i.getY()) {
				return false;
			} else {
				return this.getZ() == vec3i.getZ();
			}
		}
	}

	public int hashCode() {
		return (this.getY() + this.getZ() * 31) * 31 + this.getX();
	}

	public int compareTo(Vec3i pOther) {
		if (this.getY() == pOther.getY()) {
			return this.getZ() == pOther.getZ() ? this.getX() - pOther.getX() : this.getZ() - pOther.getZ();
		} else {
			return this.getY() - pOther.getY();
		}
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getZ() {
		return this.z;
	}

	protected Vec3i setX(int pX) {
		this.x = pX;
		return this;
	}

	protected Vec3i setY(int pY) {
		this.y = pY;
		return this;
	}

	protected Vec3i setZ(int pZ) {
		this.z = pZ;
		return this;
	}

	public Vec3i offset(int pDx, int pDy, int pDz) {
		return pDx == 0 && pDy == 0 && pDz == 0 ? this : new Vec3i(this.getX() + pDx, this.getY() + pDy, this.getZ() + pDz);
	}

	public Vec3i offset(Vec3i pVector) {
		return this.offset(pVector.getX(), pVector.getY(), pVector.getZ());
	}

	public Vec3i subtract(Vec3i pVector) {
		return this.offset(-pVector.getX(), -pVector.getY(), -pVector.getZ());
	}

	public Vec3i multiply(int pScalar) {
		if (pScalar == 1) {
			return this;
		} else {
			return pScalar == 0 ? ZERO : new Vec3i(this.getX() * pScalar, this.getY() * pScalar, this.getZ() * pScalar);
		}
	}

	/**
	 * Calculate the cross product of this and the given Vector
	 */
	public Vec3i cross(Vec3i pVector) {
		return new Vec3i(this.getY() * pVector.getZ() - this.getZ() * pVector.getY(), this.getZ() * pVector.getX() - this.getX() * pVector.getZ(), this.getX() * pVector.getY() - this.getY() * pVector.getX());
	}

	public boolean closerThan(Vec3i pVector, double pDistance) {
		return this.distSqr(pVector) < Mth.square(pDistance);
	}


	/**
	 * Calculate squared distance to the given Vector
	 */
	public double distSqr(Vec3i pVector) {
		return this.distToLowCornerSqr((double) pVector.getX(), (double) pVector.getY(), (double) pVector.getZ());
	}

	public double distToCenterSqr(double pX, double pY, double pZ) {
		double d0 = (double) this.getX() + 0.5D - pX;
		double d1 = (double) this.getY() + 0.5D - pY;
		double d2 = (double) this.getZ() + 0.5D - pZ;
		return d0 * d0 + d1 * d1 + d2 * d2;
	}

	public double distToLowCornerSqr(double pX, double pY, double pZ) {
		double d0 = (double) this.getX() - pX;
		double d1 = (double) this.getY() - pY;
		double d2 = (double) this.getZ() - pZ;
		return d0 * d0 + d1 * d1 + d2 * d2;
	}

	public int distManhattan(Vec3i pVector) {
		float f = (float) Math.abs(pVector.getX() - this.getX());
		float f1 = (float) Math.abs(pVector.getY() - this.getY());
		float f2 = (float) Math.abs(pVector.getZ() - this.getZ());
		return (int) (f + f1 + f2);
	}

	public String toString() {
		return MoreObjects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).toString();
	}


	public static long asLong(int pX, int pY, int pZ) {
		long i = 0L;
		i |= ((long)pX & 4194303L) << 42;
		i |= ((long)pY & 1048575L) << 0;
		return i | ((long)pZ & 4194303L) << 20;
	}

	public long asLong() {
		return asLong(this.x, this.y, this.z);
	}


	public String toShortString() {
		return this.getX() + ", " + this.getY() + ", " + this.getZ();
	}
}