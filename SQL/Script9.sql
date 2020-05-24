select *
from studenten s
where not exists (
  		select *
  		from hoeren h
  		where s.matrnr = h.matrnr);
