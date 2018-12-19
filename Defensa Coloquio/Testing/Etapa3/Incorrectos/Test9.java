// Herencia circular
class Main {
	static void main() {}
}
class A extends C {
}
class B extends A {
}
class C extends B {
}