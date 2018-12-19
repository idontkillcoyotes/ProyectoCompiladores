// Declaracion de variables locales con el mismo nombre en distintos bloques

class Main {

	static  void main() {}

}

class A {

	dynamic void m1() {
		if (true) {
			int a;
			a = 1;
		}
		while (true) {
			int a;
			a = 1;
		}
	}

}