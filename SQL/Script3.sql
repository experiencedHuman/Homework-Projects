/* http://hyper-db.com/interface.html# */
select v.vorlnr, v.titel, count(h.matrnr) as hoerer
from 
	vorlesungen v left outer join 
	hoeren h on (v.vorlnr = h.vorlnr)
group by v.vorlnr, v.titel
order by hoerer desc;
