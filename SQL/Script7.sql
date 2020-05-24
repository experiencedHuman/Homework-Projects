with vorlesungenOhneHoerer as (
	select v.vorlnr
	from vorlesungen v
	except (
	select h.vorlnr
    from hoeren h)
)
select v.*
from vorlesungen v join vorlesungenOhneHoerer voh on(v.vorlnr = voh.vorlnr);
