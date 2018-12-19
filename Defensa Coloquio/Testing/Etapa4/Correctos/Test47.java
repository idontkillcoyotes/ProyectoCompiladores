//comparacion de clases de ambos lados
class Main{
	static void main(){
		A a;B b;C c;
		//a.met1()[2].met1()[2].met1()[3].x= 'z';
		if(a==a){}
		if(a!=b){}
		if(a==c){}
		if(b!=b){}
		if(b==c){}
		if(b!=a){}
		if(c==c){}
		if(c!=b){}
		if(c==a){}
		while(a==c){}
		while(c!=a){}
		while(null==a){}
		while(a!=null){}
	}
}
class A{
	public char x;
	dynamic A[] met1(){
		return new A[10];
	}
}
class B extends A{
}
class C extends B{
}