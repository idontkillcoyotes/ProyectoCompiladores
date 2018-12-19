// Llamada a un metodo estatico

class Main {

	static void main() {
		(A.m1());
		(A.m2(27));
		(A.m3());
	}

}

class A {

	static void m1() {
		(System.printIln(1));
	}

	static void m2(int num) {
		(System.printIln(num));
	}

	static void m3() {
		(System.printIln(3));
	}

}