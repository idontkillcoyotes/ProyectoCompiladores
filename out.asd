.code 
push main ;cargo main
call ;llamo a main
halt ;termino

malloc:
loadfp ;Inicialización unidad 
loadsp 
storefp ;Finaliza inicialización del RA
LOADHL ;hl
DUP ;hl
PUSH 1 ;1
ADD ;hl+1
STORE 4 ;Guarda el resultado (un puntero a la primer celda de la región de memoria)
LOAD 3 ;Carga la cantidad de celdas a alojar (parámetro que debe ser positivo)
ADD
STOREHL ;Mueve el heap limit (hl). Expande el heap
STOREFP
RET 1	;Retorna eliminando el parámetro






.data
VT_A: DW A_m1_1



.code


Aconst0:
loadfp			;inicio de ra (econst A0)
loadsp			; (econst A0)
storefp			;fin de ra (econst A0)
rmem 0			;reservo espacio para var locales (econst A0)
;comienzo a inicializar atributos (econst A0)
;finalizo inicializacion de atributos (econst A0)
fmem 0			;libero espacio para var locales (econst A0)
storefp			;retorno (econst A0)
ret 1			;retorno con cant parametros + 1 (econst A0)

main:
loadfp			;inicio de ra (emetodo main)
loadsp			;(emetodo main)
storefp			;fin de ra (emetodo main)
rmem 2			;reservo espacio para var locales (emetodo main)
nop			;(nododecvars)
rmem 1			;reservo espacio para retorno de malloc (nodoconst)
push 1			;apilo tamaño de cir (nodoconst)
push malloc			;cargo dir (nodoconst)
call			;hago llamada (nodoconst)
dup			;duplico direccion de cir (nodoconst)
push VT_A			;cargo direccion de vt (nodoconst)
storeref 0			;guardo en la dir 0 del CIR, la dir de la VT (nodoconst)
dup			;duplico dir cir? (nodoconst)
push Aconst0			;cargo dir de const (nodoconst)
call			;llamo const (nodoconst)
store 0			;guardo var local o param (nodovar)
rmem 1			;guardo espacio para retorno (nodollamest)
push -1			;apilo el this ficticio (nodollamest)
push System_readI_0			;apilo direccion (nodollamest)
call			;hago llamada (nodollamest)
store -1			;guardo valor en var local (nododecvars)
nop			;(nododecvars)
load 0			;cargo var local o param (nodovar)
load -1			;cargo var local o param (nodovar)
swap			;(nodollamencad)
dup			;duplico this (nodollamencad)
loadref 0			;cargo vt (nodollamencad)
loadref 0			;cargo dir de metodo con el offset (nodollamencad)
call			;llamo (nodollamencad)
fmem 2			;libero espacio de var locales (emetodo main)
storefp			;retorno (emetodo main)
ret 1			;retorno (emetodo main)

A_m1_1:
loadfp			;inicio de ra (emetodo m1)
loadsp			;(emetodo m1)
storefp			;fin de ra (emetodo m1)
rmem 1			;reservo espacio para var locales (emetodo m1)
push 0			;pusheo literal entero
store 0			;guardo valor en var local (nododecvars)
nop			;(nododecvars)
push -1			;apilo el this ficticio (nodollamest)
.data
str_1124: dw "en m1: ", 0 ;defino literal string
.code
push str_1124			;pusheo dir literal string
swap			;swapeo this con argumento (nodollamest)
push System_printS_1			;apilo direccion (nodollamest)
call			;hago llamada (nodollamest)
push -1			;apilo el this ficticio (nodollamest)
load 4			;cargo var local o param (nodovar)
swap			;swapeo this con argumento (nodollamest)
push System_printIln_1			;apilo direccion (nodollamest)
call			;hago llamada (nodollamest)
while_1A_m1_1:			;genero label inicio while (nodowhile)
load 0			;cargo var local o param (nodovar)
load 4			;cargo var local o param (nodovar)
le ;operador menor igual
bf endwhile_1A_m1_1			;si condicion false salto a fin (nodowhile)
push -1			;apilo el this ficticio (nodollamest)
load 0			;cargo var local o param (nodovar)
swap			;swapeo this con argumento (nodollamest)
push System_printIln_1			;apilo direccion (nodollamest)
call			;hago llamada (nodollamest)
load 0			;cargo var local o param (nodovar)
push 1			;pusheo literal entero
add			;operador suma
store 0			;guardo var local o param (nodovar)
jump while_1A_m1_1			;salto al principio (nodowhile)
endwhile_1A_m1_1:			;genero label fin while (nodowhile)
fmem 1			;libero espacio de var locales (emetodo m1)
storefp			;retorno (emetodo m1)
ret 2			;retorno (emetodo m1)



System_read_0:
loadfp
loadsp
storefp
read
store 4
storefp
ret 1

System_readI_0:
loadfp
loadsp
storefp
read
push 48
sub
store 4
storefp
ret 1

System_println_0:
loadfp
loadsp
storefp
prnln
storefp
ret 1

System_printI_1:
loadfp
loadsp
storefp
load 4
iprint
storefp
ret 2

System_printB_1:
loadfp
loadsp
storefp
load 4
bprint
storefp
ret 2

System_printC_1:
loadfp
loadsp
storefp
load 4
cprint
storefp
ret 2

System_printS_1:
loadfp
loadsp
storefp
load 4
sprint
storefp
ret 2

System_printIln_1:
loadfp
loadsp
storefp
load 4
iprint
prnln
storefp
ret 2

System_printBln_1:
loadfp
loadsp
storefp
load 4
bprint
prnln
storefp
ret 2

System_printCln_1:
loadfp
loadsp
storefp
load 4
cprint
prnln
storefp
ret 2

System_printSln_1:
loadfp
loadsp
storefp
load 4
sprint
prnln
storefp
ret 2



