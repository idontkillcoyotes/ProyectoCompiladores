// Acceso a un metodo no estatico desde un metodo estatico

class Main {

	static void main() {}

}

class A {	
	private int x;
	public int y;
	
	static void m1() {
		(getA());
	}

	dynamic A getA() {
		return new A();
	}

}
class B extends A{}