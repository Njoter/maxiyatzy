

public class Terning {
	private static final int SIDER = 6;
	private int verdi;
	private boolean beholdt = false;

	public static int getSider() {
		return SIDER;
	}

	public int getVerdi() {
		return this.verdi;
	}

	public boolean getBeholdt() {
		return this.beholdt;
	}

	public void setBeholdt(boolean beholdt) {
		this.beholdt = beholdt;	
	}

	public void rull() {
		verdi = (int) (Math.random() * SIDER) + 1;
	}

	public String toString() {
		return "[" + verdi + "]";
	}
}
