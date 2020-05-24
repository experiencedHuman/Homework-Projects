/* http://hyper-db.com/interface.html# */
select v.vorlnr, v.titel
from studenten s join 
	 hoeren h on (s.matrnr = h.matrnr) join
     vorlesungen v on (h.vorlnr = v.vorlnr)
where s.name = 'Feuerbach';
