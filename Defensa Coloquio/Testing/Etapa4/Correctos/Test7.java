// Metodo con una llamada a metodo valida

class Main {

	static  void main() {}

}

class Clase {

	dynamic void m1() {
		(m2(1, 'a', "str"));
	}

	dynamic void m2(int a, char b, String c) {}

}