
update  parlist set pname = pnumb || '-' || pname where pnumb > 0
ALTER TABLE artikls DROP CONSTRAINT IARTIKLS;
ALTER TABLE artikls ALTER COLUMN aname TYPE VARCHAR(120);
update artikls set aname = cast(atypm as varchar(2)) || '.' || cast(atypp as varchar(2)) || '-' || aname

select c.name, j.name || ' / ' || k.name || ' / ' || e.name || ' / ' || d.name, b.code,  b.name
from sysprof a
left join artikl b on a.artikl_id = b.id
left join syssize c on b.syssize_id = c.id
left join systree d on a.systree_id = d.id
left join systree e on d.parent_id = e.id
left join systree k on e.parent_id = k.id
left join systree j on k.parent_id = j.id
order by c.id, k.name, e.name, d.name, b.code

select params_id, text from elempar1 where params_id = 1010 union
select params_id, text from elempar2 where params_id = 1010 union
select params_id, text from furnpar1 where params_id = 1010 union
select params_id, text from furnpar2 where params_id = 1010 union
select params_id, text from glaspar1 where params_id = 1010 union
select params_id, text from glaspar2 where params_id = 1010 union
select params_id, text from joinpar1 where params_id = 1010 union
select params_id, text from joinpar2 where params_id = 1010 order by 1

select params_id, text from elempar1 where params_id in (4801,15010,21088,24039,24800,24801,24802,24803,25800,25801,25802,25803,31073,31074,31801,33002,33032,33074,33073,34002,34016,34049,34064,34073,34074,38108,38109,38113,39108,39109,39113,40108,40109,40113) union
select params_id, text from elempar2 where params_id in (4801,15010,21088,24039,24800,24801,24802,24803,25800,25801,25802,25803,31073,31074,31801,33002,33032,33074,33073,34002,34016,34049,34064,34073,34074,38108,38109,38113,39108,39109,39113,40108,40109,40113) union
select params_id, text from furnpar1 where params_id in (4801,15010,21088,24039,24800,24801,24802,24803,25800,25801,25802,25803,31073,31074,31801,33002,33032,33074,33073,34002,34016,34049,34064,34073,34074,38108,38109,38113,39108,39109,39113,40108,40109,40113) union
select params_id, text from furnpar2 where params_id in (4801,15010,21088,24039,24800,24801,24802,24803,25800,25801,25802,25803,31073,31074,31801,33002,33032,33074,33073,34002,34016,34049,34064,34073,34074,38108,38109,38113,39108,39109,39113,40108,40109,40113) union
select params_id, text from glaspar1 where params_id in (4801,15010,21088,24039,24800,24801,24802,24803,25800,25801,25802,25803,31073,31074,31801,33002,33032,33074,33073,34002,34016,34049,34064,34073,34074,38108,38109,38113,39108,39109,39113,40108,40109,40113) union
select params_id, text from glaspar2 where params_id in (4801,15010,21088,24039,24800,24801,24802,24803,25800,25801,25802,25803,31073,31074,31801,33002,33032,33074,33073,34002,34016,34049,34064,34073,34074,38108,38109,38113,39108,39109,39113,40108,40109,40113) union
select params_id, text from joinpar1 where params_id in (4801,15010,21088,24039,24800,24801,24802,24803,25800,25801,25802,25803,31073,31074,31801,33002,33032,33074,33073,34002,34016,34049,34064,34073,34074,38108,38109,38113,39108,39109,39113,40108,40109,40113) union
select params_id, text from joinpar2 where params_id in (4801,15010,21088,24039,24800,24801,24802,24803,25800,25801,25802,25803,31073,31074,31801,33002,33032,33074,33073,34002,34016,34049,34064,34073,34074,38108,38109,38113,39108,39109,39113,40108,40109,40113) order by 1

//Поиск артикула в конструктиве
select * from VSTASPC where anumb = 'Самор.3,9х25 с/св'
select * from CONNSPC where anumb = 'Самор.3,9х25 с/св'
select * from GLASART where anumb = 'Самор.3,9х25 с/св'
select * from FURNSPC where anumb = 'Самор.3,9х25 с/св'

select  id, bin_shr(bin_and(types, 3840), 8), bin_shr(bin_and(types, 240), 4), bin_and(types, 15) from elemdet  union
select  id, bin_shr(bin_and(types, 3840), 8), bin_shr(bin_and(types, 240), 4), bin_and(types, 15) from glasdet  union
select  id, bin_shr(bin_and(types, 3840), 8), bin_shr(bin_and(types, 240), 4), bin_and(types, 15) from joindet  union
select  id, bin_shr(bin_and(types, 3840), 8), bin_shr(bin_and(types, 240), 4), bin_and(types, 15) from furndet


SELECT u.RDB$USER, u.RDB$RELATION_NAME
FROM RDB$USER_PRIVILEGES u
WHERE u.rdb$relation_name = 'DEFROLE'
ORDER BY 1, 2

SELECT CURRENT_USER FROM RDB$DATABASE
