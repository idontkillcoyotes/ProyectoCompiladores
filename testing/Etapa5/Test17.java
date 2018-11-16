// Llamada a un metodo estatico

class Main {

	static public void main() {
		(A.m1());
		(A.m2(2));
		(A.m3());
	}

}

class A {

	static public void m1() {
		(System.printI(1));
	}

	static public void m2(int num) {
		(System.printI(num));
	}

	static public void m3() {
		(System.printI(3));
	}

}