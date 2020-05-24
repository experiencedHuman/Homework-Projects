/* http://hyper-db.com/interface.html# */
select v2.titel
from vorlesungen v1, vorlesungen v2, voraussetzen b
where v1.titel = 'Wissenschaftstheorie' and
	v1.vorlnr = b.nachfolger and
    v2.vorlnr = b.vorgaenger;
