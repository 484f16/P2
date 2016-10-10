
-- Q1
select distinct first_name from tajik.public_users 
order by length(first_name) desc;

select distinct first_name from tajik.public_users 
order by length(first_name) asc;

select count(first_name),first_name from tajik.public_users group by first_name order by count(first_name) desc;

-- or together
-- select distinct first_name, 

-- Q2
select distinct U.user_id from tajik.public_users U, tajik.public_friends F 
where U.user_id not in F;

-- or
where U.user_id <> F.user_id;
select distinct U.user_id from tajik.public_users U, tajik.public_friends F where U.user_id <> F.user1_id and U.user_id <> F.user2_id;


-- or not exists
select distinct U1.user_id from tajik.public_users U1
where not exists (select U2.user_id from tajik.public_users U2, tajik.public_friends F 
				where U2.user_id = F.user1_id or U2.user_id = F.user2_id);


-- or not in 
select distinct U1.user_id from tajik.public_users U1
where U1.user_id not in (select U2.user_id from tajik.public_users U2, tajik.public_friends F 
				where U2.user_id = F.user1_id or U2.user_id = F.user2_id);


select distinct U1.user_id from tajik.public_users U1 where U1.user_id not in (select U2.user_id from tajik.public_users U2, tajik.public_friends F where U2.user_id = F.user1_id or U2.user_id = F.user2_id)

-- Q3
create view cur_addr as select * from tajik.public_user_current_city;
create view home_addr as select * from tajik.public_user_hometown_city;

select distinct C.user_id from cur_addr C,home_addr H where H.hometown_city_id <> C.current_city_id and H.user_id=C.user_id;


-- Q4
-- I don't know why this cannot run , after I used view , all things are correct
select distinct count(T.tag_subject_id), T.tag_photo_id from tajik.public_tags T group by T.tag_photo_id order by count(T.tag_subject_id) desc;

-- or
select distinct count(tag_subject_id),tag_photo_id from tags group by tag_photo_id;

create view tags as select * from tajik.public_tags;
select distinct count(tag_subject_id),tag_photo_id from tags group by tag_photo_id order by count(tag_subject_id) desc;



-- Q5

select distinct U1.user_id, U2.user_id from tajik.public_users U1
join tajik.public_users U2 on U1.gender='female' and U2.gender='male' and U1.year_of_birth-U2.year_of_birth<=yearDiff and U2.year_of_birth-U1.year_of_birth<=yearDiff
-- join tajik.public_friends F on (U1.user_id=F.user1_id and U2.user_id=F.user2_id) or (U1.user_id=F.user2_id and U2.user_id=F.user1_id)
join tajik.public_tags T1 on U1.user_id=T1.tag_subject_id
join tajik.public_tags T2 on U2.user_id=T2.tag_subject_id and T1.tag_photo_id=T2.tag_photo_id

-- by MINUS
select distinct U1.user_id, U2.user_id from tajik.public_users U1
join tajik.public_users U2 on U1.gender='female' and U2.gender='male' and U1.year_of_birth-U2.year_of_birth<=yearDiff and U2.year_of_birth-U1.year_of_birth<=yearDiff
join tajik.public_tags T1 on U1.user_id=T1.tag_subject_id
join tajik.public_tags T2 on U2.user_id=T2.tag_subject_id and T1.tag_photo_id=T2.tag_photo_id
MINUS 
select distinct U1.user_id, U2.user_id from tajik.public_users U1
join tajik.public_users U2 on U1.gender='female' and U2.gender='male' and U1.year_of_birth-U2.year_of_birth<=yearDiff and U2.year_of_birth-U1.year_of_birth<=yearDiff
join tajik.public_friends F on (U1.user_id=F.user1_id and U2.user_id=F.user2_id) or (U1.user_id=F.user2_id and U2.user_id=F.user1_id)
join tajik.public_tags T1 on U1.user_id=T1.tag_subject_id
join tajik.public_tags T2 on U2.user_id=T2.tag_subject_id and T1.tag_photo_id=T2.tag_photo_id;

-- Q6
select U1.user_id, U2.user_id, count(F.user2_id) from tajik.public_users U1
join tajik.public_users U2 on U1.user_id<>U2.user_id
join tajik.public_friends F1 on U1.user_id=F1.user1_id and U2.user_id<>F1.user2_id
join tajik.public_friends F2 on U2.user_id=F2.user1_id and U1.user_id<>F2.user2_id and F1.user2_id=F2.user2_id;
-- I don't konw the binary friends relationship


-- Q7
select 


-- Q8
create view friendsOfUser as 
(select user2_id from tajik.punlic_friends F where F.user1_id='user_id'
UNION
select user1_id from tajik.public_friends F where F.user2_id='user_id');


-- oldest
select F.user_id from friendsOfUser F, tajik.public_users U
where F.user_id=U.user_id
order by U.year_of_birth asc, U.month_of_birth asc, U.day_of_birth asc, F.user_id desc;

-- youngest
select F.user_id from friendsOfUser F, tajik.public_users U
where F.user_id=U.user_id
order by U.year_of_birth desc, U.month_of_birth desc, U.day_of_birth desc, F.user_id asc;

-- Q9

select U1.user_id, U2.user_id from tajik.public_users U1
join tajik.public_users U2 on U1.user_id<U2.user_id and U1.year_of_birth-U2.year_of_birth<10 and U2.year_of_birth-U1.year_of_birth<10
join tajik.public_friends F on (U1.user_id=F.user1_id and U2.user_id=F.user2_id) or (U1.user_id=F.user2_id and U2.user_id=F.user1_id)
join tajik.public_user_hometown_city H1 on U1.user_id=H1.hometown_city_id
join tajik.public_user_hometown_city H2 on U2.user_id=H2.hometown_city_id and H1.hometown_city_id=H2.hometown_city_id
order by U1.user1_id asc, U2.user2_id asc;




