// Incompatibilidad de tipos en una asignacion

class Main {

	static void main() {}

}

class A {

	dynamic void m1() {
		String str;
		str = 'x';
	}

}