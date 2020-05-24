/* http://hyper-db.com/interface.html# */
with pruefenx1(matrnr,vorlnr,persnr,note) as (
  select * from pruefen
  union all
  values
  		(25501,5049,2126,1),
  		(26120,5001,2137,1),
  		(26120,5043,5126,3),
  		(26120,5052,2126,4),
  		(26120,4630,2137,1)
)

select
	s.matrnr,
    s.name,
    sum(p.note * v.sws) / sum(v.sws) as durschnitt
from studenten s, pruefenx1 p, vorlesungen v
where s.matrnr = p.matrnr 
and p.vorlnr = v.vorlnr
group by s.matrnr, s.name;
