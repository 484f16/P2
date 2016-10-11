"create or replace view Q5_view as "+
"select distinct U1.user_id as user1_id, U2.user_id as user2_id from tajik.public_users U1 "+
"join tajik.public_users U2 on U1.gender='female' and U2.gender='male' and U1.year_of_birth-U2.year_of_birth<=yearDiff and U2.year_of_birth-U1.year_of_birth<=yearDiff "+
"join tajik.public_tags T1 on U1.user_id=T1.tag_subject_id "+
"join tajik.public_tags T2 on U2.user_id=T2.tag_subject_id and T1.tag_photo_id=T2.tag_photo_id "+
"MINUS "+
"select distinct U1.user_id as user1_id, U2.user_id as user2_id from tajik.public_users U1 "+
"join tajik.public_users U2 on U1.gender='female' and U2.gender='male' and U1.year_of_birth-U2.year_of_birth<=yearDiff and U2.year_of_birth-U1.year_of_birth<=yearDiff "+
"join tajik.public_friends F on (U1.user_id=F.user1_id and U2.user_id=F.user2_id) or (U1.user_id=F.user2_id and U2.user_id=F.user1_id) "+
"join tajik.public_tags T1 on U1.user_id=T1.tag_subject_id "+
"join tajik.public_tags T2 on U2.user_id=T2.tag_subject_id and T1.tag_photo_id=T2.tag_photo_id "


"create view Q5_view2 as "+
"select count(*) as count_pair,V.user1_id,V.user2_id from Q5_view V "+
"join tajik.public_tags T1 on T1.tag_subject_id=V.user1_id "+
"join tajik.public_tags T2 on T2.tag_subject_id=V.user2_id and T2.tag_photo_id=T2.tag_photo_id "+
"group by V.user1_id,V.user2_id "+
"order by count(*) desc; "

create view Q5_view2 as 
select count(*) as count_pair,V.user1_id,V.user2_id from Q5_view V 
join tajik.public_tags T1 on T1.tag_subject_id=V.user1_id 
join tajik.public_tags T2 on T2.tag_subject_id=V.user2_id and T2.tag_photo_id=T2.tag_photo_id 
group by V.user1_id,V.user2_id 
order by count(*) desc; 



"select distinct V.count_pair,V.user1_id,U1.first_name,U1.last_name,U1.year_of_birth,V.user2_id,U2.first_name,U2.last_name,U2.year_of_birth, "+
"T1.tag_photo_id,A.album_id,A.album_name,P.photo_caption,P.photo_link "+
"from Q5_view2 V,tajik.public_users U1,tajik.public_users U2,tajik.public_tags T1,tajik.public_tags T2,tajik.public_photos P,tajik.public_albums A "+
"where V.user1_id=U1.user_id and V.user2_id=U2.user_id and T1.tag_photo_id=T2.tag_photo_id and T1.tag_subject_id=U1.user_id and T2.tag_subject_id=U2.user_id "+
"and P.photo_id=T1.tag_photo_id and P.album_id=A.album_id "+
"order by V.count_pair "

select distinct V.count_pair,V.user1_id,U1.first_name,U1.last_name,U1.year_of_birth,V.user2_id,U2.first_name,U2.last_name,U2.year_of_birth 
from Q5_view2 V,tajik.public_users U1,tajik.public_users U2
where V.user1_id=U1.user_id and V.user2_id=U2.user_id
order by V.count_pair;


drop view Q5_view;