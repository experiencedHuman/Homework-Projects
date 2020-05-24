with tot_sws as (
  select distinct s.name, sum(v.sws) as totalsws
  from studenten s join 
  	   hoeren h on (s.matrnr = h.matrnr) join
  	   vorlesungen v on (h.vorlnr = v.vorlnr)
  group by s.name
)
select s.name, s.matrnr
from studenten s, tot_sws ts
where s.name = ts.name and
	ts.totalsws >= 5;
