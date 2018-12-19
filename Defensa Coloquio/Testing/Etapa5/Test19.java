// Referencia a un atributo publico encadenado a un metodo estatico
class Main {
	static void main() {
		A a;
		a = new A();
		(System.printI(a.m1().b));
	}
}
class A {
	static B m1() {
		(System.printSln("m1B!"));
		return new B(420);
	}
}
class B {
	public int b;
	B(int num) {
		(System.printSln("const B"));
		b = num;
	}
}