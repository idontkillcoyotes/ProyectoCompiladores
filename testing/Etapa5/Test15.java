// Llamada a un metodo encadenado a otro

class Main {

	static public void main() {
		A a;
		a = new A();
		(a.getB().m1());
	}

}

class A {

	private B b;

	A() {
		b = new B();
	}

	dynamic public B getB() {
		return b;
	}

}

class B {

	dynamic public void m1() {
		(System.printIln(1));
	}

}