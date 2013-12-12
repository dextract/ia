%Estados:
%localizacao_robot(s1, s2, s3); localizacao_objecto([b, s]); agarra([], b1, b2, b3);  existe_porta([s1, s2]);
%
%accao(nome : andar(S,SN),
%condicoes  : [localizacao_robot(S),existe_porta(S,SN)],
%efeitos    : [localizacao_robot(SN),-localizacao_robot(S)],
%restricoes : []).
%
%accao(nome : agarrar(S,B),
%condicoes  : [localizacao_robot(S),agarra([]),localizacao_objecto(B,S)],
%efeitos    : [agarra(B),-localizacao_objecto(B,S)],
%restricoes : []).
%
%accao(nome : largar(S,B),
%condicoes  : [agarra(B),localizacao_robot(S)],
%efeitos    : [-agarra(B),localizacao_objecto(B,S)],
%restricoes : []).
%
%inicial([localizacao_robot(s1),localizacao_objecto(b1,s1),localizacao_objecto(b2,s2),localizacao_objecto(b3,s3),agarra([]),existe_porta(s1,s2),existe_porta(s2,s3)]).
%
%objectivos([localizacao_objecto(b1,s3),localizacao_objecto(b2,s3),localizacao_objecto(b3,s3)]).


accao(nome : calcarSap(Pe), 

 condicoes : [meia(Pe)], 

   efeitos : [sapato(Pe)], 

restricoes : []). 

 

accao(nome : calcarMeia(Pe),

 condicoes : [],

   efeitos : [meia(Pe)],

restricoes : []). 

 

inicial([]). 

 
objectivos([sapato(esq),sapato(dir)]).

%[calcarMeia(dir),calcarSap(dir),calcarMeia(esq),calcarSap(esq)]

append([], X, X).
append([X | Y], Z, [X | W]) :- append(Y, Z, W).

procCond([],_).
procCond([C|LC],S) :- member(C,S), procCond(LC,S).

procEfeitos([],S,S).
procEfeitos([-E|LE],S,S2) :- delete(S,E,S1), procEfeitos(LE,S1,S2).
procEfeitos([E|LE],S,S2) :- append([E],S,S1), procEfeitos(LE,S1,S2).

procRestr(_,_).

procObjectivos([],_).
procObjectivos([O|LO],Fn) :- member(O,Fn), procObjectivos(LO,Fn).

satisfiedGoal(Fn) :- objectivos(Ob), procObjectivos(Ob, Fn).

updateExec([],[]).
updateExec([E|LE],El,[E1|LE1]) :- append(Exec,[El],Exec1).


testPlan([],I,Exec2,I).
testPlan([A|LA],I,Exec,Fn) :- accao(nome : A, condicoes : LC, efeitos : LE, restricoes : LR), append(Exec,[A],Exec1), procCond(LC,I), procEfeitos(LE,I,I1), append(Exec1,[I1],Exec2), procRestr(LR,I), testPlan(LA,I1,Exec2,Fn).


writeExec([]) :- objectivos(O), write('\nGoal '), write(O), write(' satisfied').
writeExec([E|LE]) :- write('\n'), write(E), writeExec(LE).

planExecute(P) :- testPlan(P,E), writeExec(E).

testPlan(Plan,[I|Exec]) :-
   inicial(I),                   % I = situação inicial  
   testPlan(Plan,I,Exec,Fn),     % Testa o plano linear Plan e executa-o a partir da                                   
				 % situação inicial I e devolve o resultado da execução em Exec
				 % A situação final da execução é devolvida em Fn.       
   satisfiedGoal(Fn).            % Verifica se os predicados do objectivo estão presentes em Fn.   
