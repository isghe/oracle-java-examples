select sysdate, username, MACHINE, OSUSER, count (*) as sessions_per_user
from V$SESSION
group by username, MACHINE, OSUSER
order by 1, 2, 3, 4
