// Atributo heredado inaccesible
class Main {
	static void main() {}
}
class A {
	private int a;
}
class B extends A {
	private int z;
	dynamic void m1() {
		int b;
		b = a + z;
	}
}
