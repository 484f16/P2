package project2;

import java.nio.file.attribute.UserPrincipal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyFakebookOracle extends FakebookOracle {

    static String prefix = "tajik.";

    // You must use the following variable as the JDBC connection
    Connection oracleConnection = null;

    // You must refer to the following variables for the corresponding tables in your database
    String cityTableName = null;
    String userTableName = null;
    String friendsTableName = null;
    String currentCityTableName = null;
    String hometownCityTableName = null;
    String programTableName = null;
    String educationTableName = null;
    String eventTableName = null;
    String participantTableName = null;
    String albumTableName = null;
    String photoTableName = null;
    String coverPhotoTableName = null;
    String tagTableName = null;


    // DO NOT modify this constructor
    public MyFakebookOracle(String dataType, Connection c) {
        super();
        oracleConnection = c;
        // You will use the following tables in your Java code
        cityTableName = prefix + dataType + "_CITIES";
        userTableName = prefix + dataType + "_USERS";
        friendsTableName = prefix + dataType + "_FRIENDS";
        currentCityTableName = prefix + dataType + "_USER_CURRENT_CITY";
        hometownCityTableName = prefix + dataType + "_USER_HOMETOWN_CITY";
        programTableName = prefix + dataType + "_PROGRAMS";
        educationTableName = prefix + dataType + "_EDUCATION";
        eventTableName = prefix + dataType + "_USER_EVENTS";
        albumTableName = prefix + dataType + "_ALBUMS";
        photoTableName = prefix + dataType + "_PHOTOS";
        tagTableName = prefix + dataType + "_TAGS";
    }


    @Override
    // ***** Query 0 *****
    // This query is given to your for free;
    // You can use it as an example to help you write your own code
    //
    public void findMonthOfBirthInfo() {

        // Scrollable result set allows us to read forward (using next())
        // and also backward.
        // This is needed here to support the user of isFirst() and isLast() methods,
        // but in many cases you will not need it.
        // To create a "normal" (unscrollable) statement, you would simply call
        // Statement stmt = oracleConnection.createStatement();
        //
        try (Statement stmt =
                     oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                             ResultSet.CONCUR_READ_ONLY)) {

            // For each month, find the number of users born that month
            // Sort them in descending order of count
            ResultSet rst = stmt.executeQuery("select count(*), month_of_birth from " +
                    userTableName +
                    " where month_of_birth is not null group by month_of_birth order by 1 desc");

            this.monthOfMostUsers = 0;
            this.monthOfLeastUsers = 0;
            this.totalUsersWithMonthOfBirth = 0;

            // Get the month with most users, and the month with least users.
            // (Notice that this only considers months for which the number of users is > 0)
            // Also, count how many total users have listed month of birth (i.e., month_of_birth not null)
            //
            while (rst.next()) {
                int count = rst.getInt(1);
                int month = rst.getInt(2);
                if (rst.isFirst())
                    this.monthOfMostUsers = month;
                if (rst.isLast())
                    this.monthOfLeastUsers = month;
                this.totalUsersWithMonthOfBirth += count;
            }

            // Get the names of users born in the "most" month
            rst = stmt.executeQuery("select user_id, first_name, last_name from " +
                    userTableName + " where month_of_birth=" + this.monthOfMostUsers);
            while (rst.next()) {
                Long uid = rst.getLong(1);
                String firstName = rst.getString(2);
                String lastName = rst.getString(3);
                this.usersInMonthOfMost.add(new UserInfo(uid, firstName, lastName));
            }

            // Get the names of users born in the "least" month
            rst = stmt.executeQuery("select first_name, last_name, user_id from " +
                    userTableName + " where month_of_birth=" + this.monthOfLeastUsers);
            while (rst.next()) {
                String firstName = rst.getString(1);
                String lastName = rst.getString(2);
                Long uid = rst.getLong(3);
                this.usersInMonthOfLeast.add(new UserInfo(uid, firstName, lastName));
            }

            // Close statement and result set
            rst.close();
            stmt.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

    @Override
    // ***** Query 1 *****
    // Find information about users' names:
    // (1) The longest first name (if there is a tie, include all in result)
    // (2) The shortest first name (if there is a tie, include all in result)
    // (3) The most common first name, and the number of times it appears (if there
    //      is a tie, include all in result)
    //
    public void findNameInfo() { // Query1
        // Find the following information from your database and store the information as shown
        this.longestFirstNames.add("JohnJacobJingleheimerSchmidt");
        this.shortestFirstNames.add("Al");
        this.shortestFirstNames.add("Jo");
        this.shortestFirstNames.add("Bo");
        this.mostCommonFirstNames.add("John");
        this.mostCommonFirstNames.add("Jane");
        this.mostCommonFirstNamesCount = 10;


        try (Statement stmt =
                     oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                             ResultSet.CONCUR_READ_ONLY)) {

            ResultSet rst = stmt.executeQuery("select distinct first_name from tajik.public_users order by length(first_name) desc");

            this.longestFirstNames.clear();
            this.shortestFirstNames.clear();
            this.mostCommonFirstNames.clear();
            this.mostCommonFirstNamesCount = 0;

            while (rst.next()) {

                if (this.longestFirstNames.isEmpty())
                    this.longestFirstNames.add(rst.getString(1));
                else if (rst.getString(1).length() == this.longestFirstNames.first().length())
                    this.longestFirstNames.add(rst.getString(1));
                else
                    break;
            }


            rst = stmt.executeQuery("select distinct first_name from tajik.public_users order by length(first_name) asc");
            while (rst.next()) {
                if (this.shortestFirstNames.isEmpty())
                    this.shortestFirstNames.add(rst.getString(1));
                else if (this.shortestFirstNames.first().length() == rst.getString(1).length())
                    this.shortestFirstNames.add(rst.getString(1));
                else
                    break;
            }

            rst = stmt.executeQuery("select count(first_name),first_name from tajik.public_users group by first_name order by count(first_name) desc");
            while (rst.next()) {
                if (this.mostCommonFirstNames.isEmpty()) {
                    this.mostCommonFirstNamesCount = rst.getInt(1);
                    this.mostCommonFirstNames.add(rst.getString(2));
                } else if (rst.getInt(1) == this.mostCommonFirstNamesCount) {
                    this.mostCommonFirstNames.add(rst.getString(2));
                } else
                    break;
            }

            rst.close();
            stmt.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }


    }

    @Override
    // ***** Query 2 *****
    // Find the user(s) who have no friends in the network
    //
    // Be careful on this query!
    // Remember that if two users are friends, the friends table
    // only contains the pair of user ids once, subject to
    // the constraint that user1_id < user2_id
    //
    // ***** Query 2 *****
    // Find the user(s) who have no friends in the network
    //
    // Be careful on this query!
    // Remember that if two users are friends, the friends table
    // only contains the pair of user ids once, subject to
    // the constraint that user1_id < user2_id
    //
    public void lonelyUsers() {
        // Find the following information from your database and store the information as shown
        this.lonelyUsers.add(new UserInfo(10L, "Billy", "SmellsFunny"));
        this.lonelyUsers.add(new UserInfo(11L, "Jenny", "BadBreath"));


        try (Statement stmt =
                     oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                             ResultSet.CONCUR_READ_ONLY)) {


            ResultSet rst = stmt.executeQuery("select distinct U1.user_id,U1.first_name,U1.last_name from tajik.public_users U1 where U1.user_id not in (select U2.user_id from tajik.public_users U2, tajik.public_friends F where U2.user_id = F.user1_id or U2.user_id = F.user2_id)");

            this.lonelyUsers.clear();

            while (rst.next()) {
                this.lonelyUsers.add(new UserInfo(rst.getLong(1), rst.getString(2), rst.getString(3)));

            }

            rst.close();
            stmt.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

    @Override
    // ***** Query 3 *****
    // Find the users who do not live in their hometowns
    // (I.e., current_city != hometown_city)
    //
    public void liveAwayFromHome() throws SQLException {
        this.liveAwayFromHome.add(new UserInfo(11L, "Heather", "Movalot"));

        try (Statement stmt =
                     oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                             ResultSet.CONCUR_READ_ONLY)) {


            ResultSet rst = stmt.executeQuery("select distinct C.user_id,U.first_name,U.last_name from tajik.public_user_current_city C, tajik.public_user_hometown_city H, tajik.public_users U where H.hometown_city_id <> C.current_city_id and H.user_id=C.user_id and U.user_id=C.user_id");


            this.liveAwayFromHome.clear();
            while (rst.next()) {
                this.liveAwayFromHome.add(new UserInfo(rst.getLong(1), rst.getString(2), rst.getString(3)));
            }


            rst.close();
            stmt.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }

    }


    @Override
    // **** Query 4 ****
    // Find the top-n photos based on the number of tagged users
    // If there are ties, choose the photo with the smaller numeric PhotoID first
    //
    public void findPhotosWithMostTags(int n) {
        // String photoId = "1234567";
        // String albumId = "123456789";
        // String albumName = "album1";
        // String photoCaption = "caption1";
        // String photoLink = "http://google.com";
        // PhotoInfo p = new PhotoInfo(photoId, albumId, albumName, photoCaption, photoLink);
        // TaggedPhotoInfo tp = new TaggedPhotoInfo(p);
        // tp.addTaggedUser(new UserInfo(12345L, "taggedUserFirstName1", "taggedUserLastName1"));
        // tp.addTaggedUser(new UserInfo(12345L, "taggedUserFirstName2", "taggedUserLastName2"));
        // this.photosWithMostTags.add(tp);


        PhotoInfo p;
        TaggedPhotoInfo tp;
        try (Statement stmt =
                     oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                             ResultSet.CONCUR_READ_ONLY)) {


            stmt.executeQuery("create or replace view top_tags as select distinct count(tag_subject_id) as count_tag_subject_id,tag_photo_id from tajik.public_tags group by tag_photo_id order by count(tag_subject_id) desc");

            ResultSet rst = stmt.executeQuery("select distinct T.count_tag_subject_id,T.tag_photo_id,A.album_id,A.album_name,P.photo_caption,P.photo_link,U.user_id,U.first_name,U.last_name from top_tags T, tajik.public_tags TT, tajik.public_albums A, tajik.public_photos P,tajik.public_users U where A.album_id=P.album_id and TT.tag_photo_id=T.tag_photo_id and U.user_id=TT.tag_subject_id and P.photo_id=T.tag_photo_id order by T.count_tag_subject_id desc,T.tag_photo_id asc");



            rst.next();

            p = new PhotoInfo(rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6));
            tp = new TaggedPhotoInfo(p);
            tp.addTaggedUser(new UserInfo(rst.getLong(7), rst.getString(8), rst.getString(9)));
                    


            while (rst.next() && n>0) {
                // p = new PhotoInfo(rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6));
                if(p.photoId.equals(rst.getString(2))){
                    tp.addTaggedUser(new UserInfo(rst.getLong(7), rst.getString(8), rst.getString(9)));
                } 
                else {
                    this.photosWithMostTags.add(tp);
                    p = new PhotoInfo(rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6));
                    tp = new TaggedPhotoInfo(p);
                    tp.addTaggedUser(new UserInfo(rst.getLong(7), rst.getString(8), rst.getString(9)));
                    n--;    
                }

            }

            stmt.executeQuery("drop view top_tags");

            rst.close();
            stmt.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }

    }


    @Override
    // **** Query 5 ****
    // Find suggested "match pairs" of users, using the following criteria:
    // (1) One of the users is female, and the other is male
    // (2) Their age difference is within "yearDiff"
    // (3) They are not friends with one another
    // (4) They should be tagged together in at least one photo
    //
    // You should return up to n "match pairs"
    // If there are more than n match pairs, you should break ties as follows:
    // (i) First choose the pairs with the largest number of shared photos
    // (ii) If there are still ties, choose the pair with the smaller user_id for the female
    // (iii) If there are still ties, choose the pair with the smaller user_id for the male
    //
    public void matchMaker(int n, int yearDiff) {
        // Long girlUserId = 123L;
        // String girlFirstName = "girlFirstName";
        // String girlLastName = "girlLastName";
        // int girlYear = 1988;
        // Long boyUserId = 456L;
        // String boyFirstName = "boyFirstName";
        // String boyLastName = "boyLastName";
        // int boyYear = 1986;
        // MatchPair mp = new MatchPair(girlUserId, girlFirstName, girlLastName,
        //         girlYear, boyUserId, boyFirstName, boyLastName, boyYear);
        // String sharedPhotoId = "12345678";
        // String sharedPhotoAlbumId = "123456789";
        // String sharedPhotoAlbumName = "albumName";
        // String sharedPhotoCaption = "caption";
        // String sharedPhotoLink = "link";
        // mp.addSharedPhoto(new PhotoInfo(sharedPhotoId, sharedPhotoAlbumId,
        //         sharedPhotoAlbumName, sharedPhotoCaption, sharedPhotoLink));
        // this.bestMatches.add(mp);

        MatchPair mp;

        try (Statement stmt =
                     oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                             ResultSet.CONCUR_READ_ONLY)) {

            stmt.executeQuery("create or replace view Q5_view as " +
                    "select distinct U1.user_id as user1_id, U2.user_id as user2_id from tajik.public_users U1 " +
                    "join tajik.public_users U2 on U1.gender='female' and U2.gender='male' and U1.year_of_birth-U2.year_of_birth<=" + yearDiff + " and U2.year_of_birth-U1.year_of_birth<=" + yearDiff +
                    "join tajik.public_tags T1 on U1.user_id=T1.tag_subject_id " +
                    "join tajik.public_tags T2 on U2.user_id=T2.tag_subject_id and T1.tag_photo_id=T2.tag_photo_id " +
                    "MINUS " +
                    "select distinct U1.user_id as user1_id, U2.user_id as user2_id from tajik.public_users U1 " +
                    "join tajik.public_users U2 on U1.gender='female' and U2.gender='male' and U1.year_of_birth-U2.year_of_birth<=" + yearDiff + " and U2.year_of_birth-U1.year_of_birth<=" + yearDiff +
                    "join tajik.public_friends F on (U1.user_id=F.user1_id and U2.user_id=F.user2_id) or (U1.user_id=F.user2_id and U2.user_id=F.user1_id) " +
                    "join tajik.public_tags T1 on U1.user_id=T1.tag_subject_id " +
                    "join tajik.public_tags T2 on U2.user_id=T2.tag_subject_id and T1.tag_photo_id=T2.tag_photo_id "
            );


            stmt.executeQuery("create or replace view Q5_view2 as " +
                    "select count(*) as count_pair,V.user1_id,V.user2_id from Q5_view V " +
                    "join tajik.public_tags T1 on T1.tag_subject_id=V.user1_id " +
                    "join tajik.public_tags T2 on T2.tag_subject_id=V.user2_id and T2.tag_photo_id=T2.tag_photo_id " +
                    "group by V.user1_id,V.user2_id " +
                    "order by count(*) desc "
            );


            ResultSet rst = stmt.executeQuery("select distinct V.count_pair,V.user1_id,U1.first_name,U1.last_name,U1.year_of_birth,V.user2_id,U2.first_name,U2.last_name,U2.year_of_birth, " +
                    "T1.tag_photo_id,A.album_id,A.album_name,P.photo_caption,P.photo_link " +
                    "from Q5_view2 V,tajik.public_users U1,tajik.public_users U2,tajik.public_tags T1,tajik.public_tags T2,tajik.public_photos P,tajik.public_albums A " +
                    "where V.user1_id=U1.user_id and V.user2_id=U2.user_id and T1.tag_photo_id=T2.tag_photo_id and T1.tag_subject_id=U1.user_id and T2.tag_subject_id=U2.user_id " +
                    "and P.photo_id=T1.tag_photo_id and P.album_id=A.album_id " +
                    "order by V.count_pair "
            );

            while (rst.next() && n > 0) {

                mp = new MatchPair(rst.getLong(2), rst.getString(3), rst.getString(4), rst.getInt(5),
                        rst.getLong(6), rst.getString(7), rst.getString(8), rst.getInt(9));


                mp.addSharedPhoto(new PhotoInfo(rst.getString(10), rst.getString(11), rst.getString(12), rst.getString(13), rst.getString(14)));

                this.bestMatches.add(mp);
                n--;

            }

            stmt.executeQuery("drop view Q5_view");
            stmt.executeQuery("drop view Q5_view2");
            // Close statement and result set
            rst.close();
            stmt.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

    // **** Query 6 ****
    // Suggest users based on mutual friends
    //
    // Find the top n pairs of users in the database who have the most
    // common friends, but are not friends themselves.
    //
    // Your output will consist of a set of pairs (user1_id, user2_id)
    // No pair should appear in the result twice; you should always order the pairs so that
    // user1_id < user2_id
    //
    // If there are ties, you should give priority to the pair with the smaller user1_id.
    // If there are still ties, give priority to the pair with the smaller user2_id.
    //
    @Override
    public void suggestFriendsByMutualFriends(int n) {
//        Long user1_id = 123L;
//        String user1FirstName = "User1FirstName";
//        String user1LastName = "User1LastName";
//        Long user2_id = 456L;
//        String user2FirstName = "User2FirstName";
//        String user2LastName = "User2LastName";
//        UsersPair p = new UsersPair(user1_id, user1FirstName, user1LastName, user2_id, user2FirstName, user2LastName);
//
//        p.addSharedFriend(567L, "sharedFriend1FirstName", "sharedFriend1LastName");
//        p.addSharedFriend(678L, "sharedFriend2FirstName", "sharedFriend2LastName");
//        p.addSharedFriend(789L, "sharedFriend3FirstName", "sharedFriend3LastName");
//        this.suggestedUsersPairs.add(p);

//         UsersPair p;

        try (Statement stmt =
                     oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                             ResultSet.CONCUR_READ_ONLY)) {

            // For each month, find the number of users born that month
            // Sort them in descending order of count
            stmt.executeQuery("create or replace view view_Q6 as "+
                            "select U1.user_id as user1_id, U2.user_id as user2_id ,count(*) as count_friends "+
                            "from tajik.public_users U1,tajik.public_users U2,tajik.public_friends F1,tajik.public_friends F2 "+
                            "where U1.user_id<U2.user_id and ((F1.user2_id=U1.user_id and F2.user2_id=U2.user_id and F1.user1_id=F2.user1_id) "+
                            "or (F1.user1_id=U1.user_id and F2.user2_id=U2.user_id and F1.user2_id=F2.user1_id) "+
                            "or (U1.user_id=F1.user1_id and U2.user_id=F2.user1_id and F1.user2_id=F2.user2_id)) "+
                            "group by U1.user_id,U2.user_id order by count(*) desc");



            ResultSet rst = stmt.executeQuery("(select V.user1_id as user1_id,U1.first_name,U2.last_name,V.user2_id as user2_id,U2.first_name,U2.last_name,F1.user1_id as user3_id,U3.first_name,U3.last_name,V.count_friends as count_friends "+
                            "from view_Q6 V, tajik.public_users U1, tajik.public_users U2, tajik.public_friends F1,tajik.public_friends F2,tajik.public_users U3 "+
                            "where V.user1_id=U1.user_id and V.user2_id=U2.user_id and F1.user2_id=U1.user_id and F2.user2_id=U2.user_id and F1.user1_id=F2.user1_id and F1.user1_id=U3.user_id "+
                            "UNION "+
                            "select V.user1_id,U1.first_name,U2.last_name,V.user2_id,U2.first_name,U2.last_name,F1.user2_id as user3_id,U3.first_name,U3.last_name,V.count_friends "+
                            "from view_Q6 V, tajik.public_users U1, tajik.public_users U2, tajik.public_friends F1,tajik.public_friends F2,tajik.public_users U3 "+
                            "where V.user1_id=U1.user_id and V.user2_id=U2.user_id and F1.user1_id=U1.user_id and F2.user2_id=U2.user_id and F1.user2_id=F2.user1_id and F1.user2_id=U3.user_id "+
                            "UNION "+
                            "select V.user1_id,U1.first_name,U2.last_name,V.user2_id,U2.first_name,U2.last_name,F1.user2_id as user3_id,U3.first_name,U3.last_name,V.count_friends "+
                            "from view_Q6 V, tajik.public_users U1, tajik.public_users U2, tajik.public_friends F1,tajik.public_friends F2,tajik.public_users U3 "+
                            "where V.user1_id=U1.user_id and V.user2_id=U2.user_id and F1.user1_id=U1.user_id and F2.user1_id=U2.user_id and F1.user2_id=F2.user2_id and F1.user2_id=U3.user_id) "+
                            "order by count_friends desc, user1_id asc, user2_id asc ");


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

            stmt.executeQuery("drop view view_Q6");

            // Close statement and result set
            rst.close();
            stmt.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

    @Override
    // ***** Query 7 *****
    //
    // Find the name of the state with the most events, as well as the number of
    // events in that state.  If there is a tie, return the names of all of the (tied) states.
    //

    public void findEventStates() {
//        this.eventCount = 12;
//        this.popularStateNames.add("Michigan");
//        this.popularStateNames.add("California");

        try (Statement stmt =
                     oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                             ResultSet.CONCUR_READ_ONLY)) {

            // For each month, find the number of users born that month
            // Sort them in descending order of count
            ResultSet rst = stmt.executeQuery("select C.state_name,count(C.state_name) from tajik.public_cities C join tajik.public_user_events E on C.city_id=E.event_city_id group by C.state_name order by count(C.state_name) desc");

            if (rst.next()) {
                this.eventCount = rst.getInt(2);
                this.popularStateNames.add(rst.getString(1));
            }

            while (rst.next() && (this.eventCount == rst.getInt(2))) {

                this.popularStateNames.add(rst.getString(1));

            }
            // Close statement and result set
            rst.close();
            stmt.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

    @Override
    // ***** Query 8 *****
    // Given the ID of a user, find information about that
    // user's oldest friend and youngest friend
    //
    // If two users have exactly the same age, meaning that they were born
    // on the same day, then assume that the one with the larger user_id is older
    //
    public void findAgeInfo(Long user_id) {
        this.oldestFriend = new UserInfo(1L, "Oliver", "Oldham");
        this.youngestFriend = new UserInfo(25L, "Yolanda", "Young");


        try (Statement stmt =
                     oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                             ResultSet.CONCUR_READ_ONLY)) {

            // For each month, find the number of users born that month
            // Sort them in descending order of count
            // ResultSet rst = stmt.executeQuery("create view friendsOfUser as (select user2_id from tajik.public_friends F where F.user1_id=" + "\'" + user_id + "\'" + " UNION select user1_id from tajik.public_friends F where F.user2_id=" + "\'" + user_id + "\')");
            // while (rst.next()) {

            // }

            stmt.executeQuery("create or replace view friendsOfUser as (select user2_id as user_id from tajik.public_friends F where F.user1_id=" + "\'" + user_id + "\'" + " UNION select user1_id as user_id from tajik.public_friends F where F.user2_id=" + "\'" + user_id + "\')");


            ResultSet rst = stmt.executeQuery("select F.user_id,U.first_name,U.last_name from friendsOfUser F, tajik.public_users U where F.user_id=U.user_id order by U.year_of_birth asc, U.month_of_birth asc, U.day_of_birth asc, F.user_id desc");
            if (rst.next()) {
                this.oldestFriend = new UserInfo(rst.getLong(1), rst.getString(2), rst.getString(3));
            }

            rst = stmt.executeQuery("select F.user_id,U.first_name,U.last_name from friendsOfUser F, tajik.public_users U where F.user_id=U.user_id order by U.year_of_birth desc, U.month_of_birth desc, U.day_of_birth desc, F.user_id asc");
            if (rst.next()) {
                this.youngestFriend = new UserInfo(rst.getLong(1), rst.getString(2), rst.getString(3));
            }


            stmt.executeQuery("drop view friendsOfUser");

            // Close statement and result set
            rst.close();
            stmt.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

    @Override
    //   ***** Query 9 *****
    //
    // Find pairs of potential siblings.
    //
    // A pair of users are potential siblings if they have the same last name and hometown, if they are friends, and
    // if they are less than 10 years apart in age.  Pairs of siblings are returned with the lower user_id user first
    // on the line.  They are ordered based on the first user_id and in the event of a tie, the second user_id.
    //
    //
    public void findPotentialSiblings() {
//        Long user1_id = 123L;
//        String user1FirstName = "User1FirstName";
//        String user1LastName = "User1LastName";
//        Long user2_id = 456L;
//        String user2FirstName = "User2FirstName";
//        String user2LastName = "User2LastName";
//        SiblingInfo s = new SiblingInfo(user1_id, user1FirstName, user1LastName, user2_id, user2FirstName, user2LastName);
//        this.siblings.add(s);

        SiblingInfo s;
        
        try (Statement stmt =
                     oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                             ResultSet.CONCUR_READ_ONLY)) {

            // For each month, find the number of users born that month
            // Sort them in descending order of count
            ResultSet rst = stmt.executeQuery("select U1.user_id,U1.first_name,U1.last_name, U2.user_id,U2.first_name,U2.last_name from tajik.public_users U1 " +
                    "join tajik.public_users U2 on U1.user_id<U2.user_id and U1.year_of_birth-U2.year_of_birth<10 and U2.year_of_birth-U1.year_of_birth<10 and U1.last_name=U2.last_name " +
                    "join tajik.public_friends F on (U1.user_id=F.user1_id and U2.user_id=F.user2_id) or (U1.user_id=F.user2_id and U2.user_id=F.user1_id) " +
                    "join tajik.public_user_hometown_city H1 on U1.user_id=H1.user_id " +
                    "join tajik.public_user_hometown_city H2 on U2.user_id=H2.user_id and H1.hometown_city_id=H2.hometown_city_id " +
                    "order by U1.user_id asc, U2.user_id asc ");

            while (rst.next()) {
                s = new SiblingInfo(rst.getLong(1), rst.getString(2), rst.getString(3), rst.getLong(4), rst.getString(5), rst.getString(6));
                this.siblings.add(s);

            }


            // Close statement and result set
            rst.close();
            stmt.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }


}
