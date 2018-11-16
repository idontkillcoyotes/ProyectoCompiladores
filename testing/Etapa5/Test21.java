// Construccion de un objeto con el constructor predefinido

class Main {

	static public void main() {
		A a;
		a = new A();
		a.a = 1;
		a.b = 2;
		(a.m1());
	}

}

class A {

	public int a;
	public int b;

	dynamic public void m1() {
		(System.printIln(a));
		(System.printIln(b));
	}

}