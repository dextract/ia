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

Plano = [calcarMeia(dir),calcarSap(dir),calcarMeia(esq),calcarSap(esq)]
