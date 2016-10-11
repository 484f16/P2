Q1

select  count(*), U.first_name
from tajik.public_users U
group by U.first_name
order by 1 desc;


//java 

select distinct U.first_name
from tajik.public_users U
where length(U.first_name) = (select max(length(U2.first_name)) from tajik.public_users U2);

//

select distinct U.first_name
from tajik.public_users U
where length(U.first_name) = (select min(length(U2.first_name)) from tajik.public_users U2);



Q2
select distinct U.user_id
from tajik.public_users U
minus
select distinct F1.user1_id
from tajik.public_friends F1
minus
select distinct F2.user2_id
from tajik.public_friends F2;


Q3
select UC.user_id
from tajik.public_user_current_city UC,tajik.public_user_hometown_city UH
where UC.user_id=UH.user_id and UC.current_city_id<>UH.hometown_city_id;

Q4
select tag_photo_id, count(tag_subject_id)
from tajik.public_tags
group by tag_photo_id
order by count(tag_subject_id) desc, tag_photo_id asc;


Q5
select distinct U1.user_id,U2.user_id
from tajik.public_users U1, tajik.public_users U2, tajik.public_tags T1, tajik.public_tags T2
where U1.gender='female' and U2.gender='male' and abs(U1.year_of_birth-U2.year_of_birth)<=8
and U1.user_id=T1.tag_subject_id and U2.user_id=T2.tag_subject_id and T1.tag_photo_id=T2.tag_photo_id
minus
select distinct F1.user1_id,F1.user2_id
from tajik.public_friends F1
minus
select distinct F2.user2_id,F2.user1_id
from tajik.public_friends F2;


-- group by U2.user_id, U1.user_id;
-- order by U1.user_id asc;


Q6



Q7
select distinct C.state_name, count(E.event_id)
from tajik.public_cities C,tajik.public_user_events E
where C.city_id=E.event_city_id
group by C.state_name
order by count(E.event_id) desc;

Q8
create view F as(
select user1_id, user2_id from tajik.public_friends
where user1_id='user_id'
union
select user2_id, user1_id from tajik.public_friends
where user2_id='user_id'
);

-- youngest year
select U.user_id
from tajik.public_users U, F
where F.user2_id=U.user_id
order by U.year_of_birth desc, U.month_of_birth desc, U.day_of_birth desc;

-- oldest year
select U.user_id
from tajik.public_users U, F
where F.user2_id=U.user_id
order by U.year_of_birth asc, U.month_of_birth asc, U.day_of_birth asc;






Q9
select U1.user_id,U2.user_id
from tajik.public_users U1, tajik.public_users U2, tajik.public_user_hometown_city HC1, tajik.public_user_hometown_city HC2
where U1.last_name=U2.last_name and U1.user_id<U2.user_id and abs(U1.year_of_birth-U2.year_of_birth)<=10 and U1.user_id=HC1.user_id and U2.user_id=HC2.user_id and HC1.hometown_city_id=HC2.hometown_city_id
intersect
(select distinct F1.user1_id,F1.user2_id
from tajik.public_friends F1
union
select distinct F2.user2_id,F2.user1_id
from tajik.public_friends F2);




