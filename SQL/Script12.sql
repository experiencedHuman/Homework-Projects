select s.matrnr, s.name
from studenten s
where not exists (
  select *
  from hoeren h
  where s.matrnr = h.matrnr and 
  not exists (
    select *
    from pruefen p
    where p.matrnr = h.matrnr and
    	p.note > 4));
