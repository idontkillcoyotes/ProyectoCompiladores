// Intento de redefinicion de un atributo con distinto modificador de visibilidad
class Main {
	static void main() {}
}
class B extends A {
	public int a;
}
class A {
	private int a;
}