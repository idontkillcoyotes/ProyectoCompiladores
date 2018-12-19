// Llamada a un metodo encadenado a un constructor
class Main {
	static void main() {
		(new A(322).m1());
	}
}
class A {
	private int a;
	A(int num) {
		a = num;
	}
	dynamic void m1() {
		(System.printI(a));
	}
}