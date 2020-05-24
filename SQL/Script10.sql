select p.name, p.persnr
from professoren p
where exists (
  select *
  from vorlesungen v
  where p.persnr = v.vorlnr and
  exists (
    select *
    from hoeren h
    where h.vorlnr = v.vorlnr and
    not exists (
      select *
      from studenten s
      where s.semester <> 3 or s.matrnr <> h.matrnr)
    )
  );
