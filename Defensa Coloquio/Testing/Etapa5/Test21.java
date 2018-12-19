// Construccion de un objeto con el constructor predefinido
class Main {
	static void main() {
		A a;
		a = new A();
		(a.m1());
		a.a = 1;
		a.b = 2;
		(a.m1());
	}
}

class A {
	public int a;
	public int b;
	dynamic void m1() {
		(System.printSln("m1A!~~"));
		(System.printIln(a));
		(System.printIln(b));
	}
}