// Constructor finalizado con una sentencia de retorno

class Main {

	static void main() {
		int a1, b1;
		a1 = 3;
		b1 = 5;
		A a;
		a = new A(a1, b1);
		(System.printIln(a.a));
		(System.printIln(a.b));
	}

}

class A {

	public int a;
	public int b;

	A(int a1, int b1) {
		a = 1;
		b = 1;
		a = a1;
		if (true) {
			return;
		}
		b = b1;
	}

}