/* http://hyper-db.com/interface.html# */

select distinct s1.name, s1.matrnr
from studenten s1, studenten s2, hoeren h1, hoeren h2
where s1.matrnr = h1.matrnr
and s1.matrnr != s2.matrnr
and s2.matrnr = h2.matrnr
and h1.vorlnr = h2.vorlnr
and s2.Name = 'Fichte';
