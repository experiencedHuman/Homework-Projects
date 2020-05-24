select s.*
from studenten s
where not exists(
  select v.*
  from vorlesungen v
  where not exists(
    select h.*
    from hoeren h
    where v.vorlnr = h.vorlnr and 
    	  s.matrnr = h.matrnr));
