

select c.name, j.name || ' / ' || k.name || ' / ' || e.name || ' / ' || d.name, b.code,  b.name
from sysprof a
left join artikl b on a.artikl_id = b.id
left join syssize c on b.syssize_id = c.id
left join systree d on a.systree_id = d.id
left join systree e on d.parent_id = e.id
left join systree k on e.parent_id = k.id
left join systree j on k.parent_id = j.id
order by c.id, k.name, e.name, d.name, b.code


select grup, text from elempar1 where grup > 0 union
select grup, text from elempar2 where grup > 0 union
select grup, text from furnpar1 where grup > 0 union
select grup, text from furnpar2 where grup > 0 union
select grup, text from glaspar1 where grup > 0 union
select grup, text from glaspar2 where grup > 0 union
select grup, text from joinpar1 where grup > 0 union
select grup, text from joinpar2 where grup > 0 order by 1
