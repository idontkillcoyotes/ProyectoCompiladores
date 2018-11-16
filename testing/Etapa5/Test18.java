// Llamada a un metodo encadenado a un constructor

class Main {

	static public void main() {
		(new A(123).m1());
	}

}

class A {

	private int a;

	A(int num) {
		a = num;
	}

	dynamic public void m1() {
		(System.printI(a));
	}

}