// Referencia a un atributo publico encadenado a un metodo estatico

class Main {

	static public void main() {
		A a;
		a = new A();
		(System.printI(a.m1().b));
	}

}

class A {

	static public B m1() {
		return new B(123);
	}

}

class B {

	public int b;

	B(int num) {
		b = num;
	}

}