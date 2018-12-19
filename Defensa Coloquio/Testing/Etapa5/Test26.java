//inicialización de atributos
class A{
	public int x,y=100;
	public char c,h='*';
	
	A(){
		(System.printSln("antes cod const"));
		(System.printIln(x));
		(System.printIln(y));
		(System.printCln(c));
		(System.printCln(h));
		this.x=0;
		c='+';
		(System.printSln("despues cod const"));
		(System.printIln(x));
		(System.printCln(c));
	}
	/*
	dynamic int m1(){
		return 100;
	}
	*/
	static void main(){
		A a1;
		(System.printSln("creo objeto:"));
		a1=new A();
		(System.printSln("despues de crearlo:"));
		(System.printIln(a1.x));
		(System.printIln(a1.y));
		(System.printCln(a1.c));
		(System.printCln(a1.h));
	}
}