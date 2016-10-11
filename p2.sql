
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

select distinct C.user_id,U.first_name,U.last_name from tajik.public_user_current_city C, tajik.public_user_hometown_city H, tajik.public_users U where H.hometown_city_id <> C.current_city_id and H.user_id=C.user_id and U.user_id=C.user_id
-- Q4
-- I don't know why this cannot run , after I used view , all things are correct
select distinct count(T.tag_subject_id), T.tag_photo_id from tajik.public_tags T group by T.tag_photo_id order by count(T.tag_subject_id) desc;

-- or
select distinct count(tag_subject_id),tag_photo_id from tags group by tag_photo_id;

create view tags as select * from tajik.public_tags;
select distinct count(tag_subject_id),tag_photo_id from tags group by tag_photo_id order by count(tag_subject_id) desc;


create view top_tags as select distinct count(tag_subject_id) as count_tag_subject_id,tag_photo_id from tajik.public_tags group by tag_photo_id order by count(tag_subject_id) desc;
create view top_tags as select distinct count(tag_subject_id) as count_tag_subject_id,tag_photo_id from tajik.public_tags group by tag_photo_id order by count(tag_subject_id) desc

select distinct T.count_tag_subject_id,T.tag_photo_id,A.album_id,A.album_name,P.photo_caption,P.photo_link,U.user_id,U.first_name,U.last_name from top_tags T, tajik.public_tags TT, tajik.public_albums A, tajik.public_photos P,tajik.public_users U where A.album_id=P.album_id and TT.tag_photo_id=T.tag_photo_id and U.user_id=TT.tag_subject_id and P.photo_id=T.tag_photo_id order by T.count_tag_subject_id desc;


select distinct T.count_tag_subject_id,T.tag_photo_id,P.photo_caption,U.user_id
from top_tags T, tajik.public_tags TT, tajik.public_albums A, tajik.public_photos P,tajik.public_users U 
where T.tag_photo_id=TT.tag_photo_id and TT.tag_subject_id=U.user_id and P.photo_id=T.tag_photo_id and A.album_id=P.album_id;



select distinct T.count_tag_subject_id,T.tag_photo_id,A.album_id,A.album_name,P.photo_caption,P.photo_link,U.user_id,U.first_name,U.last_name 
from top_tags T, tajik.public_tags TT, tajik.public_albums A, tajik.public_photos P,tajik.public_users U 
where A.album_id=P.album_id and TT.tag_photo_id=T.tag_photo_id and U.user_id=TT.tag_subject_id 
and P.photo_id=T.tag_photo_id 
order by T.count_tag_subject_id desc;


        String photoId = "1234567";
        String albumId = "123456789";
        String albumName = "album1";
        String photoCaption = "caption1";
        String photoLink = "http://google.com";
        PhotoInfo p = new PhotoInfo(photoId, albumId, albumName, photoCaption, photoLink);
        TaggedPhotoInfo tp = new TaggedPhotoInfo(p);
        tp.addTaggedUser(new UserInfo(12345L, "taggedUserFirstName1", "taggedUserLastName1"));
        tp.addTaggedUser(new UserInfo(12345L, "taggedUserFirstName2", "taggedUserLastName2"));
        this.photosWithMostTags.add(tp);

select distinct count(T.tag_subject_id),T.tag_photo_id,A.album_id,A.album_name,P.photo_caption,P.photo_link,U.user_id,U.first_name,U.last_name from tajik.public_tags T, tajik.public_albums A, tajik.public_photos P,tajik.public_users U where A.album_id=P.album_id and U.user_id=T.tag_subject_id and P.photo_id=T.tag_photo_id group by T.tag_photo_id order by count(tag_subject_id) desc;
select distinct count(T.tag_subject_id),T.tag_photo_id,A.album_id,A.album_name,P.photo_caption,P.photo_link,U.user_id,U.first_name,U.last_name from tajik.public_tags T, tajik.public_albums A, tajik.public_photos P,tajik.public_users U where A.album_id=P.album_id and U.user_id=T.tag_subject_id and P.photo_id=T.tag_photo_id group by T.tag_photo_id;

-- Q5

select distinct U1.user_id, U2.user_id from tajik.public_users U1
join tajik.public_users U2 on U1.gender='female' and U2.gender='male' and U1.year_of_birth-U2.year_of_birth<=yearDiff and U2.year_of_birth-U1.year_of_birth<=yearDiff
-- join tajik.public_friends F on (U1.user_id=F.user1_id and U2.user_id=F.user2_id) or (U1.user_id=F.user2_id and U2.user_id=F.user1_id)
join tajik.public_tags T1 on U1.user_id=T1.tag_subject_id
join tajik.public_tags T2 on U2.user_id=T2.tag_subject_id and T1.tag_photo_id=T2.tag_photo_id

-- by MINUS
select U1.user_id, U2.user_id from tajik.public_users U1
join tajik.public_users U2 on U1.gender='female' and U2.gender='male' and U1.year_of_birth-U2.year_of_birth<=10 and U2.year_of_birth-U1.year_of_birth<=10
join tajik.public_tags T1 on U1.user_id=T1.tag_subject_id
join tajik.public_tags T2 on U2.user_id=T2.tag_subject_id and T1.tag_photo_id=T2.tag_photo_id
group by U1.user_id,U2.user_id
order by count(*)
MINUS 
select U1.user_id, U2.user_id from tajik.public_users U1
join tajik.public_users U2 on U1.gender='female' and U2.gender='male' and U1.year_of_birth-U2.year_of_birth<=10 and U2.year_of_birth-U1.year_of_birth<=10
join tajik.public_friends F on (U1.user_id=F.user1_id and U2.user_id=F.user2_id) or (U1.user_id=F.user2_id and U2.user_id=F.user1_id)
join tajik.public_tags T1 on U1.user_id=T1.tag_subject_id
join tajik.public_tags T2 on U2.user_id=T2.tag_subject_id and T1.tag_photo_id=T2.tag_photo_id
group by U1.user_id,U2.user_id
order by count(*);



select distinct U1.user_id, U2.user_id from tajik.public_users U1
join tajik.public_users U2 on U1.gender='female' and U2.gender='male' and U1.year_of_birth-U2.year_of_birth<=yearDiff and U2.year_of_birth-U1.year_of_birth<=yearDiff
join tajik.public_tags T1 on U1.user_id=T1.tag_subject_id
join tajik.public_tags T2 on U2.user_id=T2.tag_subject_id and T1.tag_photo_id=T2.tag_photo_id
group by U1.user_id,U2.user_id
MINUS 
select distinct U1.user_id, U2.user_id from tajik.public_users U1
join tajik.public_users U2 on U1.gender='female' and U2.gender='male' and U1.year_of_birth-U2.year_of_birth<=yearDiff and U2.year_of_birth-U1.year_of_birth<=yearDiff
join tajik.public_friends F on (U1.user_id=F.user1_id and U2.user_id=F.user2_id) or (U1.user_id=F.user2_id and U2.user_id=F.user1_id)
join tajik.public_tags T1 on U1.user_id=T1.tag_subject_id
join tajik.public_tags T2 on U2.user_id=T2.tag_subject_id and T1.tag_photo_id=T2.tag_photo_id
group by U1.user_id,U2.user_id;

-- Q6
-- the newest
create or replace view view_Q6 as
select U1.user_id as user1_id, U2.user_id as user2_id ,count(*) as count_friends
from tajik.public_users U1,tajik.public_users U2,tajik.public_friends F1,tajik.public_friends F2
where U1.user_id<U2.user_id and ((F1.user2_id=U1.user_id and F2.user2_id=U2.user_id and F1.user1_id=F2.user1_id)
or (F1.user1_id=U1.user_id and F2.user2_id=U2.user_id and F1.user2_id=F2.user1_id)
or (U1.user_id=F1.user1_id and U2.user_id=F2.user1_id and F1.user2_id=F2.user2_id))
group by U1.user_id,U2.user_id order by count(*) desc;

"create or replace view view_Q6 as "+
"select U1.user_id as user1_id, U2.user_id as user2_id ,count(*) as count_friends "+
"from tajik.public_users U1,tajik.public_users U2,tajik.public_friends F1,tajik.public_friends F2 "+
"where U1.user_id<U2.user_id and ((F1.user2_id=U1.user_id and F2.user2_id=U2.user_id and F1.user1_id=F2.user1_id) "+
"or (F1.user1_id=U1.user_id and F2.user2_id=U2.user_id and F1.user2_id=F2.user1_id) "+
"or (U1.user_id=F1.user1_id and U2.user_id=F2.user1_id and F1.user2_id=F2.user2_id)) "+
"group by U1.user_id,U2.user_id order by count(*) desc;"

(select V.user1_id,U1.first_name,U2.last_name,V.user2_id,U2.first_name,U2.last_name,F1.user1_id as user3_id,U3.first_name,U3.last_name,V.count_friends 
from view_Q6 V, tajik.public_users U1, tajik.public_users U2, tajik.public_friends F1,tajik.public_friends F2,tajik.public_users U3
where V.user1_id=U1.user_id and V.user2_id=U2.user_id and F1.user2_id=U1.user_id and F2.user2_id=U2.user_id and F1.user1_id=F2.user1_id and F1.user1_id=U3.user_id
UNION
select V.user1_id,U1.first_name,U2.last_name,V.user2_id,U2.first_name,U2.last_name,F1.user2_id as user3_id,U3.first_name,U3.last_name,V.count_friends
from view_Q6 V, tajik.public_users U1, tajik.public_users U2, tajik.public_friends F1,tajik.public_friends F2,tajik.public_users U3
where V.user1_id=U1.user_id and V.user2_id=U2.user_id and F1.user1_id=U1.user_id and F2.user2_id=U2.user_id and F1.user2_id=F2.user1_id and F1.user2_id=U3.user_id
UNION
select V.user1_id,U1.first_name,U2.last_name,V.user2_id,U2.first_name,U2.last_name,F1.user2_id as user3_id,U3.first_name,U3.last_name,V.count_friends 
from view_Q6 V, tajik.public_users U1, tajik.public_users U2, tajik.public_friends F1,tajik.public_friends F2,tajik.public_users U3
where V.user1_id=U1.user_id and V.user2_id=U2.user_id and F1.user1_id=U1.user_id and F2.user1_id=U2.user_id and F1.user2_id=F2.user2_id and F1.user2_id=U3.user_id);
order by V.count_friends desc, V.user1_id asc, V.user2_id asc;








create or replace view view_Q6 as 
select U1.user_id as user1_id, U2.user_id as user2_id, count(*) as count_pair from tajik.public_users U1
join tajik.public_users U2 on U1.user_id<>U2.user_id
join tajik.public_friends F1 on U1.user_id=F1.user1_id and U2.user_id<>F1.user2_id
join tajik.public_friends F2 on U2.user_id=F2.user1_id and U1.user_id<>F2.user2_id and F1.user2_id=F2.user2_id
group by U1.user_id,U2.user_id
order by count(*) desc,U1.user_id asc,U2.user_id asc;




"select V.user1_id,U1.first_name,U2.last_name,V.user2_id,U2.first_name,U2.last_name,F2.user2_id,U3.first_name,U3.last_name,V.count_pair from view_Q6 V "+
"join tajik.public_users U1 on V.user1_id=U1.user_id "+
"join tajik.public_users U2 on V.user2_id=U2.user_id "+ 
"join tajik.public_friends F1 on U1.user_id=F1.user1_id and U2.user_id<>F1.user2_id "+
"join tajik.public_friends F2 on U2.user_id=F2.user1_id and U1.user_id<>F2.user2_id and F1.user2_id=F2.user2_id "+
"join tajik.public_users U3 on F2.user2_id=U3.user_id "+
"order by V.count_pair desc,U1.user_id asc,U2.user_id asc "

"create view view_Q6 as select U1.user_id as user1_id, U2.user_id as user2_id, count(*)as count_pair,F1.user2_id as user3_id from tajik.public_users U1 join tajik.public_users U2 on U1.user_id<>U2.user_id join tajik.public_friends F1 on U1.user_id=F1.user1_id and U2.user_id<>F1.user2_id join tajik.public_friends F2 on U2.user_id=F2.user1_id and U1.user_id<>F2.user2_id and F1.user2_id=F2.user2_id group by U1.user_id,U2.user_id order by count(*) desc,U1.user_id asc,U2.user_id asc"
-- I don't konw the binary friends relationship
select V.user1_id,U1.first_name,U2.last_name,V.user2_id,U2.first_name,U2.last_name,V.user3_id,U3.first_name,U3.last_name,V.count_pair 
from view_Q6 V, tajik.public_users U1, tajik.public_users U2
where V.user1_id=U1.user_id and V.user2_id=U2.user_id
order by V.count_pair desc,U1.user_id asc,U2.user_id asc;

select V.user1_id,U1.first_name,U2.last_name,V.user2_id,U2.first_name,U2.last_name,V.user3_id,U3.first_name,U3.last_name,V.count_pair from view_Q6 V, tajik.public_users U1, tajik.public_users U2 where V.user1_id=U1.user_id and V.user2_id=U2.user_id order by V.count_pair desc,U1.user_id asc,U2.user_id asc

-- Q7
select C.state_name,count(C.state_name) from tajik.public_cities C
join tajik.public_user_events E on C.city_id=E.event_city_id
group by C.state_name
order by count(C.state_name) desc;


select C.state_name,count(*) from tajik.public_cities C join tajik.public_user_events E on C.city_id=E.event_city_id group by C.state_name order by count(*) desc

-- Q8
create view friendsOfUser as 
(select user2_id from tajik.public_friends F where F.user1_id='user_id'
UNION
select user1_id from tajik.public_friends F where F.user2_id='user_id');

create view friendsOfUser as (select user2_id from tajik.public_friends F where F.user1_id='user_id' UNION select user1_id from tajik.public_friends F where F.user2_id='user_id')

-- oldest
select F.user_id from friendsOfUser F, tajik.public_users U
where F.user_id=U.user_id
order by U.year_of_birth asc, U.month_of_birth asc, U.day_of_birth asc, F.user_id desc;

select F.user_id,U.first_name,U.last_name from friendsOfUser F, tajik.public_users U where F.user_id=U.user_id order by U.year_of_birth asc, U.month_of_birth asc, U.day_of_birth asc, F.user_id desc

-- youngest
select F.user_id from friendsOfUser F, tajik.public_users U
where F.user_id=U.user_id
order by U.year_of_birth desc, U.month_of_birth desc, U.day_of_birth desc, F.user_id asc;

select F.user_id,U.first_name,U.last_name from friendsOfUser F, tajik.public_users U where F.user_id=U.user_id order by U.year_of_birth desc, U.month_of_birth desc, U.day_of_birth desc, F.user_id asc;


drop view friendsOfUser;

-- Q9

select U1.user_id,U1.first_name,U1.last_name, U2.user_id,U2.first_name,U2.last_name from tajik.public_users U1
join tajik.public_users U2 on U1.user_id<U2.user_id and U1.year_of_birth-U2.year_of_birth<10 and U2.year_of_birth-U1.year_of_birth<10
join tajik.public_friends F on (U1.user_id=F.user1_id and U2.user_id=F.user2_id) or (U1.user_id=F.user2_id and U2.user_id=F.user1_id)
join tajik.public_user_hometown_city H1 on U1.user_id=H1.hometown_city_id
join tajik.public_user_hometown_city H2 on U2.user_id=H2.hometown_city_id and H1.hometown_city_id=H2.hometown_city_id
order by U1.user_id asc, U2.user_id asc;


select U1.user_id,U1.first_name,U1.last_name, U2.user_id,U2.first_name,U2.last_name from tajik.public_users U1
join tajik.public_users U2 on U1.user_id<U2.user_id and U1.year_of_birth-U2.year_of_birth<10 and U2.year_of_birth-U1.year_of_birth<10 and U1.last_name=U2.last_name
join tajik.public_friends F on U1.user_id=F.user1_id and U2.user_id=F.user2_id
join tajik.public_user_hometown_city H1 on U1.user_id=H1.user_id
join tajik.public_user_hometown_city H2 on U2.user_id=H2.user and H1.hometown_city_id=H2.hometown_city_id
order by U1.user_id asc, U2.user_id asc;



