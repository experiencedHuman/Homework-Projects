/* http://hyper-db.com/interface.html# */

with view as(select v.titel 
from vorlesungen v, studenten s, hoeren h 
where s.name = 'Fichte' 
and s.matrnr = h.matrnr 
and h.vorlnr = v.vorlnr)

select * from view;
