-- Q4
create or replace view top_tags as select distinct count(tag_subject_id) as count_tag_subject_id,tag_photo_id from tajik.public_tags group by tag_photo_id order by count(tag_subject_id) desc;
create or replace view top_tags as select distinct count(tag_subject_id) as count_tag_subject_id,tag_photo_id from tajik.public_tags group by tag_photo_id order by count(tag_subject_id) desc



select * from top_tags where rownum < 2 order by count_tag_subject_id;



select distinct T.count_tag_subject_id,T.tag_photo_id,A.album_id,A.album_name,P.photo_caption,P.photo_link,U.user_id,U.first_name,U.last_name from top_tags T, tajik.public_tags TT, tajik.public_albums A, tajik.public_photos P,tajik.public_users U where A.album_id=P.album_id and TT.tag_photo_id=T.tag_photo_id and U.user_id=TT.tag_subject_id and P.photo_id=T.tag_photo_id and rownum < 2 order by T.count_tag_subject_id desc;
select distinct T.count_tag_subject_id,T.tag_photo_id from top_tags T, tajik.public_tags TT, tajik.public_albums A, tajik.public_photos P,tajik.public_users U where A.album_id=P.album_id and TT.tag_photo_id=T.tag_photo_id and U.user_id=TT.tag_subject_id and P.photo_id=T.tag_photo_id and rownum < 2 order by T.count_tag_subject_id desc;
select distinct T.count_tag_subject_id,T.tag_photo_id,A.album_id,A.album_name,P.photo_caption,P.photo_link,U.user_id,U.first_name,U.last_name from top_tags T, tajik.public_tags TT, tajik.public_albums A, tajik.public_photos P,tajik.public_users U where A.album_id=P.album_id and TT.tag_photo_id=T.tag_photo_id and U.user_id=TT.tag_subject_id and P.photo_id=T.tag_photo_id order by T.count_tag_subject_id desc


