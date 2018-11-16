// Referencia a un atributo publico encadenado a un metodo

class Main {

	static public void main() {
		A a;
		a = new A(3);
		(System.printI(a.getB().a));
	}

}

class A {

	private B b;

	A(int num) {
		b = new B(num);
	}

	dynamic public B getB() {
		return b;
	}

}

class B {

	public int a;

	
	B(int num) {
		a = num;
	}

}