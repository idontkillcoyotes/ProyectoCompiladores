// Metodo void finalizado con una sentencia de retorno

class Main {

	static void main() {
		(m1());
	}

	static void m1() {
		int a;
		a = 3;
		(System.printI(a));
		if (true) {
			return;
		}
		int b;
		b = 1;
		(System.printI(b));
	}

}