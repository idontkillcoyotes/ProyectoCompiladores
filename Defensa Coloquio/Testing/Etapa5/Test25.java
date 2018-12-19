// Llamada a un metodo encadenado a un metodo estatico
class Main {
	static void main() {
		(m1().m1());
	}
	static  A m1() {
		return new A(87);
	}
}
class A {
	public int a;
	A(int num) {
		a = num;
	}
	dynamic void m1() {
		(System.printI(a));
	}

}