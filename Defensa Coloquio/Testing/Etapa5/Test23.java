// Referencia a this
class Main {
	static void main() {
		(System.printSln("main"));
		A a;
		a = new A();
		(System.printI(a.a));
	}
}

class A {
	public int a;
	A() {
		(System.printSln("const A"));
		a = this.m1();
	}
	dynamic int m1() {
		(System.printSln("m1A"));
		return 57;
	}


}