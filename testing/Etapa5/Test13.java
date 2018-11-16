// Referencia a un atributo publico de una clase

class Main {

	static public void main() {
		A a;
		a = new A(1, 2);
		(System.printIln(a.a));
		(System.printIln(a.b));
	}

}

class A {

	public int a;
	public int b;

	A(int a1, int b1) {
		a = a1;
		b = b1;
	}

}