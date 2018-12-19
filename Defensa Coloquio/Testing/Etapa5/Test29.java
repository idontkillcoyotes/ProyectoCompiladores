//test 1 errores entrega(
class A{
	dynamic void m1(){
		(System.printI(1));
	}
}
class B{
	dynamic A nuevoA(){
		return new A();
	}
	static void main(){
		B x;
		x=new B();
		((x.nuevoA()).m1());
	}
}