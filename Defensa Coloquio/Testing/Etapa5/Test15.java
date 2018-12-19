// Llamada a un metodo encadenado a otro
class Main {
	static void main() {
		A a;
		a = new A();
		(a.getB().m1());
	}
}
class A {
	private B b=new B();
	A() {}
	dynamic B getB() {
		return b;
	}
}
class B {
	dynamic void m1() {
		(System.printSln("m1 de B!"));
	}
}