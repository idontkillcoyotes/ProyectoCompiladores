// Llamada a un metodo encadenado a un metodo estatico

class Main {

	static public void main() {
		(m1().m1());
	}

	static public A m1() {
		return new A(1);
	}

}

class A {

	public int a;

	A(int num) {
		a = num;
	}

	dynamic public void m1() {
		(System.printI(a));
	}

}