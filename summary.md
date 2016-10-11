# sql

### about friends

the pair is unique and user1_id < user2_id: which is important


##### Query 6: Suggest friends based on mutual friends (15 points)

For this part, you will suggest potential friends to a user based on mutual friends.  In particular, you will find the top n pairs of users in the database who have the most common friends, but are not friends themselves.  Your output will consist of a set of pairs (user1_id, user2_id).  No pair should appear in the result set twice; you should always order the pairs so that user1_id < user2_id. 


in this problem, for pairs in friends are ordered.

there are 3 cases:(for F.user1_id and F.user2_id are ordered)

F < U1,U2

U1<F<U2

U1,U2 < F

### intersect

##### Query 9: Find the pairs of potential siblings (10 points)

A pair of users are potential siblings if they have the same last name and hometown, if they are friends, and if they are less than 10 years apart in age. While doing this, you should compute the year-wise difference and not worry about months or days. Pairs of siblings are returned with the lower user_id user first. They are ordered based on the first user_id and, in the event of a tie, the second user_id.

```

select U1.user_id,U2.user_id    

from tajik.public_users U1, tajik.public_users U2, tajik.public_user_hometown_city HC1, tajik.public_user_hometown_city HC2    

where U1.last_name=U2.last_name and U1.user_id<U2.user_id and abs(U1.year_of_birth-U2.year_of_birth)<=10 and U1.user_id=HC1.user_id and U2.user_id=HC2.user_id and HC1.hometown_city_id=HC2.hometown_city_id    

intersect    

(select distinct F1.user1_id,F1.user2_id    

from tajik.public_friends F1    

union    

select distinct F2.user2_id,F2.user1_id    

from tajik.public_friends F2);    

```

intersect need user1_id, user2_id to be entirely same.

but it can be done through where or join

### not in :: not exists

##### find the lonely user

```


-- or not exists: cannot be useful

select distinct U1.user_id from tajik.public_users U1

where not exists (select U2.user_id from tajik.public_users U2, tajik.public_friends F 

where U2.user_id = F.user1_id or U2.user_id = F.user2_id);



-- or not in 

select distinct U1.user_id from tajik.public_user_information U1

where U1.user_id not in (select U2.user_id from tajik.public_user_information U2, tajik.public_are_friends F 

where U2.user_id = F.user1_id or U2.user_id = F.user2_id);

```

### order by :: group by

- order by

need select column have the order by column

- group by 

need select column exactly the group by column , except for count(*) etc.

### HT convey (user1_id,user2_id) in pair when using group by..

count(*)

#java

### about initial

some object need to be signed some initial value before using it in if

```

           rst.next();
           UsersPair p = new UsersPair(rst.getLong(1),rst.getString(2),rst.getString(3),rst.getLong(4),rst.getString(5),rst.getString(6));
           p.addSharedFriend(rst.getLong(7),rst.getString(8),rst.getString(9));


           while (rst.next() && n>0){
//                if(rst.isFirst()){
//                    UsersPair p = new UsersPair(rst.getLong(1),rst.getString(2),rst.getString(3),rst.getLong(4),rst.getString(5),rst.getString(6));
//                    p.addSharedFriend(rst.getLong(7),rst.getString(8),rst.getString(9));
//
//                }
//                System.out.println(p.compareTo(new UsersPair(rst.getLong(1),rst.getString(2),rst.getString(3),rst.getLong(4),rst.getString(5),rst.getString(6))));
//                System.out.print(p.compareTo(p));
               if(p.user1Id.equals(rst.getLong(1)) && p.user2Id.equals(rst.getLong(4))){
                   p.addSharedFriend(rst.getLong(7),rst.getString(8),rst.getString(9));
               }
               else{
                   this.suggestedUsersPairs.add(p);
                   p = new UsersPair(rst.getLong(1),rst.getString(2),rst.getString(3),rst.getLong(4),rst.getString(5),rst.getString(6));
                   p.addSharedFriend(rst.getLong(7),rst.getString(8),rst.getString(9));
                   n--;
               }

           }

```

in this problem, I don't know why can't use (rst.isFirst());

### stmt

this is in JDBC

can read it in JDBC

don't need to do everything like

```

rst = stmt.executeQuery();



stmt.executeQuery();// if only want to create view or etc.

```

### compareTo

need to see what's it ~

and can use 

System.out.print(p.compareTo(p))to check

### treeSet

I learned it from the project sample

have .clear()

maybe order the data in it.  mayby

﻿### isFirst

I don't know why if(rst.isFirst()) cannot work?









### desc table 

users



cities

city



friends friend



public_user_current_city



public_user_hometown_city



programs



education



public_user_events



participants



albums



photos



public_tags



