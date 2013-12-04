% Estados:
-
% Localizacao_robot(S1, S2, S3); Localizacao_objecto(B, S); Agarra([], B1, B2, B3);  Existe_porta(S1, S2);

% Funcao sucessor:

accao(nome : andar(S,SN),
condicoes  : [Localizacao_robot(S),Existe_porta(S,SN)],
efeitos    : [Localizacao_robot(SN),-Localizacao_robot(S)],
restricoes : []).

accao(nome : agarrar(S,B),
condicoes  : [Localizacao_robot(S),Agarra([]),Localizacao_objecto(B,S)],
efeitos    : [Agarra(B),-Localizacao_objecto(B,S)],
restricoes : []).

accao(nome : largar(S,B),
condicoes  : [Agarra(B),Localizacao_robot(S)],
efeitos    : [-Agarra(B),Localizacao_objecto(B,S)],
restricoes : []).


% Estado inicial:

inicial([Localizacao_robot(S1),Localizacao_objecto(B1,S1),Localizacao_objecto(B2,S2),Localizacao_objecto(B3,S3),Agarrar([]),Existe_porta(S1,S2),Existe_porta(S2,S3)]).


% Teste objectivo:

objectivos([Localizacao_objecto(B1,S3),Localizacao_objecto(B2,S3),Localizacao_objecto(B3,S3)]).



Plan = [agarrar(s1, b1),andar(s1, s2),andar(s2,s3),largar(s3,b1),andar(s3,s2),agarrar(s2,b2),andar(s2,s3),largar(s3,b2)]


procCond(C|LC,S) :- member(C,S), procCond(LC,S).

procEfeitos([],S,S).
procEfeitos([-E|LE],S,S1) :- !, delete(S,E,S1), procEfeitos(LE,S1,S2).
procEfeitos(E|LE,S,S1) :- append([E],S,S1), procEfeitos(LE,S1,S2).
	
procRestr([],S) :- true.

testPlan([A|LA],I,Exec,Fn) :-
	accao(nome : A, condicoes : LC, efeitos : LE, restricoes : LR), procCond(LC,I), procEfeitos(LE,I,I1), procRestr(LR,I),
	testPlan(LA,I1,Exec,Fn).


planExecute(P) :- testPlan(P,E), writeExec(E).
testPlan(Plan,[I|Exec]) :-
   inicial(I),                  % I = situação inicial  
   testPlan(Plan,I,Exec,Fn),    % Testa o plano linear Plan e executa-o a partir da                                   
                                % situação inicial I e devolve o resultado da execução em Exec
                                % A situação final da execução é devolvida em Fn.       
   satisfiedGoal(Fn).           % Verifica se os predicados do objectivo estão presentes em Fn.   
