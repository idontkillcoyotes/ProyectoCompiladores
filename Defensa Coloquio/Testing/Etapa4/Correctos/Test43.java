//atributos con valores
class A{
	private int x=0;
	private A a=new A();
	private int asd;
	A(){}	
	static void main(){}
	dynamic int met1(){return 0;}
	dynamic boolean met2(){return false;}
	dynamic char met3(){return '!';}
	dynamic String met4(){return "ola k ase";}
}
class B{
	dynamic A getA(){return new A();}
}

